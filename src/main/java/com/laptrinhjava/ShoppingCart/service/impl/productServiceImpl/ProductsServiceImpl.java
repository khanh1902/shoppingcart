package com.laptrinhjava.ShoppingCart.service.impl.productServiceImpl;

import com.laptrinhjava.ShoppingCart.entity.ProductVariants;
import com.laptrinhjava.ShoppingCart.entity.Products;
import com.laptrinhjava.ShoppingCart.payload.request.OptionsRequest;
import com.laptrinhjava.ShoppingCart.payload.response.ProductResponse;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IProductRepository;
import com.laptrinhjava.ShoppingCart.service.productService.IProductService;
import com.laptrinhjava.ShoppingCart.service.productService.IProductVariantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductsServiceImpl implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IProductVariantsService productVariantsService;


    @Override
    public Products save(Products product) {
        return productRepository.save(product);
    }

    @Override
    public Products findProductById(Long id) {
        return productRepository.findProductById(id);
    }

    @Override
    public Products findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Products> findByCategory_Id(Long categoryId) {
        return productRepository.findByCategory_Id(categoryId);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Products update(Products newProduct, Long id) {
        return productRepository.findById(id).map(
                product -> {
                    product.setName(newProduct.getName());
                    product.setImageUrl(newProduct.getImageUrl());
                    product.setCreatedDate(new Date());
                    return productRepository.save(product);
                }
        ).orElseGet(() -> {
            newProduct.setId(id);
            return productRepository.save(newProduct);
        });
    }

    @Override
    public Page<Products> findWithFilterAndPageAndSort(Integer offset, Integer limit, String sortBy, String name) {
        Pageable paging = PageRequest.of(offset, limit, Sort.by(sortBy).descending());

        Page<Products> pageProduct = null;
        if (name == null) {
            pageProduct = productRepository.findAll(paging);
        } else {
            pageProduct = productRepository.findByNameContainingIgnoreCase(name, paging);
        }
//        List<ProductResponse> productResponses = covertProductsToProductResponse(pageProduct);
//        return new PageImpl<>(productResponses, paging, pageProduct.getTotalElements());
        return pageProduct;
    }

    @Override
    public List<Map<String, String>> convertOptionsRequestToMap(OptionsRequest optionsRequests) {
        List<Map<String, String>> options = new ArrayList<>();
        for (int i = 0; i < optionsRequests.getOptions().size(); i++) {
            for (String iValue : optionsRequests.getOptions().get(i).getValues()) {
                if (optionsRequests.getOptions().size() > 1) {
                    for (int j = i + 1; j < optionsRequests.getOptions().size(); j++) {
                        for (String jValue : optionsRequests.getOptions().get(j).getValues()) {
                            Map<String, String> mapOption = new HashMap<>();
                            mapOption.put(optionsRequests.getOptions().get(i).getKey(), iValue);
                            mapOption.put(optionsRequests.getOptions().get(j).getKey(), jValue);
                            options.add(mapOption);
                        }
                    }
                } else {
                    Map<String, String> mapOption = new HashMap<>();
                    mapOption.put(optionsRequests.getOptions().get(i).getKey(), iValue);
                    mapOption.put(optionsRequests.getOptions().get(i).getKey(), iValue);
                    options.add(mapOption);
                }
            }
        }
        return options;
    }

    @Override
    public List<Products> findByNameContainingIgnoreCase(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Page<Products> filterWithPaging(Integer offset, Integer limit, String sortBy, Boolean asc, String name,
                                           List<Long> categoryIds, Long minPrice, Long maxPrice) {

        PageRequest pageRequest = null;
        if (asc) pageRequest = PageRequest.of(offset, limit, Sort.by(sortBy).ascending());
        else pageRequest = PageRequest.of(offset, limit, Sort.by(sortBy).descending());
        Page<Products> pageProduct = null;
        if (name == null) {
            pageProduct = productRepository.findAll(pageRequest);
        } else {
            pageProduct = productRepository.findByNameContainingIgnoreCase(name, pageRequest);
        }
        return filer(categoryIds, minPrice, maxPrice, pageProduct, pageRequest);
    }

    private Page<Products> filer(List<Long> categoryIds, Long minPrice, Long maxPrice, Page<Products> pageProduct, Pageable paging) {
        List<Products> filterWithCategory = new ArrayList<>();
        List<Products> filerWithPrice = new ArrayList<>();
        List<Products> products = new ArrayList<>(pageProduct.getContent());

        if (categoryIds != null) {
            for (Products product : products) {
                for (Long categoryId : categoryIds) {
                    if (product.getCategory().getId().equals(categoryId)) {
                        filterWithCategory.add(product);
                    }
                }
            }
        } else filterWithCategory.addAll(products);

        if (minPrice != null && maxPrice != null) {
            for (Products product : filterWithCategory) {
                if (product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                    filerWithPrice.add(product);
            }
        } else if (maxPrice != null) {
            for (Products product : filterWithCategory) {
                if (product.getPrice() <= maxPrice)
                    filerWithPrice.add(product);
            }
        } else if (minPrice == null) filerWithPrice.addAll(filterWithCategory);

        int start = (int) paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), filerWithPrice.size());
        return new PageImpl<Products>(filerWithPrice.subList(start, end), paging, pageProduct.getTotalElements());
//        return new PageImpl<>(filerWithPrice, paging, filerWithPrice.size());
    }

    public List<ProductResponse> covertProductsToProductResponse(Page<Products> pageProduct) {
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Products product : pageProduct) {
            List<ProductVariants> productVariants = productVariantsService.findByProducts_Id(product.getId());
            if (!productVariants.isEmpty()) {
                Double minPrice = productVariants.get(0).getPrice();
                for (int i = 1; i < productVariants.size(); i++) {
                    if (minPrice >= productVariants.get(i).getPrice()) minPrice = productVariants.get(i).getPrice();
                }
                ProductResponse productResponse = new ProductResponse(product.getId(), product.getName()
                        , product.getImageUrl(), minPrice);
                productResponses.add(productResponse);
            } else {
                ProductResponse productResponse = new ProductResponse(product.getId(), product.getName()
                        , product.getImageUrl(), product.getPrice());
                productResponses.add(productResponse);
            }
        }
        return productResponses;
    }
}
