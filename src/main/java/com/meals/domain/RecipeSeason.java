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
@Table(name = "RECIPE_SEASON")
public class RecipeSeason extends BaseDomain {
	@Id
	@GeneratedValue
	@Expose
	private Long id;

	@ManyToOne(targetEntity = Recipe.class, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "RECIPE_ID")
	@Expose
	private Recipe recipe;

	@ManyToOne(targetEntity = Season.class, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "SEASON_ID")
	@Expose
	private Season season;

	public Long getId() {
		return id;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}

}
