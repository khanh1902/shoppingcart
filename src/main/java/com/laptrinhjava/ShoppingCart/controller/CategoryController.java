package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.Category;
import com.laptrinhjava.ShoppingCart.entity.Products;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.reponsitory.ICategoryRepository;
import com.laptrinhjava.ShoppingCart.service.IAmazonClient;
import com.laptrinhjava.ShoppingCart.service.ICategoryService;
import com.laptrinhjava.ShoppingCart.service.productService.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Consumes;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ICategoryRepository categoryRepository;

    @Qualifier("amazonClientImpl")
    @Autowired
    private IAmazonClient amazonClient;

    @Qualifier("productsServiceImpl")
    @Autowired
    private IProductService productService;


    @GetMapping("/all")
    public ResponseEntity<ResponseObject> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!", categoryRepository.findAll())
        );
    }

    /**
     * Method: Find All Category with sort and paging
     **/
//    @GetMapping
//    public ResponseEntity<ResponseObject> findWithPageAndSort(@RequestParam(required = false, name = "offset", defaultValue = "0") Integer offset,
//                                                              @RequestParam(required = false, defaultValue = "100", name = "limit") Integer limit,
//                                                              @RequestParam(required = false, name = "sortBy", defaultValue = "id") String sortBy) {
//        Page<Category> categoryList = categoryService.findAllCategories(offset, limit, sortBy);
//        Map<String, Object> response = new HashMap<>();
//        response.put("categories", categoryList);
//        response.put("currentPage", categoryList.getNumber());
//        response.put("totalItems", categoryList.getTotalElements());
//        response.put("totalPages", categoryList.getTotalPages());
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("OK", "Successfully!", response)
//        );
//    }
    @GetMapping
    public ResponseEntity<ResponseObject> searchWithFilter(@RequestParam(required = false, name = "offset", defaultValue = "0") Integer offset,
                                                           @RequestParam(required = false, name = "limit", defaultValue = "5") Integer limit,
                                                           @RequestParam(required = false, name = "sortBy", defaultValue = "id") String sortBy,
                                                           @RequestParam(required = false, name = "name") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!",
                        categoryService.findWithFilterAndPageAndSort(offset, limit, sortBy, name)));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseObject> filter(@RequestParam(name = "name") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!", categoryRepository.findByNameLike(name.toLowerCase()))
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Consumes("multipart/form-data")
    public ResponseEntity<ResponseObject> save(@RequestParam(required = false, name = "name") String name,
                                               @RequestParam(required = false, name = "code") String code,
                                               @RequestParam(required = false, name = "file") MultipartFile[] files) {
        try {
            StringBuilder imageUrl = new StringBuilder();
            for (MultipartFile file : files) {
                String url = amazonClient.uploadFile(file);
                imageUrl.append(url).append(","); // ngan cach cac imageUrl bang dau phay
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
    public ResponseEntity<ResponseObject> updateCategory(@RequestParam(required = false, name = "name") String name,
                                                         @RequestParam(required = false, name = "code") String code,
                                                         @RequestParam(required = false, name = "file") Optional<MultipartFile[]> files,
                                                         @PathVariable Long id) {
        StringBuilder imageUrl = new StringBuilder();
        for (MultipartFile file : files.orElse(new MultipartFile[0])) {
            String url = amazonClient.uploadFile(file);
            imageUrl.append(url).append(" "); // ngan cach cac imageUrl bang dau phay
        }

        Category newCategory = new Category(name, code, imageUrl.toString());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update Successfully!", categoryService.update(newCategory, id))
        );
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> delete(@RequestParam(name = "ids") List<Long> ids) {
        try {
            for (Long id : ids) {
                Category findCategory = categoryService.findCategoryById(id);
                if (findCategory != null) {
                    List<Products> findProductsByCategoryId = productService.findByCategory_Id(findCategory.getId());
                    if (!findProductsByCategoryId.isEmpty())
                        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                                new ResponseObject("Failed", "Can not remove " + findCategory.getName() +
                                        " because product exists in this!", null)
                        );

                    else {
                        amazonClient.deleteFile(findCategory.getImageUrl());
                        categoryService.delete(findCategory.getId());
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Delete Successfully!", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("FAILED", "Error!", e.getMessage())
            );
        }
    }
}
