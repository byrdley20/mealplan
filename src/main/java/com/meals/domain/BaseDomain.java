package com.meals.domain;

import java.text.SimpleDateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class BaseDomain implements Cloneable {

	protected static final SimpleDateFormat DISPLAYABLE_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

	public String toJson() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}
}
