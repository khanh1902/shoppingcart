package com.laptrinhjava.ShoppingCart.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface IAmazonClient {
    String uploadFile(MultipartFile multipartFile);
    void deleteFile(String fileName);
    String updateFile(String fileName, String newFile);
    void deleteOneFile(String key);
}
