package com.meals.repository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.meals.domain.MealType;
import com.meals.domain.Recipe;
import com.meals.domain.RecipeMealType;

public interface RecipeMealTypeRepository extends BaseRepository<RecipeMealType> {
	List<RecipeMealType> findByMealTypeName(String mealTypeName);

	RecipeMealType findByRecipeAndMealType(Recipe recipe, MealType mealType);

	@Transactional
	void deleteByRecipe(Recipe recipe);
}
