package com.penguinshop.service;

import java.util.List;

import com.penguinshop.exceptions.ReviewException;
import com.penguinshop.model.Product;
import com.penguinshop.model.Review;
import com.penguinshop.model.User;
import com.penguinshop.request.CreateReviewRequest;

public interface ReviewService {
    Review createReview(CreateReviewRequest req,
            User user,
            Product product) throws ReviewException;

    List<Review> getReviewsByProductId(Long productId);
    
    Review getReviewById(Long reviewId) throws ReviewException;

    Review updateReview(Long userId, Long reviewId, CreateReviewRequest req) throws ReviewException;

    void deleteReview(Long reviewId, Long userId) throws ReviewException;
}
