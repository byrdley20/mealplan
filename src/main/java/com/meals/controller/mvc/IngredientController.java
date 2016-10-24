package com.meals.controller.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meals.repository.IngredientRepository;
import com.meals.repository.IngredientTypeRepository;

@Controller
public class IngredientController extends BaseController {

	@Autowired
	private IngredientRepository ingredientRepository;

	@Autowired
	private IngredientTypeRepository ingredientTypeRepository;

	@RequestMapping({ "/ingredients.html" })
	public String recipes(Model model, HttpServletRequest request) throws IOException {
		model.addAttribute("allIngredientTypes", this.ingredientTypeRepository.findAll(sortByNameAsc()));
		model.addAttribute("allIngredients", convertToIngredientDTOs(this.ingredientRepository.findAll()));
		return "ingredients";
	}
}
