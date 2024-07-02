package com.auth.wow.libre.domain.model;

import java.util.List;

public class ProductCategory {
    public final Long id;
    public final  String name;
    public final String description;
    public final String image;
    public final List<Product> products;

    public ProductCategory(Long id, String name, String description, String image, List<Product> products) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.products = products;
    }
}
