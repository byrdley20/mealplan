package com.meals.dto;

import java.util.List;

import javax.persistence.Transient;

import com.google.gson.annotations.Expose;
import com.meals.common.AppConstants;

public class RecipeDTO implements Comparable<RecipeDTO> {
	@Expose
	private Long id;
	@Expose
	private String name;
	@Expose
	private boolean leftovers;
	@Expose
	private String description;
	@Expose
	private int weight;
	@Expose
	private boolean lunch;
	@Expose
	private boolean dinner;
	@Expose
	private List<TagDTO> tags;
	@Expose
	private String tagIds;
	@Expose
	private List<IngredientDTO> ingredients;
	@Expose
	private List<String> amounts;

	public RecipeDTO() {
	}

	public RecipeDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public RecipeDTO(Long id, String name, boolean leftovers, String description, int weight, boolean lunch, boolean dinner, List<TagDTO> tags, List<IngredientDTO> ingredients) {
		this.id = id;
		this.name = name;
		this.leftovers = leftovers;
		this.description = description;
		this.weight = weight;
		this.lunch = lunch;
		this.dinner = dinner;
		this.tags = tags;
		this.ingredients = ingredients;
	}

	@Override
	public int compareTo(RecipeDTO comparingRecipe) {
		return this.name.compareTo(comparingRecipe.getName());
	}

	@Transient
	public String getTagsString() {
		StringBuilder sb = new StringBuilder();
		List<TagDTO> tags = this.getTags();
		if (tags != null) {
			for (TagDTO tag : tags) {
				sb.append(tag.getId()).append(AppConstants.TAG_KEY_DELIMITER).append(tag.getName()).append(AppConstants.TAG_LIST_DELIMITER);
			}
		}
		return sb.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isLeftovers() {
		return leftovers;
	}

	public void setLeftovers(boolean leftovers) {
		this.leftovers = leftovers;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public boolean isLunch() {
		return lunch;
	}

	public void setLunch(boolean lunch) {
		this.lunch = lunch;
	}

	public boolean isDinner() {
		return dinner;
	}

	public void setDinner(boolean dinner) {
		this.dinner = dinner;
	}

	public List<TagDTO> getTags() {
		return tags;
	}

	public void setTags(List<TagDTO> tags) {
		this.tags = tags;
	}

	public String getTagIds() {
		return tagIds;
	}

	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
	}

	public List<IngredientDTO> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<IngredientDTO> ingredients) {
		this.ingredients = ingredients;
	}

	public List<String> getAmounts() {
		return amounts;
	}

	public void setAmounts(List<String> amounts) {
		this.amounts = amounts;
	}
}
