package com.penguinshop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penguinshop.model.Deal;
import com.penguinshop.response.ApiResponse;
import com.penguinshop.service.DealService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController{
    private final DealService dealService;
    
    @GetMapping
    public ResponseEntity<List<Deal>> getDeals() {
        List<Deal> deals = dealService.getDeals(); 
        return ResponseEntity.ok(deals);
    }

    @PostMapping
    public ResponseEntity<Deal> createDeal(@RequestBody Deal deal) {
        Deal createdDeal = dealService.createDeal(deal);
        return new ResponseEntity<>(createdDeal,HttpStatus.CREATED);
    }

    @PatchMapping("/{dealId}")
    public ResponseEntity<Deal> updateDeal(@PathVariable Long dealId,@RequestBody Deal deal) throws Exception {
        Deal updatedDeal = dealService.updateDeal(dealId, deal);
        return ResponseEntity.ok(updatedDeal); 
    }

    @DeleteMapping("/{dealId}")
    public ResponseEntity<ApiResponse> deleteDeal(@PathVariable Long dealId,@RequestBody Deal deal) throws Exception {
        dealService.deleteDeal(dealId);
        ApiResponse response = new ApiResponse();
        response.setMessage("Successfully deleted deal!");
        return ResponseEntity.ok(response); 
    }

    
}
