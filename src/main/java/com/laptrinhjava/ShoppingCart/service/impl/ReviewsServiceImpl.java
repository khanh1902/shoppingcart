package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.OrderItems;
import com.laptrinhjava.ShoppingCart.entity.Reviews;
import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.payload.request.reviews.UpdateReviewRequest;
import com.laptrinhjava.ShoppingCart.payload.response.reviews.ReviewDetailResponse;
import com.laptrinhjava.ShoppingCart.payload.response.reviews.ReviewResponse;
import com.laptrinhjava.ShoppingCart.reponsitory.IOrderItemsRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.IReviewsRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.IUserRepository;
import com.laptrinhjava.ShoppingCart.service.IReviewsService;
import com.laptrinhjava.ShoppingCart.service.productService.IOrderItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.laptrinhjava.ShoppingCart.common.HandleAuth.getUsername;

@Service
public class ReviewsServiceImpl implements IReviewsService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IReviewsRepository reviewsRepository;

    @Autowired
    private IOrderItemsRepository orderItemsRepository;

    @Override
    public Reviews findReviewsById(Long id) {
        return reviewsRepository.findReviewsById(id);
    }

    @Override
    public ReviewDetailResponse update(Long reviewId, UpdateReviewRequest reviewRequest) throws Exception {
        String email = getUsername();
        Users findUser = userRepository.findByEmail(email);
        Reviews findReview = reviewsRepository.findByIdAndUsers_Id(reviewId, findUser.getId());
        if(findReview == null) throw new Exception("Review does not exists!");
        findReview.setDescription(reviewRequest.getDescription());
        findReview.setRating(reviewRequest.getRating());
        findReview.setIsReview(true);

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        findReview.setCreatedDate(cal.getTime());

        reviewsRepository.save(findReview);
        return new ReviewDetailResponse(findReview.getId(), findReview.getRating(), findReview.getDescription(), findReview.getCreatedDate(),
                findUser.getFullName(), findReview.getIsReview(), findReview.getProducts().getName());
    }

    @Override
    public List<ReviewResponse> findAllByIsReviewAndUserId(String isReview) throws Exception {
        String email = getUsername();
        Users findUser = userRepository.findByEmail(email);
        List<Reviews> findReviews = null;
        if(isReview.toLowerCase().equals("true")){
            findReviews = reviewsRepository.findAllByIsReviewAndUsers_Id(true, findUser.getId());
        }
        else{
            findReviews = reviewsRepository.findAllByIsReviewAndUsers_Id(false, findUser.getId());
        }
        if(findReviews.isEmpty()) throw new Exception("Reviews does not exists!");

        //convert review to reviewResponse;
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (Reviews review : findReviews){
            OrderItems findOrderItem = orderItemsRepository.findByProductIdAndOrder_Id(review.getProducts().getId(), review.getOrder().getId());
            if(findOrderItem == null) throw new Exception("Product Order does not exists!");
            ReviewResponse reviewResponse = new ReviewResponse(review.getId(), review.getProducts().getName(), review.getProducts().getImageUrl(),
                    findOrderItem.getQuantity(), findOrderItem.getPrice(), review.getDescription(), review.getRating());
            reviewResponses.add(reviewResponse);
        }
        return reviewResponses;
    }

    @Override
    public List<ReviewDetailResponse> findAllByProductId(Long productId) throws Exception {
        List<ReviewDetailResponse> reviewResponses = new ArrayList<>();
        List<Reviews> findReviews = reviewsRepository.findAllByProducts_Id(productId);
        if(findReviews.isEmpty()) throw new Exception("Review does not found!");
        for(Reviews review : findReviews){
            Users findUser = userRepository.findUsersById(review.getUsers().getId());
            if(findUser == null) throw new Exception("User does not found!");
            ReviewDetailResponse reviewResponse = new ReviewDetailResponse(review.getId(), review.getRating(), review.getDescription(), review.getCreatedDate(),
                    findUser.getFullName(), review.getIsReview(), review.getProducts().getName());
            reviewResponses.add(reviewResponse);
        }
        return reviewResponses;
    }

    @Override
    public void DeleteById(Long reviewId) throws Exception {
        Reviews findReview = reviewsRepository.findReviewsById(reviewId);
        if(findReview == null) throw new Exception("Review does not found!");
        reviewsRepository.deleteById(findReview.getId());
    }
}
