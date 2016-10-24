package com.meals.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;
import com.meals.common.AppConstants;

@Entity
@Table(name = "RECIPE")
public class Recipe extends BaseDomain {
	@Id
	@GeneratedValue
	@Expose
	private Long id;
	@Expose
	private String name;
	@Expose
	private boolean leftovers;
	@Expose
	private String description;
	@Expose
	private int weight = AppConstants.MEAL_WEIGHT_DEFAULT;

	@OneToMany(mappedBy = "recipe")
	@Expose
	private List<RecipeTag> recipeTags;

	@OneToMany(mappedBy = "recipe")
	@Expose
	private List<RecipeIngredient> recipeIngredients;

	@Transient
	@Expose
	private String tagIds;


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

	public Long getId() {
		return id;
	}

	public List<RecipeTag> getRecipeTags() {
		return recipeTags;
	}

	public void setRecipeTags(List<RecipeTag> recipeTags) {
		this.recipeTags = recipeTags;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getTagIds() {
		return tagIds;
	}

	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
	}

	public List<RecipeIngredient> getRecipeIngredients() {
		return recipeIngredients;
	}

	public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
		this.recipeIngredients = recipeIngredients;
	}
}
