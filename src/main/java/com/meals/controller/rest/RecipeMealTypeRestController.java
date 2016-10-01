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

import com.meals.domain.RecipeMealType;
import com.meals.repository.RecipeMealTypeRepository;

@RestController
@RequestMapping(value = "/rest/recipeMealTypes")
public class RecipeMealTypeRestController extends BaseRestController {
	@Autowired
	RecipeMealTypeRepository recipeMealTypeRepository;

	@RequestMapping(method = RequestMethod.GET)
	Collection<RecipeMealType> all() {
		return this.recipeMealTypeRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	RecipeMealType save(@RequestBody RecipeMealType recipeMealType, final HttpServletResponse response) {
		return this.recipeMealTypeRepository.save(recipeMealType);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	void delete(@PathVariable Long id) {
		this.recipeMealTypeRepository.delete(id);
	}
}
