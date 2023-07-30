package com.sourav.springsecuritydemo.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.sourav.springsecuritydemo.entity.UserInfo;
import com.sourav.springsecuritydemo.repository.UserInforRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sourav.springsecuritydemo.dto.Product;

import jakarta.annotation.PostConstruct;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductService {

    private static final String USER_SAVED = "User added to the system";
    List<Product> productList = null;
    @Autowired
    private UserInforRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void loadProductsFromDB() {

        productList = IntStream.rangeClosed(1, 100).mapToObj(i -> Product.builder()
                .productId(i)
                .name("product " + i)
                .qty(new Random().nextInt(10))
                .price(new Random().nextInt(5000)).build()).collect(Collectors.toList());
    }

    public List<Product> getProducts() {
        return productList;
    }

    public Product getProduct(int id) {
        return productList.stream()
                .filter(product -> product.getProductId() == id)
                .findAny()
                .orElseThrow(() -> new RuntimeException("product" + id + " not found"));
    }

    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return USER_SAVED;
    }
}
