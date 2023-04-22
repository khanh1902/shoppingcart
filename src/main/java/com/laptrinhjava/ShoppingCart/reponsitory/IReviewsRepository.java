package com.laptrinhjava.ShoppingCart.reponsitory;

import com.laptrinhjava.ShoppingCart.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReviewsRepository extends JpaRepository<Reviews, Long> {
    Reviews findByIdAndUsers_Id(Long id, Long userId);
    Reviews findReviewsById(Long id);
    List<Reviews> findAllByIsReviewAndUsers_Id(Boolean isReview, Long userId);
    List<Reviews> findAllByProducts_Id(Long productId);
}
