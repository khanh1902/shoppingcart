package com.laptrinhjava.ShoppingCart.reponsitory;

import com.laptrinhjava.ShoppingCart.entity.Order;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    Order findOrderById(Long id);
    Long countByUsers_Id(Long userId);
    Order findByUsers_Id(Long userId);
    @Transactional
    List<Order> findAllByUsers_Id(Long id);
    @Transactional
    List<Order> findAllByUsers_IdAndStatusContainingIgnoreCase(Long userId, String status);
    @Transactional
    List<Order> findALlByStatusContainingIgnoreCase(String status);


}
