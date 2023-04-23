package com.laptrinhjava.ShoppingCart.reponsitory;

import com.laptrinhjava.ShoppingCart.entity.CartItems;
import com.laptrinhjava.ShoppingCart.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IOrderItemsRepository extends JpaRepository<OrderItems, Long> {
    @Transactional
    @Modifying
    OrderItems findByProductIdAndOrder_Id(Long productId, Long orderId);
    @Transactional
    @Modifying
    List<OrderItems> findByOrder_Id(Long cartId);
}
