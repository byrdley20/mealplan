package com.meals.dto;

import com.google.gson.annotations.Expose;

public class ShoppingItemDTO implements Comparable<ShoppingItemDTO> {
	@Expose
	private int day;
	@Expose
	private String recipeName;
	@Expose
	private IngredientDTO ingredient;

	public ShoppingItemDTO() {
	}

	public ShoppingItemDTO(int day, String recipeName, IngredientDTO ingredient) {
		this.day = day;
		this.recipeName = recipeName;
		this.ingredient = ingredient;
	}

	@Override
	public int compareTo(ShoppingItemDTO comparingShoppingList) {
		return this.day - comparingShoppingList.day;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public IngredientDTO getIngredient() {
		return ingredient;
	}

	public void setIngredient(IngredientDTO ingredient) {
		this.ingredient = ingredient;
	}
}
