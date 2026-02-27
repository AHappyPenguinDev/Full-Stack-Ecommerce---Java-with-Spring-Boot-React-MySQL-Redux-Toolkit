package com.penguinshop.model;

import lombok.Data;

@Data
public class BankDetails {
    private String accountNumber;
    private String accountHolderName;
    private String ifsCode; // Indian financial system code
}

