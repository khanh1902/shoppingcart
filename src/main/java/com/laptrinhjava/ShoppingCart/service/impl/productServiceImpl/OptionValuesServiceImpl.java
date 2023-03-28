package com.laptrinhjava.ShoppingCart.service.impl.productServiceImpl;

import com.laptrinhjava.ShoppingCart.entity.OptionValues;
import com.laptrinhjava.ShoppingCart.entity.Options;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IOptionValuesRepository;
import com.laptrinhjava.ShoppingCart.service.productService.IOptionValuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionValuesServiceImpl implements IOptionValuesService {
    @Autowired
    private IOptionValuesRepository optionValuesRepository;

    @Override
    public OptionValues findByID(Long id) {
        return optionValuesRepository.findOptionValuesById(id);
    }

    @Override
    public OptionValues findByName(String name) {
        return optionValuesRepository.findOptionValuesByName(name);
    }

    @Override
    public OptionValues save(OptionValues optionValues) {
        return optionValuesRepository.save(optionValues);
    }

    @Override
    public List<OptionValues> findByOption(Options options) {
        return optionValuesRepository.findByOption(options);
    }

    @Override
    public OptionValues findByIdAndOption_Id(Long valueId, Long optionId) {
        return optionValuesRepository.findByIdAndOption_Id(valueId, optionId);
    }

    @Override
    public OptionValues findByNameAndOption_Id(String name, Long optionId) {
        return optionValuesRepository.findByNameAndOption_Id(name, optionId);
    }
}
