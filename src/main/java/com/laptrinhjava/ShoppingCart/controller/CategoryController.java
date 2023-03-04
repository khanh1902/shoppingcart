package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.Category;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.service.IAmazonClient;
import com.laptrinhjava.ShoppingCart.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IAmazonClient amazonClient;

    /**
     * Method: Find All Category
     * **/
    @GetMapping()
    public List<Category> findAll(){
        return categoryService.findAll();
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> save (@RequestParam("name") String name,
                                                @RequestParam("code") String code,
                                                @RequestParam("file")MultipartFile[] files){
        StringBuilder imageUrl = new StringBuilder();
        for(MultipartFile file : files){
            String url = amazonClient.uploadFile(file);
            imageUrl.append(url + ", "); // ngan cach cac imageUrl bang dau phay
        }

        Category category = new Category(name, code, imageUrl.toString());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Save Successfully!", categoryService.save(category))
        );
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> updateCategory(@RequestBody Category newCategory, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update Successfully!", categoryService.update(newCategory, id))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Long id) {
        if (categoryService.findCategoryById(id) != null) {
            categoryService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Delete Successfully!", "")
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", "Category Not Found!", "")
            );
        }
    }
}
