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

import com.meals.domain.Tag;
import com.meals.repository.TagRepository;

@RestController
@RequestMapping(value = "/rest/tags")
public class TagRestController extends BaseRestController {
	@Autowired
	TagRepository tagRepository;

	@RequestMapping(method = RequestMethod.GET)
	Collection<Tag> all() {
		return this.tagRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	Tag save(@RequestBody Tag tag, final HttpServletResponse response) {
		return this.tagRepository.save(tag);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	void delete(@PathVariable Long id) {
		this.tagRepository.delete(id);
	}
}
