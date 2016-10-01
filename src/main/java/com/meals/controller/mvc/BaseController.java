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
import com.meals.domain.Recipe;
import com.meals.domain.RecipeMealType;
import com.meals.domain.RecipeTag;
import com.meals.domain.Tag;
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

				return new RecipeDTO(recipe.getId(), recipe.getName(), recipe.isLeftovers(), recipe.getDescription(), recipe.getWeight(), lunch, dinner, tags);
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

	protected Sort sortByNameAsc() {
		return new Sort(Sort.Direction.ASC, "name");
	}

	protected Sort sortByOrdering() {
		return new Sort(Sort.Direction.ASC, "ordering");
	}
}
