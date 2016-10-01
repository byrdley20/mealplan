package com.meals.controller.rest;

import java.util.ArrayList;
import java.util.List;

import com.meals.domain.Recipe;
import com.meals.domain.RecipeTag;
import com.meals.dto.RecipeDTO;
import com.meals.dto.TagDTO;

public class BaseRestController {

	protected RecipeDTO convertToRecipeDTOWithTags(Recipe recipe, boolean lunch, boolean dinner) {
		List<TagDTO> tagDTOs = new ArrayList<TagDTO>();
		for (RecipeTag recipeTag : recipe.getRecipeTags()) {
			tagDTOs.add(new TagDTO(recipeTag.getTag().getId(), recipeTag.getTag().getName(), recipeTag.getTag().getDescription()));
		}
		return new RecipeDTO(recipe.getId(), recipe.getName(), recipe.isLeftovers(), recipe.getDescription(), recipe.getWeight(), lunch, dinner, tagDTOs);
	}
}
