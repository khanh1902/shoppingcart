package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.entity.Product;
import com.laptrinhjava.ShoppingCart.service.IAmazonClient;
import com.laptrinhjava.ShoppingCart.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Consumes;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IAmazonClient amazonClient;

    /**
     * Method: Find All Product
     **/
    @GetMapping()
    public List<Product> findAll() {
        return productService.fillAll();
    }

    /**
     * Method: Save Product
     **/
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Consumes("multipart/form-data")
    public ResponseEntity<ResponseObject> save(@RequestParam("name") String name,
                                               @RequestParam("price") Long price,
                                               @RequestParam("file") MultipartFile[] files,
                                               @RequestParam("categoryId") Long categoryId,
                                               @RequestParam("discountId") Long discountId) {
        StringBuffer imageUrl = new StringBuffer();
        for (MultipartFile file : files) {
            String url = amazonClient.uploadFile(file);
            imageUrl.append(url + ","); // ngan cach cac imageUrl bang dau phay
        }
        Product product = new Product(name, price, imageUrl.toString(), categoryId, discountId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Save Successfully!", productService.save(product))
        );
    }

    /**
     * Method: Delete Product
     **/
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> delete(@PathVariable Long id) {
        try {
            Product findById = productService.findProductById(id);
            if (findById != null) {
                amazonClient.deleteFile(findById.getImageUrl());
                productService.delete(id);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Delete successfully!", null)
                );
            } else
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED", "Delete failed!", null)
                );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", "Error!", e.getMessage())
            );
        }

    }

    /**
     * Method: Update Product
     **/
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> updateProduct(@RequestParam("name") String name,
                                                        @RequestParam("price") Long price,
                                                        @RequestParam("file") MultipartFile[] files,
                                                        @RequestParam("categoryId") Long categoryId,
                                                        @RequestParam("discountId") Long discountId,
                                                        @PathVariable Long id) {
        Product findById = productService.findProductById(id);
        if (findById != null) {
            amazonClient.deleteFile(findById.getImageUrl());
        }
        StringBuffer imageUrl = new StringBuffer();
        for (MultipartFile file : files) {
            String url = amazonClient.uploadFile(file);
            imageUrl.append(url + ", ");
        }

        Product newProduct = new Product(name, price, imageUrl.toString(), categoryId, discountId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update Successfully!", productService.update(newProduct, id))
        );
    }
}
