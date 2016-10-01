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

import com.meals.domain.RecipeSeason;
import com.meals.repository.RecipeSeasonRepository;

@RestController
@RequestMapping(value = "/rest/recipeSeasons")
public class RecipeSeasonRestController extends BaseRestController {
	@Autowired
	RecipeSeasonRepository recipeSeasonRepository;

	@RequestMapping(method = RequestMethod.GET)
	Collection<RecipeSeason> all() {
		return this.recipeSeasonRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	RecipeSeason save(@RequestBody RecipeSeason recipeSeason, final HttpServletResponse response) {
		return this.recipeSeasonRepository.save(recipeSeason);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	void delete(@PathVariable Long id) {
		this.recipeSeasonRepository.delete(id);
	}
}
