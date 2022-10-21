package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.Discount;
import com.laptrinhjava.ShoppingCart.reponsitory.IDiscountRepository;
import com.laptrinhjava.ShoppingCart.service.IDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DiscountServiceImpl implements IDiscountService {
    @Autowired
    private IDiscountRepository discountRepository;

    @Override
    public List<Discount> findAll() {
        return discountRepository.findAll();
    }

    @Override
    public Discount save(Discount discount) {
        return discountRepository.save(discount);
    }

    @Override
    public Discount findDiscountById(Long id) {
        return discountRepository.findDiscountById(id);
    }

    @Override
    public void delete(Long id) {
        discountRepository.deleteById(id);
    }

    @Override
    public Discount update(Discount newDiscount, Long id) {
        return discountRepository.findById(id).map(
                discount -> {
                    discount.setName(newDiscount.getName());
                    discount.setDescription(newDiscount.getDescription());
                    discount.setDiscountPercent(newDiscount.getDiscountPercent());
                    discount.setCreatedDate(new Date());
                    return discountRepository.save(discount);
                }
        ).orElseGet(() -> {
                    newDiscount.setId(id);
                    return discountRepository.save(newDiscount);
                }
        );
    }
}
