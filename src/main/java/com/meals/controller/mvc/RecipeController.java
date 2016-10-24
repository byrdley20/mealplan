package com.meals.controller.mvc;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meals.domain.RecipeMealType;
import com.meals.dto.RecipeDTO;
import com.meals.repository.IngredientRepository;
import com.meals.repository.RecipeMealTypeRepository;
import com.meals.repository.TagRepository;

@Controller
public class RecipeController extends BaseController {

	@Autowired
	private RecipeMealTypeRepository recipeMealTypeRepository;
	@Autowired
	private TagRepository tagRepository;
	@Autowired
	private IngredientRepository ingredientRepository;

	@RequestMapping({ "/recipes.html" })
	public String recipes(Model model, HttpServletRequest request) throws IOException {
		Map<Long, RecipeDTO> recipeIdsToRecipe = new LinkedHashMap<Long, RecipeDTO>();
		List<RecipeMealType> recipeMealTypes = this.recipeMealTypeRepository.findAll();
		List<RecipeDTO> recipeDTOs = convertToRecipeDTOsWithTags(recipeMealTypes);
		for (RecipeDTO recipeDTO : recipeDTOs) {
			RecipeDTO existingRecipeDTO = recipeIdsToRecipe.get(recipeDTO.getId());
			if (existingRecipeDTO == null) {
				recipeIdsToRecipe.put(recipeDTO.getId(), recipeDTO);
			} else {
				existingRecipeDTO.setLunch(existingRecipeDTO.isLunch() == true ? true : recipeDTO.isLunch());
				existingRecipeDTO.setDinner(existingRecipeDTO.isDinner() == true ? true : recipeDTO.isDinner());
				recipeIdsToRecipe.put(existingRecipeDTO.getId(), existingRecipeDTO);
			}
		}
		model.addAttribute("allRecipes", recipeIdsToRecipe.values());
		model.addAttribute("allTags", convertToTagDTOs(this.tagRepository.findAll(sortByNameAsc())));
		model.addAttribute("allIngredients", convertToIngredientDTOs(ingredientRepository.findAll(sortByNameAsc())));
		return "recipes";
	}
}
