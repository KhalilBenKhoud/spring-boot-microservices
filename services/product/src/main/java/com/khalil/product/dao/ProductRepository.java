package com.khalil.product.dao;

import com.khalil.product.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>
{

    List<Product> findAllByIdInOrderById(List<Integer> productIds);
}
