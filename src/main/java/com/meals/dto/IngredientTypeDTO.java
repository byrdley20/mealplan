package com.meals.dto;

import com.google.gson.annotations.Expose;

public class IngredientTypeDTO implements Comparable<IngredientTypeDTO> {
	@Expose
	private Long id;
	@Expose
	private String name;

	public IngredientTypeDTO() {
	}

	public IngredientTypeDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public int compareTo(IngredientTypeDTO comparingIngredient) {
		return this.name.compareTo(comparingIngredient.getName());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}