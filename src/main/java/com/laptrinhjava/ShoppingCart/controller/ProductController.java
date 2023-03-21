package com.laptrinhjava.ShoppingCart.controller;

import com.amazonaws.services.apigateway.model.Op;
import com.laptrinhjava.ShoppingCart.entity.*;
import com.laptrinhjava.ShoppingCart.payload.TypeOptionsDetail;
import com.laptrinhjava.ShoppingCart.payload.request.OptionsDetailRequest;
import com.laptrinhjava.ShoppingCart.payload.request.OptionsRequest;
import com.laptrinhjava.ShoppingCart.payload.TypeOptions;
import com.laptrinhjava.ShoppingCart.payload.response.OptionDetailResponse;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.service.IAmazonClient;
import com.laptrinhjava.ShoppingCart.service.ICategoryService;
import com.laptrinhjava.ShoppingCart.service.impl.productServiceImpl.ProductVariantsService;
import com.laptrinhjava.ShoppingCart.service.productService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Consumes;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Method: Find All Product
     **/
//    @GetMapping()
//    public List<Products> findAll() {
//        return productService.fillAll();
//    }

    /**
     * Method: Save Product
     **/

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Consumes("multipart/form-data")
    public ResponseEntity<ResponseObject> save(@RequestParam("name") String name,
                                               @RequestParam("file") MultipartFile[] files,
                                               @RequestParam("categoryId") Long categoryId,
                                               @RequestParam("description") String description) {
        StringBuffer imageUrl = new StringBuffer();
        for (MultipartFile file : files) {
            String url = amazonClient.uploadFile(file);
            imageUrl.append(url + ","); // ngan cach cac imageUrl bang dau phay
        }

        Category category = categoryService.findCategoryById(categoryId);

        if (category != null) {
            Products product = new Products(name, imageUrl.toString(), category, description);


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
            List<String> listSize = null;
            List<String> listColor = null;
            ProductOptions productOptionsSize = null;
            ProductOptions productOptionsColor = null;
            Options optionSize = null;
            Options optionColor = null;
            Products product = productService.findProductById(optionsRequests.getProductId());

            for (TypeOptions typeOption : optionsRequests.getOptions()) {
                if (typeOption.getKey().equals("size")) {
                    optionSize = optionsService.findByName(typeOption.getKey().toLowerCase());
                    productOptionsSize = new ProductOptions();
                    ProductOptionsKey productOptionsKey = new ProductOptionsKey(product.getId(), optionSize.getId());
                    productOptionsSize.setId(productOptionsKey);
                    productOptionsService.save(productOptionsSize);
                    listSize = typeOption.getValues();
                } else if (typeOption.getKey().equals("color")) {
                    optionColor = optionsService.findByName(typeOption.getKey().toLowerCase());
                    productOptionsColor = new ProductOptions();
                    ProductOptionsKey productOptionsKey = new ProductOptionsKey(product.getId(), optionColor.getId());
                    productOptionsColor.setId(productOptionsKey);
                    productOptionsService.save(productOptionsColor);
                    listColor = typeOption.getValues();
                }
            }
            assert listColor != null;
            for (String color : listColor) {
                OptionValues optionValuesColor = optionValuesService.findByName(color);
                assert listSize != null;
                for (String size : listSize) {
                    String skuId = getSkuId(optionsRequests.getProductId(), size, color);
                    ProductVariants productVariants = new ProductVariants(skuId, null, product);
                    productVariantsService.save(productVariants);

                    OptionValues optionValuesSize = optionValuesService.findByName(size);
                    VariantValues variantValuesSize = new VariantValues();
                    VariantValuesKey variantValuesKeySize = new VariantValuesKey(productOptionsSize.getId().getProductId(),
                            productVariants.getId(), productOptionsSize.getId().getOptionId(), optionValuesSize.getId());
                    variantValuesSize.setId(variantValuesKeySize);
                    variantValuesService.save(variantValuesSize);

                    VariantValues variantValuesColor = new VariantValues();
                    VariantValuesKey variantValuesKeyColor = new VariantValuesKey(productOptionsColor.getId().getProductId(),
                            productVariants.getId(), productOptionsColor.getId().getOptionId(), optionValuesColor.getId());
                    variantValuesColor.setId(variantValuesKeyColor);
                    variantValuesService.save(variantValuesColor);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Successfully!", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", "Error!", e.getMessage())
            );
        }
    }

    public String getSkuId(Long productId, String size, String color) {
        StringBuilder skuId = new StringBuilder();
        int sizeIndex1 = size.toUpperCase().charAt(0);
        int colorIndex1 = color.toUpperCase().charAt(0);
        skuId.append("P").append(productId).append("S").append((char) sizeIndex1).append("C").append((char) colorIndex1);
        return skuId.toString();

    }

    @GetMapping("/options")
    public ResponseEntity<ResponseObject> findOptionByProductId(@RequestParam("productId") Long productId) {
        Products product = productService.findProductById(productId);
        List<TypeOptionsDetail> typeOptionsDetailList = new ArrayList<>();
        List<ProductVariants> productVariants = productVariantsService.findByProducts_Id(product.getId());
        for (ProductVariants productVariant : productVariants) {
            TypeOptionsDetail typeOptionsDetail = new TypeOptionsDetail();
            typeOptionsDetail.setPrice(productVariant.getPrice());
            List<VariantValues> variantValues = variantValuesService.findById_VariantId(productVariant.getId());
            for (VariantValues variantValue : variantValues) {
                OptionValues optionValues = optionValuesService.findByIdAndOption_Id(variantValue.getId().getValueId()
                        , variantValue.getId().getOptionId());
                Options options = optionsService.findById(variantValue.getId().getOptionId());
                if (options.getName().equals("size")) {
                    typeOptionsDetail.setSize(optionValues.getName());
                } else
                    typeOptionsDetail.setColor(optionValues.getName());

            }
            typeOptionsDetailList.add(typeOptionsDetail);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!", typeOptionsDetailList)
        );
    }

    @PutMapping("/options")
    public ResponseEntity<ResponseObject> updatePriceProduct(@RequestParam("productId") Long productId,
                                                             @RequestBody OptionsDetailRequest optionsDetails) {
        try {
            Products product = productService.findProductById(productId);
            if (product != null) {
                for (TypeOptionsDetail typeOptionsDetail : optionsDetails.getOptions()) {
                    String skuId = getSkuId(productId, typeOptionsDetail.getSize(), typeOptionsDetail.getColor());
                    ProductVariants productVariant = productVariantsService.findBySkuId(skuId);
                    if (productVariant != null) {
                        productVariant.setPrice(typeOptionsDetail.getPrice());
                        productVariant.setProducts(productVariant.getProducts());
                        productVariant.setSkuId(productVariant.getSkuId());
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

//    @PostMapping("/options-detail")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Consumes("multipart/form-data")
//    public ResponseEntity<ResponseObject> addOptionsDetail(@RequestBody OptionsDetailRequest optionsRequest) {
//        try {
//            Products product = productService.findProductById(optionsRequest.getProductId());
//            productOptionsService.save(new ProductOptions(product, optionsService.findByName("size")));
//            productOptionsService.save(new ProductOptions(product, optionsService.findByName("color")));
//            // get key and values for size and color
//            for (TypeOptionsDetail typeOption : optionsRequest.getOptions()) {
//                // tao skuId cho product variant
//                String skuId = getSkuId(optionsRequest.getProductId(), typeOption.getSize(), typeOption.getColor());
//                ProductVariants productVariants = new ProductVariants(skuId, typeOption.getPrice(), product);
//                productVariantsService.save(productVariants);
//                OptionValues optionValuesSize = optionValuesService.findByName(typeOption.getSize());
//                OptionValues optionValuesColor = optionValuesService.findByName(typeOption.getColor());

//                if (typeOption.getKey().equals("size")) {
//                    ProductVariants productVariants = new ProductVariants(typeOption.getSkuId(), optionsRequest.getPrice(), product);
//                    productVariantsService.save(productVariants);
//
//                    Options getOption = optionsService.findByName("size");
//                    ProductOptions productOptions = new ProductOptions(product, getOption);
//                    productOptionsService.save(productOptions);
//                    // save optionValue
//                    for (String optionValue : typeOption.getValues()) {
//                        OptionValues optionValues = optionValuesService.findByName(optionValue);
//                        VariantValues variantValues = new VariantValues(productOptions, optionValues, productVariants);
//                        variantValuesService.save(variantValues);
//                    }
//                } else {
//                    ProductVariants productVariants = new ProductVariants(typeOption.getSkuId(), optionsRequest.getPrice(), product);
//                    productVariantsService.save(productVariants);
//                    Options getOption = optionsService.findByName("color");
//                    // tim option de luu productoption
//                    ProductOptions productOptions = new ProductOptions(product, getOption);
//                    productOptionsService.save(productOptions);
//                    // save optionValue
//                    for (String optionValue : typeOption.getValues()) {
//                        OptionValues optionValues = optionValuesService.findByName(optionValue);
//                        VariantValues variantValues = new VariantValues(productOptions, optionValues, productVariants);
//                        variantValuesService.save(variantValues);
//                    }
//                }

//                return ResponseEntity.status(HttpStatus.OK).body(
//                        new ResponseObject("OK", "Add options SuccessFully!", null)
//                );
//            }
//        } catch (
//                Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    new ResponseObject("Failed", "Error!", e.getMessage()));
//        }
//        return null;
//    }


//    @PreAuthorize("hasRole('ADMIN')")
//    @Consumes("multipart/form-data")
//    public ResponseEntity<ResponseObject> addOptions(@RequestParam("name") String name,
//                                               @RequestParam("file") MultipartFile[] files,
//                                               @RequestParam("categoryId") Long categoryId,
//                                               @RequestParam("price") Long price,
//                                               @RequestParam("skuId") String skuId,
//                                               @RequestParam(name = "options", required = false) Options[] options) {
//        try {
//
//
//            StringBuffer imageUrl = new StringBuffer();
//            for (MultipartFile file : files) {
//                String url = amazonClient.uploadFile(file);
//                imageUrl.append(url + ","); // ngan cach cac imageUrl bang dau phay
//            }
//
//            Category category = categoryService.findCategoryById(categoryId);
//
//            if (category != null) {
//                Products product = new Products(name, imageUrl.toString(), category);
//                productService.save(product);
//
//                ProductVariants productVariants = new ProductVariants(skuId, price, product);
//                productVariantsService.save(productVariants);
//
//
//                // get key and values for size and color
//                for (Options option : options) {
//                    com.laptrinhjava.ShoppingCart.entity.Options getOption = optionsService.findByName(option.getKey());
//
//                    ProductOptions productOptions = new ProductOptions(product, getOption);
//                    productOptionsService.save(productOptions);
//                    OptionValues optionValues = optionValuesService.findByName(option.getValues());
//                    VariantValues variantValues = new VariantValues(productOptions, optionValues, productVariants);
//                    variantValuesService.save(variantValues);
//
//                }
//
//                return ResponseEntity.status(HttpStatus.OK).body(
//                        new ResponseObject("OK", "Save Successfully!", null)
//                );
//            } else
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                        new ResponseObject("Failed", "Category not exists!", null)
//                );
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    new ResponseObject("Failed", "Error!", e.getMessage()));
//        }
//    }

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
