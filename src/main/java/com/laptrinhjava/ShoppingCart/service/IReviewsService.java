package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Reviews;
import com.laptrinhjava.ShoppingCart.payload.request.reviews.UpdateReviewRequest;
import com.laptrinhjava.ShoppingCart.payload.response.reviews.ReviewDetailResponse;
import com.laptrinhjava.ShoppingCart.payload.response.reviews.ReviewResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IReviewsService {
    Reviews findReviewsById(Long id);
    ReviewDetailResponse update(Long reviewId, UpdateReviewRequest reviewRequest) throws Exception;
    List<ReviewResponse> findAllByIsReviewAndUserId(String isReview) throws Exception;
    List<ReviewDetailResponse> findAllByProductId(Long productId) throws Exception;
    void DeleteById(Long reviewId) throws Exception;
    Long countReviews(Long productId);
    Double averageRating(Long productId);

}
