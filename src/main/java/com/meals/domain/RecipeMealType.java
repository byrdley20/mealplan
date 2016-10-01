package com.meals.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "RECIPE_MEAL_TYPE")
public class RecipeMealType extends BaseDomain {
	@Id
	@GeneratedValue
	@Expose
	private Long id;

	@ManyToOne(targetEntity = Recipe.class, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "RECIPE_ID")
	@Expose
	private Recipe recipe;

	@ManyToOne(targetEntity = MealType.class, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "MEAL_TYPE_ID")
	@Expose
	private MealType mealType;

	public Long getId() {
		return id;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public MealType getMealType() {
		return mealType;
	}

	public void setMealType(MealType mealType) {
		this.mealType = mealType;
	}
}
