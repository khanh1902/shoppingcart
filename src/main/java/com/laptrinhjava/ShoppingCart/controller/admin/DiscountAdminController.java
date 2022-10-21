package com.laptrinhjava.ShoppingCart.controller.admin;

import com.laptrinhjava.ShoppingCart.entity.Discount;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.service.IDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/discount")
public class DiscountAdminController {
    @Autowired
    IDiscountService discountService;

    @PostMapping("/save")
    public ResponseEntity<ResponseObject> save(@RequestBody Discount discount) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Save Successfully!", discountService.save(discount))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateDiscount(@RequestBody Discount discount, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update Successfully!", discountService.update(discount, id))
        );
    }

    @DeleteMapping("/{id}")
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
