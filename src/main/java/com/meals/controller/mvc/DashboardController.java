package com.meals.controller.mvc;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meals.common.AppConstants;
import com.meals.repository.MealTypeRepository;
import com.meals.repository.RecipeMealTypeRepository;
import com.meals.repository.TagRepository;

@Controller
public class DashboardController extends BaseController {

	@Autowired
	private TagRepository tagRepository;
	@Autowired
	private MealTypeRepository mealTypeRepository;
	@Autowired
	private RecipeMealTypeRepository recipeMealTypeRepository;

	@RequestMapping({ "/", "/dashboard.html" })
	public String dashboard(Model model, HttpServletRequest request) throws IOException {
		model.addAttribute("allTags", this.tagRepository.findAll(sortByNameAsc()));
		model.addAttribute("allMealTypes", this.mealTypeRepository.findAll(sortByOrdering()));
		model.addAttribute("allLunches", convertToRecipeDTOs(this.recipeMealTypeRepository.findByMealTypeName(AppConstants.MEAL_TYPE_LUNCH)));
		model.addAttribute("allDinners", convertToRecipeDTOs(this.recipeMealTypeRepository.findByMealTypeName(AppConstants.MEAL_TYPE_DINNER)));
		model.addAttribute("lunchMealTypeId", this.mealTypeRepository.findByName(AppConstants.MEAL_TYPE_LUNCH).getId());
		model.addAttribute("dinnerMealTypeId", this.mealTypeRepository.findByName(AppConstants.MEAL_TYPE_DINNER).getId());
		model.addAttribute("today", new Date());
		return "dashboard";
	}
}
