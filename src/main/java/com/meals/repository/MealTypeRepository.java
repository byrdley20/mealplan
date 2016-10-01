package com.meals.repository;

import com.meals.domain.MealType;

public interface MealTypeRepository extends BaseRepository<MealType> {

	MealType findByName(String name);
}
