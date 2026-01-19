package com.lbs.blaybus.product.repository;

import com.lbs.blaybus.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
