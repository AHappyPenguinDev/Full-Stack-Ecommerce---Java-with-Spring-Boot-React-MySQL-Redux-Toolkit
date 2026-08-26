package com.penguinshop.request;

import java.util.List;

import lombok.Data;

@Data
public class CreateReviewRequest{
    private String reviewText;
    private int reviewRating;
    private List<String> productImages;

}
