package com.meals.controller.rest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.meals.common.AppConstants;
import com.meals.domain.DateTag;
import com.meals.domain.Event;
import com.meals.domain.MealType;
import com.meals.domain.Tag;
import com.meals.dto.TagDTO;
import com.meals.repository.DateTagRepository;
import com.meals.repository.MealTypeRepository;
import com.meals.repository.TagRepository;
import com.meals.util.DateUtil;

@RestController
@RequestMapping(value = "/rest/dateTags")
public class DateTagRestController extends BaseRestController {
	@Autowired
	private DateTagRepository dateTagRepository;
	@Autowired
	private TagRepository tagRepository;
	@Autowired
	private MealTypeRepository mealTypeRepository;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	DateTag addDateTag(@RequestParam(value = "tagDate") String date, @RequestParam(value = "tagName", required = false) String tagName, @RequestParam(value = "tagDescription", required = false) String tagDescription,
			@RequestParam(value = "tagMealType") String mealTypeId, @RequestParam(value = "existingTag", required = false) String existingTagId) throws ParseException {

		Tag tag = null;
		if (StringUtils.isBlank(existingTagId) || existingTagId.equals("-1")) {
			tag = new Tag();
			tag.setName(tagName);
			tag.setDescription(tagDescription);
			tag = this.tagRepository.save(tag);
		} else {
			tag = this.tagRepository.findOne(Long.parseLong(existingTagId));
		}

		MealType mealTypeForName = this.mealTypeRepository.findOne(Long.parseLong(mealTypeId));

		DateTag dateTag = new DateTag();
		dateTag.setDate(AppConstants.CALENDAR_DATE_FORMAT.parse(date));
		dateTag.setMealType(mealTypeForName);
		dateTag.setTag(tag);

		return this.dateTagRepository.save(dateTag);
	}

	@RequestMapping(value = "/month/{month}", method = RequestMethod.GET)
	public Map<String, Map<String, List<TagDTO>>> dateTagsForMonth(@PathVariable(value = "month") int month) throws ParseException {
		List<Event> events = new ArrayList<Event>();
		Calendar startCal = DateUtil.findMonthStartDate(month, null);
		startCal.add(Calendar.DAY_OF_MONTH, -7); // go back 7 days before the month starts, since some of the previous month's days show
		Calendar endCal = DateUtil.findMonthEndDate(month, null);
		endCal.add(Calendar.DAY_OF_MONTH, 7); // go forward 7 days after the month ends, since some of the next month's days show

		List<DateTag> monthDateTags = dateTagRepository.findByDateBetweenOrderByDateAsc(startCal.getTime(), endCal.getTime());
		Map<String, Map<String, List<TagDTO>>> dateToMealTypeToTags = new HashMap<String, Map<String, List<TagDTO>>>();
		for (DateTag monthDateTag : monthDateTags) {
			Date date = monthDateTag.getDate();
			Tag tag = monthDateTag.getTag();
			MealType mealType = monthDateTag.getMealType();

			TagDTO tagDto = new TagDTO();
			tagDto.setId(tag.getId());
			tagDto.setName(tag.getName());
			tagDto.setDescription(tag.getDescription());
			tagDto.setDateTagId(monthDateTag.getId());

			String formattedDate = AppConstants.CALENDAR_DATE_FORMAT.format(date);
			Map<String, List<TagDTO>> mealTypeToTagsForDate = dateToMealTypeToTags.get(formattedDate);
			if (mealTypeToTagsForDate == null) {
				mealTypeToTagsForDate = new HashMap<String, List<TagDTO>>();
			}
			List<TagDTO> tagsForDateAndMealType = mealTypeToTagsForDate.get(mealType.getName());
			if (tagsForDateAndMealType == null) {
				tagsForDateAndMealType = new ArrayList<TagDTO>();
			}
			tagsForDateAndMealType.add(tagDto);
			mealTypeToTagsForDate.put(mealType.getName(), tagsForDateAndMealType);
			dateToMealTypeToTags.put(formattedDate, mealTypeToTagsForDate);
		}
		return dateToMealTypeToTags;
	}

	@RequestMapping(method = RequestMethod.GET)
	Collection<DateTag> all() {
		return this.dateTagRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	DateTag save(@RequestBody DateTag dateTag, final HttpServletResponse response) {
		return this.dateTagRepository.save(dateTag);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	void delete(@PathVariable Long id) {
		this.dateTagRepository.delete(id);
	}
}
