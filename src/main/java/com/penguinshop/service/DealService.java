package com.penguinshop.service;

import java.util.List;

import com.penguinshop.model.Deal;

public interface DealService {
    List<Deal> getDeals();

    Deal createDeal(Deal deal);

    Deal updateDeal(Long dealId, Deal deal) throws Exception;

    void deleteDeal(Long dealId) throws Exception;
}
