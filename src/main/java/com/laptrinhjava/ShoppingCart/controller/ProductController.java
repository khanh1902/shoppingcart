package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.*;
import com.laptrinhjava.ShoppingCart.payload.TypeOptionsDetail;
import com.laptrinhjava.ShoppingCart.payload.request.OptionsDetailRequest;
import com.laptrinhjava.ShoppingCart.payload.request.OptionsRequest;
import com.laptrinhjava.ShoppingCart.payload.TypeOptions;
import com.laptrinhjava.ShoppingCart.payload.response.OneProductResponse;
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
import java.util.*;

import static java.lang.Long.parseLong;

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

    @GetMapping("/getOne")
    public ResponseEntity<ResponseObject> getOneProduct(@RequestParam(name = "id") Long productId) {
        Products product = productService.findProductById(productId);
        if (product != null) {
            List<Map<String, Object>> optionList = new ArrayList<>();
            OneProductResponse productResponse = new OneProductResponse();
            productResponse.setId(product.getId());
            productResponse.setDescription(product.getDescription());
            productResponse.setImageUrl(product.getImageUrl());
            productResponse.setName(product.getName());
            productResponse.setPrice(product.getPrice());
            productResponse.setQuantity(product.getQuantity());

            List<ProductVariants> productVariants = productVariantsService.findByProducts_Id(product.getId());
            for (ProductVariants productVariant : productVariants) {
                Map<String, Object> optionMap = new HashMap<>();

                if (productVariant.getPrice() == null) optionMap.put("price", null);
                else optionMap.put("price", productVariant.getPrice());

                if (productVariant.getQuantity() == null) optionMap.put("quantity", null);
                else optionMap.put("quantity", productVariant.getQuantity());

                List<VariantValues> variantValues = variantValuesService.findById_VariantId(productVariant.getId());
                for (VariantValues variantValue : variantValues) {
                    OptionValues optionValues = optionValuesService.findByIdAndOption_Id(variantValue.getId().getValueId()
                            , variantValue.getId().getOptionId());
                    Options options = optionsService.findById(variantValue.getId().getOptionId());
                    optionMap.put(options.getName().toLowerCase(), optionValues.getName());
                }
                optionList.add(optionMap);
            }
            productResponse.setOptions(optionList);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Successfully!", productResponse)
            );
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("OK", "Product is not exists!", null)
            );
    }

    /**
     * Method: Save Product
     **/
    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
    @Consumes("multipart/form-data")
    public ResponseEntity<ResponseObject> save(@RequestParam(name = "name") String name,
                                               @RequestParam(name = "file") MultipartFile[] files,
                                               @RequestParam(name = "categoryId") Long categoryId,
                                               @RequestParam(name = "description") String description,
                                               @RequestParam(name = "price", required = false) Long price,
                                               @RequestParam(name = "quantity", required = false) Long quantity) {
        StringBuilder imageUrl = new StringBuilder();
        for (MultipartFile file : files) {
            String url = amazonClient.uploadFile(file);
            imageUrl.append(url).append(","); // ngan cach cac imageUrl bang dau phay
        }

        Category category = categoryService.findCategoryById(categoryId);
        String email = getUsername();
        Users user = userService.findByEmail(email);

        if (category != null) {
            Products product = null;
            if (quantity == null) {
                product = new Products(name, imageUrl.toString(), category, description, user, price, null);
            } else {
                product = new Products(name, imageUrl.toString(), category, description, user, price, quantity);
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Save Successfully!", productService.save(product))
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed", "Category not exists!", null)
            );
        }
    }

    /**
     * Method: Add Options for Product
     **/
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
                List<VariantValuesKey> variantValuesKeys = new ArrayList<>();
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
                    if (findOptionValues == null) {
                        findOptionValues = new OptionValues(entry.getValue(), findOptions);
                        optionValuesService.save(findOptionValues);
                    }
                    skuId.append(entry.getValue().toUpperCase());
                    valueIds.add(findOptionValues.getId());
                    VariantValuesKey variantValuesKey = new VariantValuesKey();
                    variantValuesKey.setProductId(product.getId());
                    variantValuesKey.setOptionId(findOptions.getId());
                    variantValuesKey.setValueId(findOptionValues.getId());
                    variantValuesKeys.add(variantValuesKey);
                }
                boolean flag = true;
                List<ProductVariants> productVariantsList = productVariantsService.findByProducts_Id(product.getId());
                for (ProductVariants productVariant : productVariantsList) {
                    if (productVariant.getSkuId().equals(skuId.toString())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    ProductVariants productVariants = new ProductVariants(skuId.toString(), product.getPrice(), product, null);
                    productVariantsService.save(productVariants);
                    for (VariantValuesKey variantValuesKey : variantValuesKeys) {
                        VariantValues variantValue = new VariantValues();
                        variantValuesKey.setVariantId(productVariants.getId());
                        variantValue.setId(variantValuesKey);
                        variantValuesService.save(variantValue);
                    }
                }
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

    /**
     * Method: Find options Product By product id
     **/
    @GetMapping("/options")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> findOptionByProductId(@RequestParam(name = "productId") Long productId) {
        Products product = productService.findProductById(productId);
        List<Map<String, Object>> optionList = new ArrayList<>();
        List<ProductVariants> productVariants = productVariantsService.findByProducts_Id(product.getId());
        for (ProductVariants productVariant : productVariants) {
            Map<String, Object> optionMap = new HashMap<>();

            List<VariantValues> variantValues = variantValuesService.findById_VariantId(productVariant.getId());
            for (VariantValues variantValue : variantValues) {
                OptionValues optionValues = optionValuesService.findByIdAndOption_Id(variantValue.getId().getValueId()
                        , variantValue.getId().getOptionId());
                Options options = optionsService.findById(variantValue.getId().getOptionId());
                optionMap.put(options.getName().toLowerCase(), optionValues.getName());
            }

            if (productVariant.getPrice() == null) optionMap.put("price", null);
            else optionMap.put("price", productVariant.getPrice());

            if (productVariant.getQuantity() == null) optionMap.put("quantity", null);
            else optionMap.put("quantity", productVariant.getQuantity());
            optionMap.entrySet().stream().sorted();
            optionList.add(optionMap);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!", optionList)
        );
    }

    /**
     * Method: Update Price and Quantity for options product by product id
     **/
    @PutMapping("/options")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> updatePriceProduct(@RequestParam(name = "productId") Long productId,
                                                             @RequestBody List<Map<String, String>> options) {
        try {
            Products product = productService.findProductById(productId);
            Long totalQuantity = 0L;
            for (Map<String, String> option : options) {
                StringBuilder skuId = new StringBuilder();
                skuId.append("P").append(product.getId());
                for (Map.Entry<String, String> entry : option.entrySet()) {
                    if (!Objects.equals(entry.getKey(), "price") && !Objects.equals(entry.getKey(), "quantity")) {
                        skuId.append(entry.getKey().toUpperCase().charAt(0)).append(entry.getValue().toUpperCase());
                    }
                }
                ProductVariants productVariant = productVariantsService.findBySkuId(skuId.toString());
                productVariant.setProducts(productVariant.getProducts());
                productVariant.setSkuId(productVariant.getSkuId());
                // set price và quantity
                for (Map.Entry<String, String> entry : option.entrySet()) {
                    if (entry.getKey().equals("price")) {
                        productVariant.setPrice(parseLong(entry.getValue()));
                    } else if (entry.getKey().equals("quantity")) {
                        productVariant.setQuantity(parseLong(entry.getValue()));
                    }
                }
                totalQuantity += productVariant.getQuantity();
                productVariantsService.save(productVariant);
            }

            // lưu lại quantity cho product từ các options
            product = new Products(product.getName(), product.getImageUrl(), product.getCategory(),
                    product.getDescription(), product.getUsers(), product.getPrice(), totalQuantity);
            productService.save(product);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Save Successfully!", null)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", "Error!", e.getMessage())
            );
        }
    }

    /**
     * Method: Delete Product
     **/
    @DeleteMapping()
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> delete(@RequestParam(name = "productIds", required = false) List<Long> ids) {
        try {
            for (Long id : ids) {
                Products product = productService.findProductById(id);
                if (product != null) {
                    // delete variant values if exists
                    List<VariantValues> variantValuesList = variantValuesService.findById_ProductId(product.getId());
                    if (variantValuesList != null) {
                        for (VariantValues variantValue : variantValuesList) {
                            variantValuesService.deleteById_ProductId(variantValue.getId().getProductId());
                        }
                    }

                    // delete product variant if exists
                    List<ProductVariants> productVariantsList = productVariantsService.findByProducts_Id(product.getId());
                    if (productVariantsList != null) {
                        for (ProductVariants productVariant : productVariantsList) {
                            productVariantsService.deleteById(productVariant.getId());
                        }
                    }

                    // delete product options if exists
                    List<ProductOptions> productOptionsList = productOptionsService.findByProducts_Id(product.getId());
                    if (productOptionsList != null) {
                        for (ProductOptions productOption : productOptionsList) {
                            productOptionsService.deleteById_ProductId(productOption.getId().getProductId());
                        }
                    }

                    // delete image on aws
                    amazonClient.deleteFile(product.getImageUrl());

                    // delete product
                    productService.delete(product.getId());
                } else
                    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                            new ResponseObject("FAILED", "Product not exists!", null)
                    );
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Delete successfully!", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", "Error!", e.getMessage())
            );
        }
    }

    /**
     * Method: Delete product option by product id
     **/
    @DeleteMapping("/options/delete")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> deleteProductOption(@RequestParam("productId") Long productId,
                                                              @RequestBody List<Map<String, Object>> options) {
        try {
            Products findProduct = productService.findProductById(productId);
            if (findProduct != null) {
                for (Map<String, Object> option : options) {
                    StringBuilder skuId = new StringBuilder();
                    skuId.append("P").append(findProduct.getId());
                    for (Map.Entry<String, Object> entry : option.entrySet()) {
                        skuId.append(entry.getKey().toUpperCase().charAt(0)).append(entry.getValue());
                    }
                    List<ProductVariants> productVariantsList = productVariantsService.findByProducts_Id(findProduct.getId());
                    for (ProductVariants productVariant : productVariantsList) {
                        if (productVariant.getSkuId().equals(skuId.toString())) {
                            List<VariantValues> variantValuesList = variantValuesService.findById_VariantId(productVariant.getId());
                            for (VariantValues variantValue : variantValuesList) {
                                variantValuesService.deleteById_VariantId(variantValue.getId().getVariantId());
                            }
                            productVariantsService.deleteById(productVariant.getId());
                        }
                        return ResponseEntity.status(HttpStatus.OK).body(
                                new ResponseObject("OK", "Delete successfully!", null)
                        );
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed", "Product not exist!", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", "Error!", e.getMessage())
            );
        }
    }
}

