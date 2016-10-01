package com.meals.controller.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meals.repository.TagRepository;

@Controller
public class TagController extends BaseController {

	@Autowired
	private TagRepository tagRepository;

	@RequestMapping({ "/tags.html" })
	public String recipes(Model model, HttpServletRequest request) throws IOException {
		model.addAttribute("allTags", convertToTagDTOs(this.tagRepository.findAll()));
		return "tags";
	}
}
