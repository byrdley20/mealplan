package com.meals.common;

import java.text.SimpleDateFormat;

public class AppConstants {

	public static final SimpleDateFormat CALENDAR_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final String MEAL_TYPE_LUNCH = "Lunch";
	public static final String MEAL_TYPE_DINNER = "Dinner";

	public static int MEAL_SPACING_STEP_DAYS = 7;
	public static int MEAL_WEIGHT_DEFAULT = 5;

	public static final String TAG_KEY_DELIMITER = "^";
	public static final String TAG_LIST_DELIMITER = "|";

	public static final String NO_LUNCH_TAG = "No Lunch";
	public static final String NO_DINNER_TAG = "No Dinner";
}
