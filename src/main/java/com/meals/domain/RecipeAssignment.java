package com.meals.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;
import com.meals.common.AppConstants;

@Entity
@Table(name = "RECIPE_ASSIGNMENT")
public class RecipeAssignment extends BaseDomain {
	@Id
	@GeneratedValue
	@Expose
	private Long id;

	@ManyToOne(targetEntity = RecipeMealType.class, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "RECIPE_MEAL_TYPE_ID")
	@Expose
	private RecipeMealType recipeMealType;

	@Expose
	private Date date;

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
