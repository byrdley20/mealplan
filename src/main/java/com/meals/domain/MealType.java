package com.meals.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;
import com.meals.common.AppConstants;

@Entity
@Table(name = "MEAL_TYPE")
public class MealType extends BaseDomain {
	@Id
	@GeneratedValue
	@Expose
	private Long id;
	@Expose
	private String name;
	@Expose
	private int ordering;

	@Transient
	public boolean isLunch() {
		return AppConstants.MEAL_TYPE_LUNCH.equals(name);
	}

	@Transient
	public boolean isDinner() {
		return AppConstants.MEAL_TYPE_DINNER.equals(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
}
