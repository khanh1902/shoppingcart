package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.Address;
import com.laptrinhjava.ShoppingCart.payload.request.address.AddressRequest;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.service.IAddressService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Qualifier("addressServiceImpl")
    @Autowired
    private IAddressService addressService;

    @PutMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> update(@RequestParam(name = "addressId") Long addressId,
                                                 @RequestBody AddressRequest addressRequest){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update Successfully!", addressService.update(addressId, addressRequest))
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> getAllForUser(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!", addressService.findALlByUsers_Id())
        );
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> deleteByAddressId(@RequestParam(name = "addressId") Long addressId){
        Address findAddress = addressService.findById(addressId);
        if (findAddress != null){
            addressService.deleteByAddressId(findAddress.getId());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Successfully!", addressService.findALlByUsers_Id())
            );
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("OK", "Address does not exists!!!", null)
            );
        }
    }

}
