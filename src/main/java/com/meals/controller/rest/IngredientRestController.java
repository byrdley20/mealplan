package com.meals.controller.rest;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.meals.domain.Ingredient;
import com.meals.repository.IngredientRepository;

@RestController
@RequestMapping(value = "/rest/ingredients")
public class IngredientRestController extends BaseRestController {
	@Autowired
	IngredientRepository ingredientRepository;

	@RequestMapping(method = RequestMethod.GET)
	Collection<Ingredient> all() {
		return this.ingredientRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	Ingredient save(@RequestBody Ingredient ingredient, final HttpServletResponse response) {
		return this.ingredientRepository.save(ingredient);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	void delete(@PathVariable Long id) {
		this.ingredientRepository.delete(id);
	}
}
