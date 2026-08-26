package com.penguinshop.service;

import java.util.List;

import com.penguinshop.model.Home;
import com.penguinshop.model.HomeCategory;

public interface HomeService{
    public Home createHomePageData(List<HomeCategory> allCategories);
}
