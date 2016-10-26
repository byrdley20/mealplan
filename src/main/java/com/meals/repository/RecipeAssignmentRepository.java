package com.meals.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.meals.domain.RecipeAssignment;
import com.meals.domain.RecipeMealType;

public interface RecipeAssignmentRepository extends BaseRepository<RecipeAssignment> {

	List<RecipeAssignment> findByDateBetweenOrderByDateAsc(Date startDate, Date endDate);

	// @Query("SELECT ra FROM RecipeAssignment ra WHERE ra.date between :startDate and :endDate")
	// List<RecipeAssignment> findByDateBetweenOrderByDateAsc(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	// @Query("SELECT new com.meals.dto.RecipeAssignmentDTO(ra.id, ra.recipeMealType, ra.date, ra.recipeIngredients, ri.id) FROM RecipeAssignment ra left outer join RecipeIngredient ri on ri.recipe.id=ra.recipeMealType.recipe.id WHERE ra.date between :startDate and :endDate")
	// List<RecipeAssignmentDTO> findByDateBetweenOrderByDateAsc(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	void deleteByDate(Date date);

	@Modifying
	@Transactional
	@Query("DELETE FROM RecipeAssignment ra WHERE ra.date between :startDate and :endDate")
	void deleteByDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	void deleteByRecipeMealType(RecipeMealType recipeMealType);
}
