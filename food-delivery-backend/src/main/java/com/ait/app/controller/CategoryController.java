package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.CategoryDto;
import com.ait.app.model.Category;
import com.ait.app.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@PostMapping("/restaurant/{rid}")
	public ResponseEntity createCategory(@PathVariable int rid, @RequestBody CategoryDto dto) {

		categoryService.createCategory(rid, dto);

		return new ResponseEntity("Category created for restaurant id :" + rid, HttpStatus.CREATED);

	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable int categoryId) {

		CategoryDto dto = categoryService.getCategoryById(categoryId);

		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@PutMapping("/restaurant/{rId}/category/{cId}")
	public ResponseEntity<Category> updateCategory(
	        @PathVariable int rId,
	        @PathVariable int cId,
	        @RequestBody CategoryDto dto) {

		Category category = categoryService.updateCategory(cId, rId, dto);

		    return new ResponseEntity(category, HttpStatus.OK);
	}

}
