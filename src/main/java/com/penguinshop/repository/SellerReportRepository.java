package com.penguinshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penguinshop.model.SellerReport;

public interface SellerReportRepository extends JpaRepository<SellerReport,Long>{
    SellerReport findBySellerId(Long sellerId);
}
