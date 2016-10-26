package com.meals.dto;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.meals.common.AppConstants;
import com.meals.domain.RecipeIngredient;
import com.meals.domain.RecipeMealType;

public class RecipeAssignmentDTO {
	@Expose
	private Long id;
	@Expose
	private RecipeMealType recipeMealType;
	@Expose
	private Date date;
	@Expose
	private List<RecipeIngredient> recipeIngredients;

	public RecipeAssignmentDTO(Long id, RecipeMealType recipeMealType, Date date, List<RecipeIngredient> recipeIngredients) {
		this.id = id;
		this.recipeMealType = recipeMealType;
		this.date = date;
		this.recipeIngredients=recipeIngredients;
	}

	public Long getId() {
		return id;
	}

	public RecipeMealType getRecipeMealType() {
		return recipeMealType;
	}

	public void setRecipeMealType(RecipeMealType recipeMealType) {
		this.recipeMealType = recipeMealType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCalendarDate() {
		return AppConstants.CALENDAR_DATE_FORMAT.format(date);
	}
}
