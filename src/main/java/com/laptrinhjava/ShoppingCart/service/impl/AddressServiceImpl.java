package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.Address;
import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.payload.request.address.AddressRequest;
import com.laptrinhjava.ShoppingCart.reponsitory.IAddressRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.IUserRepository;
import com.laptrinhjava.ShoppingCart.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.laptrinhjava.ShoppingCart.common.HandleAuth.getUsername;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private IAddressRepository addressRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public Address findById(Long addressId) {
        return addressRepository.findAddressById(addressId);
    }

    @Override
    public Address update(Long id, AddressRequest addressRequest) {
        Address findAddress = addressRepository.findAddressById(id);
        if (addressRequest.getProvince() != null) findAddress.setProvince(addressRequest.getProvince());
        if (addressRequest.getDistrict() != null) findAddress.setDistrict(addressRequest.getDistrict());
        if (addressRequest.getWard() != null) findAddress.setWard(addressRequest.getWard());
        if (addressRequest.getAddressDetail() != null) findAddress.setAddressDetail(addressRequest.getAddressDetail());
        if (addressRequest.getIsDefault() != null) {
            if (addressRequest.getIsDefault().toLowerCase().equals("true")) {
                findAddress.setIsDefault(true);

                // update isDefault cho cac dia chi khac khi set dia chi da update la true
                String email = getUsername();
                Users findUser = userRepository.findByEmail(email);
                List<Address> addresses = addressRepository.findALlByUsers_Id(findUser.getId());
                if (!addresses.isEmpty()) {
                    for (Address address : addresses) {
                        address.setIsDefault(false);
                        addressRepository.save(address);
                    }
                }
            } else if (addressRequest.getIsDefault().toLowerCase().equals("false")) findAddress.setIsDefault(false);
        }


        return addressRepository.save(findAddress);
    }

    @Override
    public List<Address> findALlByUsers_Id() {
        String email = getUsername();
        Users findUser = userRepository.findByEmail(email);
        return addressRepository.findALlByUsers_Id(findUser.getId());
    }

    @Override
    public void deleteByAddressId(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    @Override
    public Address save(AddressRequest addressRequest) {
        String email = getUsername();
        Users findUser = userRepository.findByEmail(email);
//        List<Address> addresses = addressRepository.findALlByUsers_Id(findUser.getId());
        Address address = new Address(addressRequest.getProvince(), addressRequest.getDistrict(), addressRequest.getWard(),
                addressRequest.getAddressDetail(), findUser, null);
        if (addressRequest.getIsDefault().toLowerCase().equals("true")) {
            address.setIsDefault(true);
            // update isDefault cho cac dia chi khac khi set dia chi da update la true

            List<Address> addresses = addressRepository.findALlByUsers_Id(findUser.getId());
            if (!addresses.isEmpty()) {
                for (Address getAddress : addresses) {
                    getAddress.setIsDefault(false);
                    addressRepository.save(getAddress);
                }
            }
        }
        else if (addressRequest.getIsDefault().toLowerCase().equals("false")) address.setIsDefault(false);
        return addressRepository.save(address);
    }
}
