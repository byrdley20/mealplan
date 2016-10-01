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

@Entity
@Table(name = "DATE_TAG")
public class DateTag extends BaseDomain {
	@Id
	@GeneratedValue
	@Expose
	private Long id;

	@Expose
	private Date date;

	@ManyToOne(targetEntity = MealType.class, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "MEAL_TYPE_ID")
	@Expose
	private MealType mealType;

	@ManyToOne(targetEntity = Tag.class, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "TAG_ID")
	@Expose
	private Tag tag;

	public Long getId() {
		return id;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public MealType getMealType() {
		return mealType;
	}

	public void setMealType(MealType mealType) {
		this.mealType = mealType;
	}
}
