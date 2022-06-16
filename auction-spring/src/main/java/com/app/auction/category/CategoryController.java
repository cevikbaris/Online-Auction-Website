package com.app.auction.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {

	@Autowired
	CategoryRepository categoryRepository;
	
	@GetMapping("/category")
	List<Category> getCategories(){
		return categoryRepository.findAll();

	}
}
