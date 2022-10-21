package com.laptrinhjava.ShoppingCart.payload.request;

import org.springframework.web.multipart.MultipartFile;

public class ProductRequest {
    private String name;
    private Long price;
    private Long categoryId;
    private Long discountId;

    public ProductRequest() {
    }

    public ProductRequest(String name, Long price, Long categoryId, Long discountId) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.discountId = discountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }
}
