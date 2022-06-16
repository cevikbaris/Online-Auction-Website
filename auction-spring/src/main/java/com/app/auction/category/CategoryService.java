package com.app.auction.category;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
