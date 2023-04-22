package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.Reviews;
import com.laptrinhjava.ShoppingCart.payload.request.reviews.UpdateReviewRequest;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.service.IReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
public class ReviewsController {

    @Qualifier("reviewsServiceImpl")
    @Autowired
    private IReviewsService reviewsService;

    @GetMapping("getOne")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> getOneReview(@RequestParam(name = "reviewId") Long reviewId) {
        try {
            Reviews findReview = reviewsService.findReviewsById(reviewId);
            if (findReview == null) throw new Exception("Review does not exists!");
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Successfully!", findReview)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllReviewByIsReview(@RequestParam(name = "isReview") String isReview) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Successfully!", reviewsService.findAllByIsReviewAndUserId(isReview))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject> getAllReviewsByProductId(@RequestParam(name = "productId") Long productId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Successfully!", reviewsService.findAllByProductId(productId))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }


    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseObject> updateReview(@RequestParam(name = "reviewId") Long reviewId,
                                                       @RequestBody UpdateReviewRequest reviewRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update Successfully!", reviewsService.update(reviewId, reviewRequest))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> deleteReview(@RequestParam(name = "reviewId") Long reviewId) {
        try {
            reviewsService.DeleteById(reviewId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update Successfully!", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }

}
