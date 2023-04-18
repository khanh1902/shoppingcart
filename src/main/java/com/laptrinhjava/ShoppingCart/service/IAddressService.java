package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Address;
import com.laptrinhjava.ShoppingCart.payload.request.address.AddressRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAddressService {
    Address findById(Long addressId);
    Address update(Long id, AddressRequest addressRequest);
    List<Address> findALlByUsers_Id();
    void deleteByAddressId(Long addressId);


}
