package com.laptrinhjava.ShoppingCart.reponsitory;

import com.laptrinhjava.ShoppingCart.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ICartItemsRepository extends JpaRepository<CartItems, Long> {
//    void deleteByProduct(Product product);
//    void deleteByProductId(Long id);
    CartItems findByProductIdAndProductVariantsId(Long productId, Long productVariantsId);
    @Transactional
    @Modifying
    List<CartItems> findByProductIdAndCart_Id(Long productId, Long cartId);
    List<CartItems> findByCart_Id(Long cartId);
    CartItems findCartItemsById(Long id);
}
