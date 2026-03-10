package com.penguinshop.request;

import lombok.Data;

@Data
public class AddCartItemRequest {
    private String size;
    private int quantity;
    private Long productId;
}
