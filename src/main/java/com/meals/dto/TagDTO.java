package com.meals.dto;

import com.google.gson.annotations.Expose;

public class TagDTO implements Comparable<TagDTO> {
	@Expose
	private Long id;
	@Expose
	private String name;
	@Expose
	private String description;
	@Expose
	private Long dateTagId;

	public TagDTO() {
	}

	public TagDTO(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	@Override
	public int compareTo(TagDTO comparingRecipe) {
		return this.name.compareTo(comparingRecipe.getName());
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

	public Long getDateTagId() {
		return dateTagId;
	}

	public void setDateTagId(Long dateTagId) {
		this.dateTagId = dateTagId;
	}
}
