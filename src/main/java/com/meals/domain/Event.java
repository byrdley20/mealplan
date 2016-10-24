package com.meals.domain;

import com.google.gson.annotations.Expose;

public class Event {
	@Expose
	private String title;
	@Expose
	private boolean allDay;
	@Expose
	private String start;
	@Expose
	private int order;
	@Expose
	private long recipeAssignmentId;
	@Expose
	private long recipeId;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public long getRecipeAssignmentId() {
		return recipeAssignmentId;
	}

	public void setRecipeAssignmentId(long recipeAssignmentId) {
		this.recipeAssignmentId = recipeAssignmentId;
	}

	public long getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(long recipeId) {
		this.recipeId = recipeId;
	}
}
