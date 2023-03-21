package com.laptrinhjava.ShoppingCart.service.productService;

import com.laptrinhjava.ShoppingCart.entity.OptionValues;
import com.laptrinhjava.ShoppingCart.entity.Options;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOptionValuesService {
    OptionValues findByID(Long id);
    OptionValues findByName(String name);
    OptionValues save(OptionValues optionValues);
    List<OptionValues> findByOption(Options options);
    OptionValues findByIdAndOption_Id(Long valueId, Long optionId);
}
