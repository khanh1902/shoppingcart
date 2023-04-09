package com.laptrinhjava.ShoppingCart.reponsitory;

import com.laptrinhjava.ShoppingCart.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartItemsRepository extends JpaRepository<CartItems, Long> {
//    void deleteByProduct(Product product);
//    void deleteByProductId(Long id);
    CartItems findByProductIdAndProductVariantsId(Long productId, Long productVariantsId);
    List<CartItems> findByProductId(Long productId);
    List<CartItems> findByCart_Id(Long cartId);
    CartItems findCartItemsById(Long id);
}
