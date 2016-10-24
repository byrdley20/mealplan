package com.meals.controller.rest;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.meals.domain.RecipeIngredient;
import com.meals.dto.IngredientDTO;
import com.meals.repository.RecipeIngredientRepository;

@RestController
@RequestMapping(value = "/rest/recipeIngredients")
public class RecipeIngredientRestController extends BaseRestController {
	@Autowired
	RecipeIngredientRepository recipeIngredientRepository;

	@RequestMapping(method = RequestMethod.GET)
	Collection<RecipeIngredient> all() {
		return this.recipeIngredientRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	RecipeIngredient save(@RequestBody RecipeIngredient recipeIngredient, final HttpServletResponse response) {
		return this.recipeIngredientRepository.save(recipeIngredient);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	void delete(@PathVariable Long id) {
		this.recipeIngredientRepository.delete(id);
	}

	@RequestMapping(value = "/{recipeId}", method = RequestMethod.GET)
	@ResponseBody
	List<IngredientDTO> getIngredients(@PathVariable Long recipeId) {
		return convertToIngredientDTOs(recipeIngredientRepository.findByRecipeId(recipeId));
	}
}
