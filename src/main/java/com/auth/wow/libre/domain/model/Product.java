package com.auth.wow.libre.domain.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Product {
  public Long id;
  public String name;
  public String description;
  public String image;
  public Double price;
  public Double discount;
  public String referenceNumber;
  public String itemCode;
  public String category;
}
