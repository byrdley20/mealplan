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
@Table(name = "RECIPE")
public class RecipeIngredient extends BaseDomain {
	@Id
	@GeneratedValue
	@Expose
	private Long id;

	@ManyToOne(targetEntity = Recipe.class, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "RECIPE_ID")
	@Expose
	private Recipe recipe;

	@ManyToOne(targetEntity = Ingredient.class, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "INGREDIENT_ID")
	@Expose
	private Ingredient ingredient;

	@Expose
	private String amount;

	public Long getId() {
		return id;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
