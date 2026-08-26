package com.penguinshop.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.penguinshop.model.Deal;
import com.penguinshop.model.HomeCategory;
import com.penguinshop.repository.DealRepository;
import com.penguinshop.repository.HomeCategoryRepository;
import com.penguinshop.service.DealService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DealServiceImpl implements DealService {
    DealRepository dealRepository;
    HomeCategoryRepository homeCategoryRepository;

    @Override
    public List<Deal> getDeals() {
        List<Deal> deals = dealRepository.findAll();
        return deals;
    }

    @Override
    public Deal createDeal(Deal deal) {
        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId())
                .orElseThrow(null);
        Deal newDeal = new Deal();
        newDeal.setCategory(deal.getCategory());
        newDeal.setDiscount(deal.getDiscount());
        return dealRepository.save(newDeal);
    }

    @Override
    public Deal updateDeal(Long dealId, Deal deal) throws Exception {
        Deal existingDeal = dealRepository.findById(dealId).orElse(null);
        HomeCategory homeCategory = homeCategoryRepository.findById(
                deal.getCategory().getId()).orElse(null);

        if (existingDeal != null) {
            if (deal.getDiscount() != null)
                existingDeal.setDiscount(deal.getDiscount());

            if (homeCategory != null)
                existingDeal.setCategory(deal.getCategory());

            return dealRepository.save(existingDeal);
        }
        throw new Exception("Unable to update deal, deal not found");
    }

    @Override
    public void deleteDeal(Long dealId) throws Exception {
        Deal deal = dealRepository.findById(dealId).orElseThrow(() -> new Exception("Could not find deal"));
        dealRepository.delete(deal);
    }

}
