package com.laptrinhjava.ShoppingCart.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface AmazonClient {
    String uploadFile(MultipartFile multipartFile);
    void deleteFile(String fileName);
}
