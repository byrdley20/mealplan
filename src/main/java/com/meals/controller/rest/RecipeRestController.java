package com.meals.controller.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.meals.common.AppConstants;
import com.meals.domain.Ingredient;
import com.meals.domain.MealType;
import com.meals.domain.Recipe;
import com.meals.domain.RecipeIngredient;
import com.meals.domain.RecipeMealType;
import com.meals.domain.RecipeTag;
import com.meals.domain.Tag;
import com.meals.dto.IngredientDTO;
import com.meals.dto.RecipeDTO;
import com.meals.repository.IngredientRepository;
import com.meals.repository.MealTypeRepository;
import com.meals.repository.RecipeAssignmentRepository;
import com.meals.repository.RecipeIngredientRepository;
import com.meals.repository.RecipeMealTypeRepository;
import com.meals.repository.RecipeRepository;
import com.meals.repository.RecipeTagRepository;
import com.meals.repository.TagRepository;

@RestController
@RequestMapping(value = "/rest/recipes")
public class RecipeRestController extends BaseRestController {
	@Autowired
	private RecipeRepository recipeRepository;
	@Autowired
	private TagRepository tagRepository;
	@Autowired
	private RecipeTagRepository recipeTagRepository;
	@Autowired
	private MealTypeRepository mealTypeRepository;
	@Autowired
	private RecipeMealTypeRepository recipeMealTypeRepository;
	@Autowired
	private RecipeAssignmentRepository recipeAssignmentRepository;
	@Autowired
	private IngredientRepository ingredientRepository;
	@Autowired
	private RecipeIngredientRepository recipeIngredientRepository;

	@RequestMapping(method = RequestMethod.GET)
	Collection<Recipe> all() {
		return this.recipeRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	RecipeDTO save(@RequestBody RecipeDTO recipeDTO) {

		Recipe recipe;

		if (recipeDTO.getId() != null) {
			recipe = this.recipeRepository.findOne(recipeDTO.getId());
		} else {
			recipe = new Recipe();
		}
		recipe.setName(recipeDTO.getName());
		recipe.setDescription(recipeDTO.getDescription());
		recipe.setWeight(recipeDTO.getWeight() == 0 ? AppConstants.MEAL_WEIGHT_DEFAULT : recipeDTO.getWeight());
		recipe.setLeftovers(recipeDTO.isLeftovers());

		Recipe savedRecipe = this.recipeRepository.save(recipe);

		List<String> amounts = recipeDTO.getAmounts();

		this.recipeIngredientRepository.deleteByRecipe(savedRecipe);

		if (CollectionUtils.isNotEmpty(recipeDTO.getIngredients())) {
			for (int i = 0; i < recipeDTO.getIngredients().size(); i++) {
				IngredientDTO ingredientDTO = recipeDTO.getIngredients().get(i);
				if (ingredientDTO.getId() != null) {
					Ingredient ingredient = ingredientRepository.findOne(ingredientDTO.getId());
					RecipeIngredient recipeIngredient = new RecipeIngredient();
					recipeIngredient.setRecipe(savedRecipe);
					recipeIngredient.setIngredient(ingredient);
					// set amount if it exists
					if (CollectionUtils.isNotEmpty(amounts)) {
						String amount = amounts.get(i);
						if (StringUtils.isNotBlank(amount)) {
							recipeIngredient.setAmount(amount);
						}
					}
					this.recipeIngredientRepository.save(recipeIngredient);
				}
			}
		}

		updateRecipeMealType(savedRecipe, AppConstants.MEAL_TYPE_LUNCH, recipeDTO.isLunch());
		updateRecipeMealType(savedRecipe, AppConstants.MEAL_TYPE_DINNER, recipeDTO.isDinner());

		updateTags(recipeDTO, savedRecipe);
		savedRecipe.setRecipeIngredients(recipeIngredientRepository.findByRecipe(savedRecipe));
		return convertToRecipeDTOWithTagsAndIngredients(savedRecipe, recipeDTO.isLunch(), recipeDTO.isDinner());
	}

	private void updateRecipeMealType(Recipe savedRecipe, String mealTypeName, boolean isMealType) {
		MealType mealType = mealTypeRepository.findByName(mealTypeName);
		RecipeMealType foundRecipeMealType = recipeMealTypeRepository.findByRecipeAndMealType(savedRecipe, mealType);
		// if it's not this meal type and one exists, then we need to delete it
		if (!isMealType && foundRecipeMealType != null) {
			this.recipeAssignmentRepository.deleteByRecipeMealType(foundRecipeMealType);
			this.recipeMealTypeRepository.delete(foundRecipeMealType);
		}
		// if it is this meal type and one doesn't exist, then we need to create it
		else if (isMealType && foundRecipeMealType == null) {
			RecipeMealType recipeMealType = new RecipeMealType();
			recipeMealType.setMealType(mealType);
			recipeMealType.setRecipe(savedRecipe);
			recipeMealTypeRepository.save(recipeMealType);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	void delete(@PathVariable Long id) {
		Recipe recipe = recipeRepository.findOne(id);
		recipeMealTypeRepository.deleteByRecipe(recipe);
		recipeIngredientRepository.deleteByRecipe(recipe);
		recipeTagRepository.deleteByRecipe(recipe);
		this.recipeRepository.delete(id);
	}

	private void updateTags(RecipeDTO recipeDTO, Recipe savedRecipe) {
		if (!StringUtils.isEmpty(recipeDTO.getTagIds())) {
			List<Long> longIds = new ArrayList<Long>();
			for (String id : Arrays.asList(recipeDTO.getTagIds().split(","))) {
				longIds.add(Long.parseLong(id));
			}
			this.recipeTagRepository.deleteByRecipe(savedRecipe);
			List<RecipeTag> recipeTags = new ArrayList<RecipeTag>();
			for (Tag tag : this.tagRepository.findAll(longIds)) {
				RecipeTag recipeTag = new RecipeTag();
				recipeTag.setRecipe(savedRecipe);
				recipeTag.setTag(tag);
				recipeTagRepository.save(recipeTag);
				recipeTags.add(recipeTag);
			}
			savedRecipe.setRecipeTags(recipeTags);
		}
	}
}
