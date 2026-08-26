package com.penguinshop.service;

import java.util.List;

import com.penguinshop.model.HomeCategory;

public interface HomeCategoryService{
    HomeCategory createHomeCategory(HomeCategory homeCategory);
    List<HomeCategory> createHomeCategories(List<HomeCategory> homeCategories);
    List<HomeCategory> getAllHomeCategories();
    HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception;
}
