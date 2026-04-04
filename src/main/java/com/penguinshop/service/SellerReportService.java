package com.penguinshop.service;

import com.penguinshop.model.Seller;
import com.penguinshop.model.SellerReport;

public interface SellerReportService{
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);

}
