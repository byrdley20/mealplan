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
@Table(name = "RECIPE_TAG")
public class RecipeTag extends BaseDomain {
	@Id
	@GeneratedValue
	@Expose
	private Long id;

	@ManyToOne(targetEntity = Recipe.class, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "RECIPE_ID")
	@Expose
	private Recipe recipe;

	@ManyToOne(targetEntity = Tag.class, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "TAG_ID")
	@Expose
	private Tag tag;

	public Long getId() {
		return id;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
}
