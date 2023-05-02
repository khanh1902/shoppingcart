package com.laptrinhjava.ShoppingCart.reponsitory;

import com.laptrinhjava.ShoppingCart.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderItemsRepository extends JpaRepository<OrderItems, Long> {
    OrderItems findByProductIdAndOrder_Id(Long productId, Long orderId);
    List<OrderItems> findByOrder_Id(Long cartId);
    List<OrderItems> findByProductId(Long productId);
    Long countByProductId(Long productId);
}
