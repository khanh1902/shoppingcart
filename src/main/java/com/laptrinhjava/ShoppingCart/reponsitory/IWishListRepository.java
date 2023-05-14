package com.laptrinhjava.ShoppingCart.reponsitory;

import com.laptrinhjava.ShoppingCart.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findAllByUsers_Id(Long userId);
    WishList findByIdAndUsers_Id(Long id, Long userId);
    WishList findWishListById(Long id);
    WishList findByUsers_IdAndProducts_Id(Long userId, Long productId);
}
