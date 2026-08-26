package com.penguinshop.request;

import lombok.Data;

// Used in review to check if user can post review
@Data
public class UserPurchaseStatus {
    private Boolean hasPurchased;
    private int reviewCount;
}
