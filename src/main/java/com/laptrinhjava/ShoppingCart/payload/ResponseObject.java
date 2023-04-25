package com.laptrinhjava.ShoppingCart.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseObject {
    private String status;
    private String message;
    private Object data;

    // constructor
    public ResponseObject(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
