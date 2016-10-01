package com.meals.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.meals.domain.Recipe;
import com.meals.domain.RecipeHistory;

public interface RecipeHistoryRepository extends BaseRepository<RecipeHistory> {

	List<RecipeHistory> findByRecipeAndDateBetween(Recipe recipe, Date startDate, Date endDate);

	void deleteByDate(Date date);

	@Modifying
	@Transactional
	@Query("DELETE FROM RecipeHistory rh WHERE rh.date between :startDate and :endDate")
	void deleteByDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Modifying
	@Transactional
	@Query("DELETE FROM RecipeHistory rh WHERE rh.recipe.id=:recipeId and rh.date between :startDate and :endDate")
	void deleteByRecipeAndDateBetween(@Param("recipeId") Long recipeId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
