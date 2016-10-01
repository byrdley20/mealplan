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

import com.meals.domain.RecipeHistory;
import com.meals.repository.RecipeHistoryRepository;

@RestController
@RequestMapping(value = "/rest/recipeHistory")
public class RecipeHistoryRestController extends BaseRestController {
	@Autowired
	RecipeHistoryRepository recipeHistoryRepository;

	@RequestMapping(method = RequestMethod.GET)
	Collection<RecipeHistory> all() {
		return this.recipeHistoryRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	RecipeHistory save(@RequestBody RecipeHistory recipeHistory, final HttpServletResponse response) {
		return this.recipeHistoryRepository.save(recipeHistory);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	void delete(@PathVariable Long id) {
		this.recipeHistoryRepository.delete(id);
	}
}
