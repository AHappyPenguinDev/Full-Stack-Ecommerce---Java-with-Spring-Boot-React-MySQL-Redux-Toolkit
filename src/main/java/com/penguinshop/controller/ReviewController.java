package com.penguinshop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penguinshop.exceptions.ReviewException;
import com.penguinshop.model.Product;
import com.penguinshop.model.Review;
import com.penguinshop.model.User;
import com.penguinshop.request.CreateReviewRequest;
import com.penguinshop.response.ApiResponse;
import com.penguinshop.service.ProductService;
import com.penguinshop.service.ReviewService;
import com.penguinshop.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;
    
    @PostMapping("/products/{productId}/reviews")
    ResponseEntity<Review> createReview(@RequestHeader("Authorization") String jwt, @RequestBody CreateReviewRequest req,@PathVariable  Long productId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(productId);
        Review review = reviewService.createReview(req, user, product);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/products/{productId}/reviews")
    ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reviews/{reviewId}")
    ResponseEntity<Review> getReviewById(@PathVariable Long reviewId) throws ReviewException {
        Review review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    @PatchMapping("/reviews/{reviewId}")
    ResponseEntity<Review> updateReview(@RequestHeader("Authorization") String jwt, Long reviewId, CreateReviewRequest req) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Review updatedReview = reviewService.updateReview(user.getId(), reviewId, req);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        reviewService.deleteReview(reviewId, user.getId()); 
        
        ApiResponse res = new ApiResponse();
        res.setMessage("Review deleted successfully!");
        return ResponseEntity.ok(res);
    }

}
