package com.laptrinhjava.ShoppingCart.service.productService;

import com.laptrinhjava.ShoppingCart.entity.ProductOptions;
import org.springframework.stereotype.Service;

@Service
public interface IProductOptionsService {
    ProductOptions save(ProductOptions productOptions);
//    ProductOptions findProductOptionsByOptionId(Long optionId);
}
