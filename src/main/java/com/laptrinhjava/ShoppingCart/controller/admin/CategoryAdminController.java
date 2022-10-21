package com.laptrinhjava.ShoppingCart.controller.admin;

import com.laptrinhjava.ShoppingCart.entity.Category;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/category")
public class CategoryAdminController {
    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/save")
    public ResponseEntity<ResponseObject> save(@RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Save successfully!", categoryService.save(category))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateCategory(@RequestBody Category newCategory, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update Successfully!", categoryService.update(newCategory, id))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Long id) {
//        Category findCategory = categoryService.findCategoryById(id);
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
