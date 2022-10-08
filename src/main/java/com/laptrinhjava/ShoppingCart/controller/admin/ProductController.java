package com.laptrinhjava.ShoppingCart.controller.admin;

import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.entity.Product;
import com.laptrinhjava.ShoppingCart.service.AmazonClient;
import com.laptrinhjava.ShoppingCart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Consumes;

@RestController
@RequestMapping("api/admin/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private AmazonClient amazonClient;

    @PostMapping("/save")
    @Consumes("multipart/form-data")
    public ResponseEntity<ResponseObject> save(@RequestParam("name") String name,
                                               @RequestParam("price") Long price,
                                               @RequestParam("file") MultipartFile file,
                                               @RequestParam("categoryId") Long categoryId,
                                               @RequestParam("discountId") Long discountId) {
        String imageUrl = amazonClient.uploadFile(file);
        Product product = new Product(name, price, imageUrl, categoryId, discountId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Save Successfully!", productService.save(product))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable Long id) {
        Product findById = productService.findProductById(id);
        if (findById != null) {
            productService.delete(id);
            amazonClient.deleteFile(findById.getImageUrl());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Delete successfully!", "")
            );
        } else
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", "Delete failed!", "")
            );
    }
}
