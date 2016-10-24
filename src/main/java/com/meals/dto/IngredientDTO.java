package com.meals.dto;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.annotations.Expose;

public class IngredientDTO implements Comparable<IngredientDTO> {
	@Expose
	private Long id;
	@Expose
	private String name;
	@Expose
	private String description;
	@Expose
	private IngredientTypeDTO ingredientType;
	@Expose
	private String amount;

	public IngredientDTO() {
	}

	public IngredientDTO(String id) {
		if (StringUtils.isBlank(id)) {
			new IngredientDTO();
		} else {
			this.id = Long.parseLong(id);
		}
	}

	public IngredientDTO(Long id, String name, String description, IngredientTypeDTO ingredientType) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.ingredientType = ingredientType;
	}

	@Override
	public int compareTo(IngredientDTO comparingIngredient) {
		return this.name.compareTo(comparingIngredient.getName());
	}

	public String getLabel() {
		return getName();
	}

	public String getValue() {
		return getId().toString();
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public IngredientTypeDTO getIngredientType() {
		return ingredientType;
	}

	public void setIngredientType(IngredientTypeDTO ingredientType) {
		this.ingredientType = ingredientType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
