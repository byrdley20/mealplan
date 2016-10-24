package com.meals.repository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.meals.domain.Recipe;
import com.meals.domain.RecipeIngredient;

public interface RecipeIngredientRepository extends BaseRepository<RecipeIngredient> {

	List<RecipeIngredient> findByRecipe(Recipe recipe);

	List<RecipeIngredient> findByRecipeId(Long recipeId);
	
	List<RecipeIngredient> findByRecipeIdIn(List<Long> recipeIds);

	@Transactional
	void deleteByRecipe(Recipe recipe);
}
