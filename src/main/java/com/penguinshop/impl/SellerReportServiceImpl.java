package com.penguinshop.impl;

import org.springframework.stereotype.Service;

import com.penguinshop.model.Seller;
import com.penguinshop.model.SellerReport;
import com.penguinshop.repository.SellerReportRepository;
import com.penguinshop.service.SellerReportService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SellerReportServiceImpl implements SellerReportService {
    SellerReportRepository sellerReportRepository;

    @Override
    public SellerReport getSellerReport(Seller seller) {
        SellerReport sellerReport = sellerReportRepository.findBySellerId(seller.getId());

        // If report doesn't exist, create a new one and return it
        if(sellerReport == null) {
            SellerReport newReport = new SellerReport();
            newReport.setSeller(seller);
            return sellerReportRepository.save(newReport); 
        }
        
        return sellerReport;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepository.save(sellerReport);
    }

}
