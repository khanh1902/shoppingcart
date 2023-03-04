package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.Discount;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.service.IDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discount")
public class DiscountController {
    @Autowired
    IDiscountService discountService;

    /**
     * Method: Find All Discount
     * **/
    @GetMapping()
    public List<Discount> findAll() {
        return discountService.findAll();
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> save(@RequestBody Discount discount) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Save Successfully!", discountService.save(discount))
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> updateDiscount(@RequestBody Discount discount, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update Successfully!", discountService.update(discount, id))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> deleteDiscount(@PathVariable Long id) {
        if (discountService.findDiscountById(id) != null) {
            discountService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Delete Successfully!", "")
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", "Discount Not Found!", "")
            );
        }
    }
}
