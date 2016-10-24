package com.meals.controller.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.meals.domain.RecipeAssignment;
import com.meals.domain.RecipeIngredient;
import com.meals.dto.ShoppingItemDTO;
import com.meals.repository.RecipeAssignmentRepository;
import com.meals.repository.RecipeIngredientRepository;

@Controller
public class ShoppingListController extends BaseController {

	@Autowired
	private RecipeAssignmentRepository recipeAssignmentRepository;
	@Autowired
	private RecipeIngredientRepository recipeIngredientRepository;

	@RequestMapping("/shoppingList.html")
	public String dashboard(@RequestParam(value = "month", required = true) int month, Model model, HttpServletRequest request) throws IOException {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month - 1);
		int minDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, minDay);
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		int maxDay = endDate.getActualMaximum(Calendar.DAY_OF_MONTH);
		endDate.set(Calendar.DAY_OF_MONTH, maxDay);

		Map<String, Map<String, List<ShoppingItemDTO>>> shoppingLists = new HashMap<String, Map<String, List<ShoppingItemDTO>>>();
		Map<String, List<ShoppingItemDTO>> shoppingListByIngredientTypeByDay = new HashMap<String, List<ShoppingItemDTO>>();
		Map<String, List<ShoppingItemDTO>> shoppingListByIngredientTypeByName = new HashMap<String, List<ShoppingItemDTO>>();
		List<RecipeAssignment> recipeAssignments = recipeAssignmentRepository.findByDateBetweenOrderByDateAsc(cal.getTime(), endDate.getTime());
		for (RecipeAssignment recipeAssignment : recipeAssignments) {
			List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByRecipeId(recipeAssignment.getRecipeMealType().getRecipe().getId());
			for (RecipeIngredient recipeIngredient : recipeIngredients) {
				Calendar recipeDate = Calendar.getInstance();
				recipeDate.setTime(recipeAssignment.getDate());
				int day = recipeDate.get(Calendar.DAY_OF_MONTH);
				String ingredientType = recipeIngredient.getIngredient().getIngredientType().getName();

				addIngredientToMap(shoppingListByIngredientTypeByDay, recipeIngredient, day, ingredientType);
				addIngredientToMap(shoppingListByIngredientTypeByName, recipeIngredient, day, ingredientType);
			}
		}

		for (String ingredientTypeKey : shoppingListByIngredientTypeByDay.keySet()) {
			Map<String, List<ShoppingItemDTO>> shoppingListForIngredient = new HashMap<String, List<ShoppingItemDTO>>();
			List<ShoppingItemDTO> shoppingListSortedByDay = shoppingListByIngredientTypeByDay.get(ingredientTypeKey);
			Collections.sort(shoppingListSortedByDay); //sort by day
			shoppingListForIngredient.put("day", shoppingListSortedByDay);

			List<ShoppingItemDTO> shoppingListSortedByName = shoppingListByIngredientTypeByName.get(ingredientTypeKey);
			Collections.sort(shoppingListSortedByName, ShoppingItemComparator); // sort by name
			shoppingListForIngredient.put("name", shoppingListSortedByName);

			shoppingLists.put(ingredientTypeKey, shoppingListForIngredient);
		}
		
		model.addAttribute("shoppingLists", shoppingLists);
		return "shoppingList";
	}

	private void addIngredientToMap(Map<String, List<ShoppingItemDTO>> shoppingListByIngredientType, RecipeIngredient recipeIngredient, int day, String ingredientType) {
		List<ShoppingItemDTO> shoppingItemsForIngredientType = shoppingListByIngredientType.get(ingredientType);
		if (shoppingItemsForIngredientType == null) {
			shoppingItemsForIngredientType = new ArrayList<ShoppingItemDTO>();
		}
		ShoppingItemDTO shoppingItem = new ShoppingItemDTO(day, recipeIngredient.getRecipe().getName(), convertToIngredientDTO(recipeIngredient.getIngredient()));
		shoppingItem.getIngredient().setAmount(recipeIngredient.getAmount());
		shoppingItemsForIngredientType.add(shoppingItem);
		shoppingListByIngredientType.put(ingredientType, shoppingItemsForIngredientType);
	}

	public static Comparator<ShoppingItemDTO> ShoppingItemComparator = new Comparator<ShoppingItemDTO>() {

		@Override
		public int compare(ShoppingItemDTO shoppingItem1, ShoppingItemDTO shoppingItem2) {

			String shoppingItemName1 = shoppingItem1.getIngredient().getName();
			String shoppingItemName2 = shoppingItem2.getIngredient().getName();

			// ascending order
			return shoppingItemName1.compareTo(shoppingItemName2);
		}

	};
}
