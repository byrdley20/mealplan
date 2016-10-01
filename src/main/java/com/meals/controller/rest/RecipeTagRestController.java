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

import com.meals.domain.RecipeTag;
import com.meals.repository.RecipeTagRepository;

@RestController
@RequestMapping(value = "/rest/recipeTags")
public class RecipeTagRestController extends BaseRestController {
	@Autowired
	RecipeTagRepository recipeTagRepository;

	@RequestMapping(method = RequestMethod.GET)
	Collection<RecipeTag> all() {
		return this.recipeTagRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	RecipeTag save(@RequestBody RecipeTag recipeTag, final HttpServletResponse response) {
		return this.recipeTagRepository.save(recipeTag);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	void delete(@PathVariable Long id) {
		this.recipeTagRepository.delete(id);
	}
}
