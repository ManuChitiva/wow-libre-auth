package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ProductDto {
  @NotNull
  @NotEmpty
  public String name;
  @NotNull
  @NotEmpty
  public String description;
  @NotNull
  @NotEmpty
  public String image;
  @NotNull
  public Double price;
  @NotNull
  public Double discount;
  @NotNull
  @NotEmpty
  public String itemCode;
}
