package com.laptrinhjava.ShoppingCart.reponsitory;

import com.laptrinhjava.ShoppingCart.entity.Cart;
import com.laptrinhjava.ShoppingCart.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartItemsRepository extends JpaRepository<CartItems, Long> {
    void deleteByCart(Cart cart);
}
