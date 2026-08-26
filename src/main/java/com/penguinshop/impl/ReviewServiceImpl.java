package com.penguinshop.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.penguinshop.domain.ORDER_STATUS;
import com.penguinshop.exceptions.ReviewException;
import com.penguinshop.model.Product;
import com.penguinshop.model.Review;
import com.penguinshop.model.User;
import com.penguinshop.repository.OrderItemRepository;
import com.penguinshop.repository.ProductRepository;
import com.penguinshop.repository.ReviewRepository;
import com.penguinshop.request.CreateReviewRequest;
import com.penguinshop.request.UserPurchaseStatus;
import com.penguinshop.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository reviewRepository;
    OrderItemRepository orderItemRepository;
    ProductRepository productRepository;

    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) throws ReviewException {
        // Check if user has purchased and received product before allowing them to post
        // a review
        UserPurchaseStatus status = orderItemRepository.getUserPurchaseStatus(user.getId(), product.getId(),
                ORDER_STATUS.DELIVERED);

        if (!status.getHasPurchased())
            throw new ReviewException("You must purchase this product before reviewing it");

        // If user has already posted a review, don't allow them to post another
        if (!(status.getReviewCount() < 1))
            throw new ReviewException("You can only post one review per product");

        Review review = new Review();
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProductImages(req.getProductImages());
        review.setProduct(product);
        review.setUser(user);

        product.getReviews().add(review);
        // productRepository.save(product);

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review getReviewById(Long reviewId) throws ReviewException {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException("Review not found"));
    }

    @Override
    public Review updateReview(Long userId, Long reviewId, CreateReviewRequest req) throws ReviewException {
        Review review = getReviewById(reviewId);
        
        if(!(review.getUser().getId().equals(userId)))
            throw new ReviewException("You can't update this review");

        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProductImages(req.getProductImages());

        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws ReviewException {
        Review review = getReviewById(reviewId);
        
        if(!review.getUser().getId().equals(userId))
            throw new ReviewException("You can't delete this review");
        
        reviewRepository.delete(review);
    }

}
