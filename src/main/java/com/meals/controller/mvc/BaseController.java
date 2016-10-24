package com.meals.controller.mvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.data.domain.Sort;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meals.common.AppConstants;
import com.meals.domain.BaseDomain;
import com.meals.domain.Ingredient;
import com.meals.domain.Recipe;
import com.meals.domain.RecipeIngredient;
import com.meals.domain.RecipeMealType;
import com.meals.domain.RecipeTag;
import com.meals.domain.Tag;
import com.meals.dto.IngredientDTO;
import com.meals.dto.IngredientTypeDTO;
import com.meals.dto.RecipeDTO;
import com.meals.dto.TagDTO;

public class BaseController {

	public String toJson(List<? extends BaseDomain> domainList) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(domainList);
	}

	protected List<RecipeDTO> convertToRecipeDTOs(List<RecipeMealType> recipeMealTypes) {
		List<RecipeDTO> recipeDTOs = new ArrayList<RecipeDTO>();
		CollectionUtils.collect(recipeMealTypes, new Transformer() {
			@Override
			public Object transform(Object o) {
				Recipe recipe = ((RecipeMealType) o).getRecipe();
				return new RecipeDTO(recipe.getId(), recipe.getName());
			}

		}, recipeDTOs);
		Collections.sort(recipeDTOs);
		return recipeDTOs;
	}

	protected List<RecipeDTO> convertToRecipeDTOsWithTags(List<RecipeMealType> recipeMealTypes) {
		List<RecipeDTO> recipeDTOs = new ArrayList<RecipeDTO>();
		CollectionUtils.collect(recipeMealTypes, new Transformer() {
			@Override
			public Object transform(Object o) {
				RecipeMealType recipeMealType = (RecipeMealType) o;
				Recipe recipe = recipeMealType.getRecipe();
				boolean lunch = AppConstants.MEAL_TYPE_LUNCH.equals(recipeMealType.getMealType().getName());
				boolean dinner = AppConstants.MEAL_TYPE_DINNER.equals(recipeMealType.getMealType().getName());

				List<TagDTO> tags = new ArrayList<TagDTO>();

				List<RecipeTag> recipeTags = recipe.getRecipeTags();
				if (recipeTags != null) {
					for (RecipeTag recipeTag : recipeTags) {
						TagDTO tag = new TagDTO();
						tag.setId(recipeTag.getTag().getId());
						tag.setName(recipeTag.getTag().getName());
						tag.setDescription(recipeTag.getTag().getDescription());
						tags.add(tag);
					}
				}

				List<IngredientDTO> ingredients = new ArrayList<IngredientDTO>();

				List<RecipeIngredient> recipeIngredients = recipe.getRecipeIngredients();
				if (recipeIngredients != null) {
					for (RecipeIngredient recipeIngredient : recipeIngredients) {
						IngredientTypeDTO ingredientType = new IngredientTypeDTO();
						ingredientType.setId(recipeIngredient.getIngredient().getIngredientType().getId());
						ingredientType.setName(recipeIngredient.getIngredient().getIngredientType().getName());

						IngredientDTO ingredient = new IngredientDTO();
						ingredient.setId(recipeIngredient.getIngredient().getId());
						ingredient.setDescription(recipeIngredient.getIngredient().getDescription());
						ingredient.setName(recipeIngredient.getIngredient().getName());
						ingredient.setIngredientType(ingredientType);
						ingredient.setAmount(recipeIngredient.getAmount());
						ingredients.add(ingredient);
					}
				}

				return new RecipeDTO(recipe.getId(), recipe.getName(), recipe.isLeftovers(), recipe.getDescription(), recipe.getWeight(), lunch, dinner, tags, ingredients);
			}

		}, recipeDTOs);
		Collections.sort(recipeDTOs);
		return recipeDTOs;
	}

	protected List<TagDTO> convertToTagDTOs(List<Tag> tags) {
		List<TagDTO> tagDTOs = new ArrayList<TagDTO>();
		CollectionUtils.collect(tags, new Transformer() {
			@Override
			public Object transform(Object o) {
				Tag tag = (Tag) o;
				return new TagDTO(tag.getId(), tag.getName(), tag.getDescription());
			}

		}, tagDTOs);
		Collections.sort(tagDTOs);
		return tagDTOs;
	}

	protected List<IngredientDTO> convertToIngredientDTOs(List<Ingredient> ingredients) {
		List<IngredientDTO> ingredientDTOs = new ArrayList<IngredientDTO>();
		CollectionUtils.collect(ingredients, new Transformer() {
			@Override
			public Object transform(Object o) {
				Ingredient ingredient = (Ingredient) o;
				return convertToIngredientDTO(ingredient);
			}

		}, ingredientDTOs);
		Collections.sort(ingredientDTOs);
		return ingredientDTOs;
	}

	protected IngredientDTO convertToIngredientDTO(Ingredient ingredient) {
		return new IngredientDTO(ingredient.getId(), ingredient.getName(), ingredient.getDescription(), new IngredientTypeDTO(ingredient.getIngredientType().getId(), ingredient.getIngredientType().getName()));
	}

	protected Sort sortByNameAsc() {
		return new Sort(Sort.Direction.ASC, "name");
	}

	protected Sort sortByOrdering() {
		return new Sort(Sort.Direction.ASC, "ordering");
	}
}
