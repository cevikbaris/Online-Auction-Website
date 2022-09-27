package com.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entity.Category;
import com.app.repository.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	CategoryRepository categoryRepository;

	public Category findById(int category) {
	 Optional<Category> myCategory = categoryRepository.findById(category);
	 	if(myCategory.isPresent()) {
	 		return myCategory.get();
	 	}else {
	 		return null;
	 	}
	}
	
	public int findByName(String name) {
		return categoryRepository.findByCategoryName(name);
	}
	
	
}
