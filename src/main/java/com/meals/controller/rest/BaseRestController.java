package com.meals.controller.rest;

import java.util.ArrayList;
import java.util.List;

import com.meals.domain.Ingredient;
import com.meals.domain.IngredientType;
import com.meals.domain.Recipe;
import com.meals.domain.RecipeIngredient;
import com.meals.domain.RecipeTag;
import com.meals.domain.Tag;
import com.meals.dto.IngredientDTO;
import com.meals.dto.IngredientTypeDTO;
import com.meals.dto.RecipeDTO;
import com.meals.dto.TagDTO;

public class BaseRestController {

	protected RecipeDTO convertToRecipeDTOWithTagsAndIngredients(Recipe recipe, boolean lunch, boolean dinner) {
		List<TagDTO> tagDTOs = new ArrayList<TagDTO>();
		if (recipe.getRecipeTags() != null) {
			for (RecipeTag recipeTag : recipe.getRecipeTags()) {
				Tag tag = recipeTag.getTag();
				tagDTOs.add(new TagDTO(tag.getId(), tag.getName(), tag.getDescription()));
			}
		}

		List<IngredientDTO> ingredientDTOs = convertToIngredientDTOs(recipe.getRecipeIngredients());

		RecipeDTO recipeDTO = new RecipeDTO(recipe.getId(), recipe.getName(), recipe.isLeftovers(), recipe.getDescription(), recipe.getWeight(), lunch, dinner, tagDTOs, ingredientDTOs);

		return recipeDTO;
	}

	protected List<IngredientDTO> convertToIngredientDTOs(List<RecipeIngredient> recipeIngredients) {
		if (recipeIngredients == null) {
			return new ArrayList<IngredientDTO>();
		}

		List<IngredientDTO> ingredientDTOs = new ArrayList<IngredientDTO>();

		for (RecipeIngredient recipeIngredient : recipeIngredients) {
			Ingredient ingredient = recipeIngredient.getIngredient();
			IngredientType ingredientType = ingredient.getIngredientType();
			IngredientDTO ingredientDTO = new IngredientDTO(ingredient.getId(), ingredient.getName(), ingredient.getDescription(), new IngredientTypeDTO(ingredientType.getId(), ingredientType.getName()));
			ingredientDTO.setAmount(recipeIngredient.getAmount());
			ingredientDTOs.add(ingredientDTO);
		}
		return ingredientDTOs;
	}
}
