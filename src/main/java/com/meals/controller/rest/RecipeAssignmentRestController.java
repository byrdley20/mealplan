package com.meals.controller.rest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.meals.common.AppConstants;
import com.meals.domain.DateTag;
import com.meals.domain.Event;
import com.meals.domain.MealType;
import com.meals.domain.Recipe;
import com.meals.domain.RecipeAssignment;
import com.meals.domain.RecipeHistory;
import com.meals.domain.RecipeMealType;
import com.meals.domain.RecipeTag;
import com.meals.domain.Tag;
import com.meals.dto.RecipeDTO;
import com.meals.repository.DateTagRepository;
import com.meals.repository.MealTypeRepository;
import com.meals.repository.RecipeAssignmentRepository;
import com.meals.repository.RecipeHistoryRepository;
import com.meals.repository.RecipeMealTypeRepository;
import com.meals.repository.RecipeRepository;
import com.meals.repository.TagRepository;
import com.meals.util.DateUtil;

@RestController
@RequestMapping(value = "/rest/recipeAssignments")
public class RecipeAssignmentRestController extends BaseRestController {
	@Autowired
	private RecipeAssignmentRepository recipeAssignmentRepository;
	@Autowired
	private DateTagRepository dateTagRepository;
	@Autowired
	private RecipeMealTypeRepository recipeMealTypeRepository;
	@Autowired
	private RecipeHistoryRepository recipeHistoryRepository;
	@Autowired
	private MealTypeRepository mealTypeRepository;
	@Autowired
	private RecipeRepository recipeRepository;
	@Autowired
	private TagRepository tagRepository;

	@RequestMapping(method = RequestMethod.GET)
	Collection<RecipeAssignment> all() {
		return this.recipeAssignmentRepository.findAll();
	}

	@RequestMapping(value = "/betweenDates", method = RequestMethod.GET)
	public String recipesBetweenDates(@RequestParam(value = "start") String start, @RequestParam(value = "end") String end) throws ParseException {
		List<Event> events = new ArrayList<Event>();
		Date startDate = AppConstants.CALENDAR_DATE_FORMAT.parse(start);
		Date endDate = AppConstants.CALENDAR_DATE_FORMAT.parse(end);
		List<RecipeAssignment> recipeAssignments = this.recipeAssignmentRepository.findByDateBetweenOrderByDateAsc(startDate, endDate);
		for (RecipeAssignment recipeAssignment : recipeAssignments) {
			Event event = new Event();
			event.setTitle(recipeAssignment.getRecipeMealType().getMealType().getName() + ": " + recipeAssignment.getRecipeMealType().getRecipe().getName());
			event.setAllDay(true);
			event.setStart(recipeAssignment.getCalendarDate());
			event.setOrder(recipeAssignment.getRecipeMealType().getMealType().getOrdering());
			event.setRecipeAssignmentId(recipeAssignment.getId());
			event.setRecipeId(recipeAssignment.getRecipeMealType().getRecipe().getId());
			event.setHasIngredients(recipeAssignment.getRecipeMealType().getRecipe().getRecipeIngredients().size() > 0);
			events.add(event);
		}
		return new Gson().toJson(events);
	}

	@RequestMapping(value = "/{month}/generate", method = RequestMethod.GET)
	public void generateRecipeAssignments(@PathVariable(value = "month") int month) {
		List<RecipeMealType> lunches = this.recipeMealTypeRepository.findByMealTypeName(AppConstants.MEAL_TYPE_LUNCH);
		Map<Long, List<RecipeMealType>> tagToLunchRecipes = constructTagToRecipes(lunches);
		List<RecipeMealType> dinners = this.recipeMealTypeRepository.findByMealTypeName(AppConstants.MEAL_TYPE_DINNER);
		Map<Long, List<RecipeMealType>> tagToDinnerRecipes = constructTagToRecipes(dinners);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month - 1);
		int minDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, minDay);
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		int maxDay = endDate.getActualMaximum(Calendar.DAY_OF_MONTH);
		endDate.set(Calendar.DAY_OF_MONTH, maxDay);

		this.recipeAssignmentRepository.deleteByDateBetween(DateUtil.findDayStart(cal.getTime()), DateUtil.findDayEnd(endDate.getTime()));
		this.recipeHistoryRepository.deleteByDateBetween(DateUtil.findDayStart(cal.getTime()), DateUtil.findDayEnd(endDate.getTime()));

		List<DateTag> monthDateTags = this.dateTagRepository.findByDateBetweenOrderByDateAsc(DateUtil.findDayStart(cal.getTime()), DateUtil.findDayEnd(endDate.getTime()));
		Map<Integer, List<Long>> lunchDayTag = new HashMap<Integer, List<Long>>();
		Map<Integer, List<Long>> dinnerDayTag = new HashMap<Integer, List<Long>>();

		Calendar tagDay = Calendar.getInstance();
		for (DateTag monthDateTag : monthDateTags) {
			tagDay.setTime(monthDateTag.getDate());
			int day = tagDay.get(Calendar.DAY_OF_MONTH);
			if (AppConstants.MEAL_TYPE_LUNCH.equalsIgnoreCase(monthDateTag.getMealType().getName())) {
				putTag(lunchDayTag, monthDateTag, day);
			} else if (AppConstants.MEAL_TYPE_DINNER.equalsIgnoreCase(monthDateTag.getMealType().getName())) {
				putTag(dinnerDayTag, monthDateTag, day);
			}
		}
		Tag noLunchTag = this.tagRepository.findByName(AppConstants.NO_LUNCH_TAG);
		Tag noDinnerTag = this.tagRepository.findByName(AppConstants.NO_DINNER_TAG);

		for (int i = 0; i < maxDay; i++) {
			cal.set(Calendar.DAY_OF_MONTH, i + 1);
			RecipeAssignment lunchRecipeAssignment = new RecipeAssignment();
			lunchRecipeAssignment.setDate(cal.getTime());

			RecipeAssignment dinnerRecipeAssignment = new RecipeAssignment();
			dinnerRecipeAssignment.setDate(cal.getTime());

			assignRecipe(lunchRecipeAssignment, findDayRecipeAssignment(lunches, tagToLunchRecipes, cal, lunchDayTag, lunchRecipeAssignment, noLunchTag == null ? -100 : noLunchTag.getId()));
			assignRecipe(dinnerRecipeAssignment, findDayRecipeAssignment(dinners, tagToDinnerRecipes, cal, dinnerDayTag, dinnerRecipeAssignment, noDinnerTag == null ? -101 : noDinnerTag.getId()));
		}
	}

	private void assignRecipe(RecipeAssignment recipeAssignment, RecipeMealType recipe) {
		if (recipe != null) {
			recipeAssignment.setRecipeMealType(recipe);
			saveRecipeHistory(recipeAssignment);
			this.recipeAssignmentRepository.save(recipeAssignment);
		}
	}

	@RequestMapping(value = "/generate/{date}/{mealType}", method = RequestMethod.GET)
	public List<RecipeDTO> findRecipeAssignmentCandidates(@PathVariable(value = "date") String date, @PathVariable(value = "mealType") String mealTypeId) throws ParseException {

		MealType mealType = this.mealTypeRepository.findOne(Long.parseLong(mealTypeId));

		List<RecipeMealType> recipes = this.recipeMealTypeRepository.findByMealTypeName(mealType.getName());
		Map<Long, List<RecipeMealType>> tagToRecipes = constructTagToRecipes(recipes);

		Date dateToAssign = AppConstants.CALENDAR_DATE_FORMAT.parse(date);

		List<DateTag> dateTags = this.dateTagRepository.findByDateAndMealType(dateToAssign, mealType);
		Map<Integer, List<Long>> dayTag = new HashMap<Integer, List<Long>>();

		Calendar tagDay = Calendar.getInstance();
		for (DateTag dateTag : dateTags) {
			tagDay.setTime(dateTag.getDate());
			int day = tagDay.get(Calendar.DAY_OF_MONTH);
			putTag(dayTag, dateTag, day);
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(dateToAssign);
		RecipeAssignment recipeAssignment = new RecipeAssignment();
		recipeAssignment.setDate(cal.getTime());

		Tag noRecipeTag = null;
		if (AppConstants.MEAL_TYPE_LUNCH.equals(mealType)) {
			noRecipeTag = this.tagRepository.findByName(AppConstants.NO_LUNCH_TAG);
		} else if (AppConstants.MEAL_TYPE_DINNER.equals(mealType)) {
			noRecipeTag = this.tagRepository.findByName(AppConstants.NO_DINNER_TAG);
		}

		List<RecipeMealType> recipeMealTypes = findMatchingRecipes(recipes, tagToRecipes, cal, dayTag, recipeAssignment, true, noRecipeTag == null ? -100 : noRecipeTag.getId());
		recipeMealTypes = findQualifyingRecipes(recipeMealTypes, cal);
		List<RecipeDTO> recipeCandidates = new ArrayList<RecipeDTO>();
		CollectionUtils.collect(recipeMealTypes, new Transformer() {
			@Override
			public Object transform(Object o) {
				Recipe recipe = ((RecipeMealType) o).getRecipe();
				return new RecipeDTO(recipe.getId(), recipe.getName());
			}

		}, recipeCandidates);
		Collections.sort(recipeCandidates);
		return recipeCandidates;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	void addRecipeAssignment(@RequestParam(value = "recipeDate") String date, @RequestParam(value = "recipe") String recipeFromTagId, @RequestParam(value = "recipeFromAll") String recipeFromAllId,
			@RequestParam(value = "recipeMealType") String mealTypeId) throws ParseException {

		String recipeId = (StringUtils.isBlank(recipeFromTagId) || recipeFromTagId.equals("-1")) ? recipeFromAllId : recipeFromTagId;

		RecipeAssignment recipeAssignment = new RecipeAssignment();
		recipeAssignment.setDate(AppConstants.CALENDAR_DATE_FORMAT.parse(date));
		Recipe recipe = this.recipeRepository.findOne(Long.parseLong(recipeId));
		MealType mealType = this.mealTypeRepository.findOne(Long.parseLong(mealTypeId));
		RecipeMealType recipeMealType = this.recipeMealTypeRepository.findByRecipeAndMealType(recipe, mealType);
		recipeAssignment.setRecipeMealType(recipeMealType);

		RecipeAssignment savedRecipeAssignment = this.recipeAssignmentRepository.save(recipeAssignment);
		saveRecipeHistory(savedRecipeAssignment);
	}

	@RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
	public void updateRecipeAssignment(@PathVariable(value = "id") Long id, @RequestParam(value = "oldDate") String oldDate, @RequestParam(value = "date") String date) throws ParseException {
		RecipeAssignment recipeAssignment = this.recipeAssignmentRepository.findOne(id);
		Date formattedOldDate = AppConstants.CALENDAR_DATE_FORMAT.parse(oldDate);
		Date startDate = DateUtil.findDayStart(formattedOldDate);
		Date endDate = DateUtil.findDayEnd(formattedOldDate);
		this.recipeHistoryRepository.deleteByRecipeAndDateBetween(recipeAssignment.getRecipeMealType().getRecipe().getId(), startDate, endDate);
		recipeAssignment.setDate(AppConstants.CALENDAR_DATE_FORMAT.parse(date));
		this.recipeAssignmentRepository.save(recipeAssignment);
		saveRecipeHistory(recipeAssignment);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteRecipeAssignments(@PathVariable(value = "id") Long id) throws ParseException {
		RecipeAssignment recipeAssignment = recipeAssignmentRepository.findOne(id);
		if (recipeAssignment != null) {
			Date startDate = DateUtil.findDayStart(recipeAssignment.getDate());
			Date endDate = DateUtil.findDayEnd(recipeAssignment.getDate());
			this.recipeHistoryRepository.deleteByRecipeAndDateBetween(recipeAssignment.getRecipeMealType().getRecipe().getId(), startDate, endDate);
			this.recipeAssignmentRepository.delete(id);
		}
	}

	private RecipeMealType findDayRecipeAssignment(List<RecipeMealType> possibleMealTypes, Map<Long, List<RecipeMealType>> tagToMealTypeRecipes, Calendar day, Map<Integer, List<Long>> dayTag, RecipeAssignment recipeAssignment, Long noRecipeTagId) {
		return assignRandomRecipe(findMatchingRecipes(possibleMealTypes, tagToMealTypeRecipes, day, dayTag, recipeAssignment, false, noRecipeTagId), day);
	}

	private List<RecipeMealType> findMatchingRecipes(List<RecipeMealType> possibleMealTypes, Map<Long, List<RecipeMealType>> tagToMealTypeRecipes, Calendar day, Map<Integer, List<Long>> dayTag, RecipeAssignment recipeAssignment,
			boolean onlyTagMatches, Long noRecipeTagId) {
		List<Long> dayTagIds = dayTag.get(day.get(Calendar.DAY_OF_MONTH));
		// if the day doesn't have any tags, assign any recipe
		if (CollectionUtils.isEmpty(dayTagIds)) {
			return possibleMealTypes;
		} else { // otherwise, assign a recipe with those tags
			List<RecipeMealType> mealTypeRecipesThatMatchAllDayTags = new ArrayList<RecipeMealType>();
			for (Long dayTagId : dayTagIds) {
				// no recipe for this day and meal type
				if (dayTagId.equals(noRecipeTagId)) {
					return new ArrayList<RecipeMealType>();
				}
				List<RecipeMealType> mealTypeRecipesThatMatchDayTag = tagToMealTypeRecipes.get(dayTagId);

				// found results for tag, so merge with previous tags list
				if (!CollectionUtils.isEmpty(mealTypeRecipesThatMatchDayTag)) {
					if (CollectionUtils.isEmpty(mealTypeRecipesThatMatchAllDayTags)) {
						mealTypeRecipesThatMatchAllDayTags.addAll(mealTypeRecipesThatMatchDayTag);
					} else {
						mealTypeRecipesThatMatchAllDayTags.retainAll(mealTypeRecipesThatMatchDayTag);
					}
				}
			}
			// unable to find a meal with all matching day tags, so assigning any recipe, if allowed
			if (CollectionUtils.isEmpty(mealTypeRecipesThatMatchAllDayTags) && !onlyTagMatches) {
				return possibleMealTypes;
			} else {
				return mealTypeRecipesThatMatchAllDayTags;
			}
		}
	}

	private void saveRecipeHistory(RecipeAssignment recipeAssignment) {
		RecipeHistory recipeHistory = new RecipeHistory();
		recipeHistory.setDate(recipeAssignment.getDate());
		recipeHistory.setRecipe(recipeAssignment.getRecipeMealType().getRecipe());
		this.recipeHistoryRepository.save(recipeHistory);
	}

	private RecipeMealType assignRandomRecipe(List<RecipeMealType> mealTypeRecipes, Calendar assignDate) {
		Collections.shuffle(mealTypeRecipes);
		for (RecipeMealType potentialRecipeMealType : mealTypeRecipes) {
			Calendar startDate = Calendar.getInstance();
			Calendar endDate = Calendar.getInstance();
			startDate.setTime(assignDate.getTime());
			endDate.setTime(assignDate.getTime());
			int weight = (potentialRecipeMealType.getRecipe().getWeight() == 0 ? AppConstants.MEAL_WEIGHT_DEFAULT : potentialRecipeMealType.getRecipe().getWeight());
			startDate.add(Calendar.DAY_OF_MONTH, AppConstants.MEAL_SPACING_STEP_DAYS * weight * -1);
			endDate.add(Calendar.DAY_OF_MONTH, AppConstants.MEAL_SPACING_STEP_DAYS * weight);
			List<RecipeHistory> recentRecipeHistories = this.recipeHistoryRepository.findByRecipeAndDateBetween(potentialRecipeMealType.getRecipe(), startDate.getTime(), endDate.getTime());
			// This recipe hasn't been used X days in the past or future, so we're good
			if (CollectionUtils.isEmpty(recentRecipeHistories)) {
				return potentialRecipeMealType;
			}
		}
		return null;
	}

	private List<RecipeMealType> findQualifyingRecipes(List<RecipeMealType> mealTypeRecipes, Calendar assignDate) {
		List<RecipeMealType> qualifyingRecipes = new ArrayList<RecipeMealType>();
		for (RecipeMealType potentialRecipeMealType : mealTypeRecipes) {
			Calendar startDate = Calendar.getInstance();
			Calendar endDate = Calendar.getInstance();
			startDate.setTime(assignDate.getTime());
			endDate.setTime(assignDate.getTime());
			int weight = (potentialRecipeMealType.getRecipe().getWeight() == 0 ? AppConstants.MEAL_WEIGHT_DEFAULT : potentialRecipeMealType.getRecipe().getWeight());
			startDate.add(Calendar.DAY_OF_MONTH, AppConstants.MEAL_SPACING_STEP_DAYS * weight * -1);
			endDate.add(Calendar.DAY_OF_MONTH, AppConstants.MEAL_SPACING_STEP_DAYS * weight);
			List<RecipeHistory> recentRecipeHistories = this.recipeHistoryRepository.findByRecipeAndDateBetween(potentialRecipeMealType.getRecipe(), startDate.getTime(), endDate.getTime());
			// This recipe hasn't been used X days in the past or future, so we're good
			if (CollectionUtils.isEmpty(recentRecipeHistories)) {
				qualifyingRecipes.add(potentialRecipeMealType);
			}
		}
		return qualifyingRecipes;
	}

	private void putTag(Map<Integer, List<Long>> dayTag, DateTag dateTag, int day) {
		List<Long> dayTags = dayTag.get(day);
		if (CollectionUtils.isEmpty(dayTags)) {
			dayTags = new ArrayList<Long>();
		}
		dayTags.add(dateTag.getTag().getId());
		dayTag.put(day, dayTags);
	}

	private Map<Long, List<RecipeMealType>> constructTagToRecipes(List<RecipeMealType> mealTypeRecipes) {
		Map<Long, List<RecipeMealType>> tagToRecipes = new HashMap<Long, List<RecipeMealType>>();
		for (RecipeMealType mealTypeRecipe : mealTypeRecipes) {
			List<RecipeTag> recipeTags = mealTypeRecipe.getRecipe().getRecipeTags();
			for (RecipeTag recipeTag : recipeTags) {
				Long tagId = recipeTag.getTag().getId();
				List<RecipeMealType> tagIdRecipes = tagToRecipes.get(tagId);
				if (CollectionUtils.isEmpty(tagIdRecipes)) {
					tagIdRecipes = new ArrayList<RecipeMealType>();
				}
				tagIdRecipes.add(mealTypeRecipe);
				tagToRecipes.put(tagId, tagIdRecipes);
			}
		}
		return tagToRecipes;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public RecipeAssignment save(@RequestBody RecipeAssignment recipeAssignment, final HttpServletResponse response) {
		return this.recipeAssignmentRepository.save(recipeAssignment);
	}
}