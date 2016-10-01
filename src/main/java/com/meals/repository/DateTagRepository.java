package com.meals.repository;

import java.util.Date;
import java.util.List;

import com.meals.domain.DateTag;
import com.meals.domain.MealType;

public interface DateTagRepository extends BaseRepository<DateTag> {

	List<DateTag> findByDateBetweenOrderByDateAsc(Date startDate, Date endDate);

	List<DateTag> findByDateAndMealType(Date date, MealType mealType);
}
