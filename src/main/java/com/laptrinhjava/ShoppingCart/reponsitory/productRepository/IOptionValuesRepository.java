package com.laptrinhjava.ShoppingCart.reponsitory.productRepository;

import com.laptrinhjava.ShoppingCart.entity.OptionValues;
import com.laptrinhjava.ShoppingCart.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOptionValuesRepository extends JpaRepository<OptionValues, Long> {
    OptionValues findOptionValuesById(Long id);
    OptionValues findOptionValuesByName(String name);
    List<OptionValues> findByOption(Options options);
    OptionValues findByIdAndOption_Id(Long valueId, Long optionId);
}

