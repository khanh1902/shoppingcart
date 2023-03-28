package com.laptrinhjava.ShoppingCart.service.impl.productServiceImpl;

import com.laptrinhjava.ShoppingCart.entity.Options;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IOptionsRepository;
import com.laptrinhjava.ShoppingCart.service.productService.IOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionsServiceImpl implements IOptionsService {
    @Autowired
    private IOptionsRepository optionsRepository;

    @Override
    public Options findById(Long id) {
        return optionsRepository.findOptionById(id);
    }

    @Override
    public Options findByName(String name) {
        return optionsRepository.findByName(name);
    }

    @Override
    public Options save(Options option) {
        return optionsRepository.save(option);
    }
}
