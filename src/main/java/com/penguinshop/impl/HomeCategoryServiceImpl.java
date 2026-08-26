package com.penguinshop.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.penguinshop.model.HomeCategory;
import com.penguinshop.repository.HomeCategoryRepository;
import com.penguinshop.service.HomeCategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HomeCategoryServiceImpl implements HomeCategoryService{
    private final HomeCategoryRepository homeCategoryRepository;
    
    @Override
	public HomeCategory createHomeCategory(HomeCategory homeCategory) {
            return homeCategoryRepository.save(homeCategory);
	}

	@Override
	public List<HomeCategory> createHomeCategories(List<HomeCategory> homeCategories) {
        if(homeCategoryRepository.findAll().isEmpty())
            return homeCategoryRepository.saveAll(homeCategories);
        return homeCategoryRepository.findAll();
	}

	@Override
	public List<HomeCategory> getAllHomeCategories() {
        return homeCategoryRepository.findAll();
	}

	@Override
	public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception {
        HomeCategory existingCategory = homeCategoryRepository.findById(id).orElseThrow(() -> new Exception("Category not found"));

        if(homeCategory.getImage() != null)
            existingCategory.setImage(homeCategory.getImage());

        if(homeCategory.getCategoryId() != null) 
            existingCategory.setId(homeCategory.getId());
        
        return homeCategoryRepository.save(existingCategory);
	}  

}
