package com.meals.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController extends BaseController {
	@RequestMapping("/login")
	String login(Model model) {
		return "login";
	}
}
