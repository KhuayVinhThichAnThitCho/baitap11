package com.example.demo3.service;

import com.example.demo3.entity.Product;
import java.util.List;

public interface ProductServices {
    void delete(Long id);
    Product get(Long id);
    Product save(Product product);
    List<Product> listAll();
}
