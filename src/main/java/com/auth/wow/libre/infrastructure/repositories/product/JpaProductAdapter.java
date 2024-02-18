package com.auth.wow.libre.infrastructure.repositories.product;

import com.auth.wow.libre.domain.ports.out.product.LoadProductPort;
import com.auth.wow.libre.infrastructure.entities.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JpaProductAdapter implements LoadProductPort {

  private final ProductRepository productRepository;

  public JpaProductAdapter(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }


  @Override
  public List<ProductEntity> findAll(String transactionId) {
    return productRepository.findByStatusIsTrue();
  }
}