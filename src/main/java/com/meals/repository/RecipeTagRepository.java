package com.meals.repository;

import org.springframework.transaction.annotation.Transactional;

import com.meals.domain.Recipe;
import com.meals.domain.RecipeTag;

public interface RecipeTagRepository extends BaseRepository<RecipeTag> {

	@Transactional
	void deleteByRecipe(Recipe recipe);
}
