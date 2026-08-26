package com.penguinshop.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.penguinshop.domain.HOME_CATEGORY_SECTION;
import com.penguinshop.model.Deal;
import com.penguinshop.model.Home;
import com.penguinshop.model.HomeCategory;
import com.penguinshop.repository.DealRepository;
import com.penguinshop.service.HomeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService {
    DealRepository dealRepository;

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {
        List<HomeCategory> gridCategories = allCategories.stream()
                .filter(category -> category.getSection() == HOME_CATEGORY_SECTION.GRID)
                .collect(Collectors.toList());

        List<HomeCategory> shopByCategories = allCategories.stream()
                .filter(category -> category.getSection() == HOME_CATEGORY_SECTION.SHOP_BY_CATEGORIES)
                .collect(Collectors.toList());

        List<HomeCategory> electricCategories = allCategories.stream()
                .filter(category -> category.getSection() == HOME_CATEGORY_SECTION.ELECTRIC_CATEGORIES)
                .collect(Collectors.toList());

        List<HomeCategory> dealCategories = allCategories.stream()
                .filter(category -> category.getSection() == HOME_CATEGORY_SECTION.DEALS)
                .collect(Collectors.toList());

        List<Deal> createdDeals = new ArrayList<>();

        if(dealRepository.findAll().isEmpty()) {
            List<Deal> deals = allCategories.stream()
                .filter(category -> category.getSection() == HOME_CATEGORY_SECTION.DEALS)
                .map(category -> new Deal(null, 10 , category)) // 10% discount for all deals
                .collect(Collectors.toList());
            createdDeals = dealRepository.saveAll(deals);
        } else createdDeals = dealRepository.findAll();

        Home home = new Home();
        home.setGrid(gridCategories);
        home.setShopByCategories(shopByCategories);
        home.setElectricCategories(electricCategories);
        home.setDealCategories(dealCategories);
        home.setDeals(createdDeals);
        return home;
    }

}
