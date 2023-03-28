package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.*;
import com.laptrinhjava.ShoppingCart.payload.TypeOptionsDetail;
import com.laptrinhjava.ShoppingCart.payload.request.OptionsDetailRequest;
import com.laptrinhjava.ShoppingCart.payload.request.OptionsRequest;
import com.laptrinhjava.ShoppingCart.payload.TypeOptions;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.service.IAmazonClient;
import com.laptrinhjava.ShoppingCart.service.ICategoryService;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import com.laptrinhjava.ShoppingCart.service.productService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Consumes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IAmazonClient amazonClient;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IOptionValuesService optionValuesService;

    @Autowired
    private IProductOptionsService productOptionsService;

    @Autowired
    private IProductVariantsService productVariantsService;

    @Autowired
    private IVariantValuesService variantValuesService;

    @Autowired
    private IOptionsService optionsService;

    @Autowired
    private IUserService userService;

    /**
     * Method: Find All Product with paging, sort and filter
     **/
    @GetMapping
    public ResponseEntity<ResponseObject> searchWithFilter(@RequestParam(required = false, name = "offset", defaultValue = "0") Integer offset,
                                                           @RequestParam(required = false, name = "limit", defaultValue = "5") Integer limit,
                                                           @RequestParam(required = false, name = "sortBy", defaultValue = "id") String sortBy,
                                                           @RequestParam(required = false, name = "name") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!",
                        productService.findWithFilterAndPageAndSort(offset, limit, sortBy, name)));
    }


    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * Method: Save Product
     **/
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Consumes("multipart/form-data")
    public ResponseEntity<ResponseObject> save(@RequestParam("name") String name,
                                               @RequestParam("file") MultipartFile[] files,
                                               @RequestParam("categoryId") Long categoryId,
                                               @RequestParam("description") String description,
                                               @RequestParam("price") Long price) {
        StringBuilder imageUrl = new StringBuilder();
        for (MultipartFile file : files) {
            String url = amazonClient.uploadFile(file);
            imageUrl.append(url).append(","); // ngan cach cac imageUrl bang dau phay
        }

        Category category = categoryService.findCategoryById(categoryId);
        String email = getUsername();
        Users user = userService.findByEmail(email);

        if (category != null) {
            Products product = new Products(name, imageUrl.toString(), category, description, user, price);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Save Successfully!", productService.save(product))
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed", "Category not exists!", null)
            );
        }
    }

    @PostMapping("/options")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> addOptions(@RequestBody OptionsRequest optionsRequests) {
        try {
            Products product = productService.findProductById(optionsRequests.getProductId());
            List<Map<String, String>> options = productService.convertOptionsRequestToMap(optionsRequests);
            for (Map<String, String> option : options) {
                StringBuilder skuId = new StringBuilder();
                skuId.append("P").append(product.getId());
                List<Long> valueIds = new ArrayList<>();
                for (Map.Entry<String, String> entry : option.entrySet()) {
                    Options findOptions = optionsService.findByName(entry.getKey().toUpperCase());
                    // Nếu option k tồn tại thì lưu vào db
                    if (findOptions == null) {
                        findOptions = new Options(entry.getKey().toUpperCase());
                        optionsService.save(findOptions);
                    }
                    skuId.append(entry.getKey().toUpperCase().charAt(0));

                    ProductOptions productOption = new ProductOptions(product, findOptions);
                    ProductOptionsKey productOptionsKey = new ProductOptionsKey(product.getId(), findOptions.getId());
                    productOption.setId(productOptionsKey);
                    productOptionsService.save(productOption);

                    OptionValues findOptionValues = optionValuesService.findByNameAndOption_Id(entry.getValue(), findOptions.getId());
                    // Nếu giá trị của option không tồn tại thì lưu vào db
                    if (findOptionValues == null){
                        findOptionValues = new OptionValues(entry.getValue(), findOptions);
                        optionValuesService.save(findOptionValues);
                    }
                    skuId.append(entry.getValue().toUpperCase());
                    valueIds.add(findOptionValues.getId());
                }
                ProductVariants productVariants = new ProductVariants(skuId.toString(), product.getPrice(), product, null);
                productVariantsService.save(productVariants);

            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Successfully!", null)
            );
        } catch (
                Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", "Error!", e.getMessage())
            );
        }
    }


//    @PostMapping("/options")
////    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<ResponseObject> addOptions(@RequestBody OptionsRequest optionsRequests) {
//        try {
//            List<String> listSize = null;
//            List<String> listColor = null;
//            ProductOptions productOptionsSize = null;
//            ProductOptions productOptionsColor = null;
//            ProductOptions productOptionsOther = null;
//            Options optionSize = null;
//            Options optionColor = null;
//            Products product = productService.findProductById(optionsRequests.getProductId());
//
//            for (TypeOptions typeOption : optionsRequests.getOptions()) {
//                if (typeOption.getKey().toUpperCase().contains("SIZES")) {
//                    optionSize = optionsService.findByName(typeOption.getKey().toUpperCase());
//                    productOptionsSize = new ProductOptions();
//                    ProductOptionsKey productOptionsKey = new ProductOptionsKey(product.getId(), optionSize.getId());
//                    productOptionsSize.setId(productOptionsKey);
//                    productOptionsService.save(productOptionsSize);
//                    listSize = typeOption.getValues();
//                } else if (typeOption.getKey().toUpperCase().contains("COLORS")) {
//                    optionColor = optionsService.findByName(typeOption.getKey().toUpperCase());
//                    productOptionsColor = new ProductOptions();
//                    ProductOptionsKey productOptionsKey = new ProductOptionsKey(product.getId(), optionColor.getId());
//                    productOptionsColor.setId(productOptionsKey);
//                    productOptionsService.save(productOptionsColor);
//                    listColor = typeOption.getValues();
//                }
//
//            }
//            assert listColor != null;
//            for (String color : listColor) {
//                OptionValues optionValuesColor = optionValuesService.findByName(color);
//                // lưu color nếu không có sẳn trong db
//                if (optionValuesColor == null) {
//                    optionValuesColor = new OptionValues(color.toUpperCase(), optionColor);
//                    optionValuesService.save(optionValuesColor);
//                    optionValuesColor = optionValuesService.findByName(color.toUpperCase());
//                }
//                assert listSize != null;
//                for (String size : listSize) {
//                    String skuId = getSkuId(optionsRequests.getProductId(), size, color);
//                    ProductVariants productVariants = new ProductVariants(skuId, null, product, null);
//                    productVariantsService.save(productVariants);
//                    OptionValues optionValuesSize = optionValuesService.findByName(size.toUpperCase());
//                    // lưu size nếu không có sẳn trong db
//                    if (optionValuesSize == null) {
//                        optionValuesSize = new OptionValues(size.toUpperCase(), optionSize);
//                        optionValuesService.save(optionValuesSize);
//                        optionValuesSize = optionValuesService.findByName(size.toUpperCase());
//                    }
//                    VariantValues variantValuesSize = new VariantValues();
//                    VariantValuesKey variantValuesKeySize = new VariantValuesKey(productOptionsSize.getId().getProductId(),
//                            productVariants.getId(), productOptionsSize.getId().getOptionId(), optionValuesSize.getId());
//                    variantValuesSize.setId(variantValuesKeySize);
//                    variantValuesService.save(variantValuesSize);
//
//                    VariantValues variantValuesColor = new VariantValues();
//                    VariantValuesKey variantValuesKeyColor = new VariantValuesKey(productOptionsColor.getId().getProductId(),
//                            productVariants.getId(), productOptionsColor.getId().getOptionId(), optionValuesColor.getId());
//                    variantValuesColor.setId(variantValuesKeyColor);
//                    variantValuesService.save(variantValuesColor);
//                }
////            }
//
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject("OK", "Successfully!", null)
//            );
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
//                    new ResponseObject("Failed", "Error!", e.getMessage())
//            );
//        }
//    }

    public String getSkuId(Long productId, String size, String color) {
        StringBuilder skuId = new StringBuilder();
//        int sizeIndex1 = size.toUpperCase().charAt(0);
//        int colorIndex1 = color.toUpperCase().charAt(0);
        skuId.append("P").append(productId).append("S").append(size.toUpperCase()).append("C").append(color.toUpperCase());
        return skuId.toString();
    }

    @GetMapping("/options")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> findOptionByProductId(@RequestParam("productId") Long productId) {
        Products product = productService.findProductById(productId);
        List<TypeOptionsDetail> typeOptionsDetailList = new ArrayList<>();
        List<ProductVariants> productVariants = productVariantsService.findByProducts_Id(product.getId());
        for (ProductVariants productVariant : productVariants) {
            TypeOptionsDetail typeOptionsDetail = new TypeOptionsDetail();
            typeOptionsDetail.setPrice(productVariant.getPrice());
            typeOptionsDetail.setQuantity(productVariant.getQuantity());
            List<VariantValues> variantValues = variantValuesService.findById_VariantId(productVariant.getId());
            for (VariantValues variantValue : variantValues) {
                OptionValues optionValues = optionValuesService.findByIdAndOption_Id(variantValue.getId().getValueId()
                        , variantValue.getId().getOptionId());
                Options options = optionsService.findById(variantValue.getId().getOptionId());
                if (options.getName().toUpperCase().contains("SIZES")) {
                    typeOptionsDetail.setSize(optionValues.getName().toUpperCase());
                } else
                    typeOptionsDetail.setColor(optionValues.getName().toUpperCase());
            }
            typeOptionsDetailList.add(typeOptionsDetail);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!", typeOptionsDetailList)
        );
    }

    @PutMapping("/options")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> updatePriceProduct(@RequestParam("productId") Long productId,
                                                             @RequestBody OptionsDetailRequest optionsDetails) {
        try {
            Products product = productService.findProductById(productId);
            if (product != null) {
                for (TypeOptionsDetail typeOptionsDetail : optionsDetails.getOptions()) {
                    String skuId = getSkuId(productId, typeOptionsDetail.getSize().toUpperCase(), typeOptionsDetail.getColor().toUpperCase());
                    ProductVariants productVariant = productVariantsService.findBySkuId(skuId);
                    if (productVariant != null) {
                        productVariant.setPrice(typeOptionsDetail.getPrice());
                        productVariant.setProducts(productVariant.getProducts());
                        productVariant.setSkuId(productVariant.getSkuId());
                        productVariant.setQuantity(typeOptionsDetail.getQuantity());
                        productVariantsService.save(productVariant);
                    }
                }
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Save Successfully!", null)
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("Failed", "Error!", null)
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", "Error!", e.getMessage())
            );
        }
    }

/**
 * Method: Delete Product
 * <p>
 * Method: Update Product
 **/
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<ResponseObject> delete(@PathVariable Long id) {
//        try {
//            Product findById = productService.findProductById(id);
//            if (findById != null) {
//                amazonClient.deleteFile(findById.getImageUrl());
//                productService.delete(id);
//                return ResponseEntity.status(HttpStatus.OK).body(
//                        new ResponseObject("OK", "Delete successfully!", null)
//                );
//            } else
//                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
//                        new ResponseObject("FAILED", "Delete failed!", null)
//                );
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
//                    new ResponseObject("Failed", "Error!", e.getMessage())
//            );
//        }
//
//    }

    /**
     * Method: Update Product
     **/
//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<ResponseObject> updateProduct(@RequestParam("name") String name,
//                                                        @RequestParam("price") Long price,
//                                                        @RequestParam("file") MultipartFile[] files,
//                                                        @RequestParam("categoryId") Long categoryId,
//                                                        @RequestParam("discountId") Long discountId,
//                                                        @PathVariable Long id) {
//        Product findById = productService.findProductById(id);
//        if (findById != null) {
//            amazonClient.deleteFile(findById.getImageUrl());
//        }
//        StringBuffer imageUrl = new StringBuffer();
//        for (MultipartFile file : files) {
//            String url = amazonClient.uploadFile(file);
//            imageUrl.append(url + ", ");
//        }
//
//        Product newProduct = new Product(name, price, imageUrl.toString(), categoryId, discountId);
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("OK", "Update Successfully!", productService.update(newProduct, id))
//        );
//    }

}
