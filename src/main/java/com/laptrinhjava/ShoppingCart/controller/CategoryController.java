package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.Category;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.service.IAmazonClient;
import com.laptrinhjava.ShoppingCart.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Consumes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IAmazonClient amazonClient;

    /**
     * Method: Find All Category
     **/
//    @GetMapping()
//    public ResponseEntity<ResponseObject> findAll() {
//        List<Category> findAll = categoryService.findAll();
//        if (findAll.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    new ResponseObject("FAILED", "Is Empty!", null)
//            );
//        } else
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject("OK", "Successfully!", findAll)
//            );
//    }
    @GetMapping
    public ResponseEntity<ResponseObject> findAll(@RequestParam(name = "offset") Integer offset,
                                                  @RequestParam(name = "limit") Integer limit,
                                                  @RequestParam(name = "sortBy") String sortBy) {
        Page<Category> categoryList = categoryService.findAllCategories(offset, limit, sortBy);
        Map<String, Object> response = new HashMap<>();
        response.put("categories", categoryList);
        response.put("currentPage", categoryList.getNumber());
        response.put("totalItems", categoryList.getTotalElements());
        response.put("totalPages", categoryList.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!", response)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> searchWithFilter(@RequestParam(name = "offset") Integer offset,
                                                           @RequestParam(name = "limit") Integer limit,
                                                           @RequestParam(name = "sortBy") String sortBy,
                                                           @RequestBody String name) {
        try {
            Page<Category> categoryList = categoryService.searchWithFilter(offset, limit, sortBy, name);
            Map<String, Object> response = new HashMap<>();
            response.put("categories", categoryList);
            response.put("currentPage", categoryList.getNumber());
            response.put("totalItems", categoryList.getTotalElements());
            response.put("totalPages", categoryList.getTotalPages());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Successfully!", response)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", "Error!", e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Consumes("multipart/form-data")
    public ResponseEntity<ResponseObject> save(@RequestParam("name") String name,
                                               @RequestParam("code") String code,
                                               @RequestParam("file") MultipartFile[] files) {
        try {
            StringBuilder imageUrl = new StringBuilder();
            for (MultipartFile file : files) {
                String url = amazonClient.uploadFile(file);
                imageUrl.append(url + ","); // ngan cach cac imageUrl bang dau phay
            }

            Category category = new Category(name, code, imageUrl.toString());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Save Successfully!", categoryService.save(category))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("FAILED", "Error!", e.getMessage())
            );
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Consumes("multipart/form-data")
    public ResponseEntity<ResponseObject> updateCategory(@RequestParam("name") String name,
                                                         @RequestParam("code") String code,
                                                         @RequestParam("file") MultipartFile[] files,
                                                         @PathVariable Long id) {
            StringBuilder imageUrl = new StringBuilder();
            for (MultipartFile file : files) {
                String url = amazonClient.uploadFile(file);
                imageUrl.append(url + ","); // ngan cach cac imageUrl bang dau phay
            }

            Category newCategory = new Category(name, code, imageUrl.toString());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update Successfully!", categoryService.update(newCategory, id))
        );
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> deleteCategory(@RequestParam Long[] ids) {
        for(Long id : ids){
            if (categoryService.findCategoryById(id) != null) {
                categoryService.delete(id);
            }
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
//                        new ResponseObject("Failed", "Category Not Found!", "")
//                );
//            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Delete Successfully!", "")
        );
    }
}
