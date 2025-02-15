package com.khalil.product.services;

import com.khalil.product.dao.ProductRepository;
import com.khalil.product.dto.ProductPurchaseRequest;
import com.khalil.product.dto.ProductPurchaseResponse;
import com.khalil.product.dto.ProductRequest;
import com.khalil.product.dto.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProductRepository repository ;
    private final ProductMapper mapper ;

    public Integer createProduct(ProductRequest request) {
        var product = mapper.toProduct(request) ;
        return repository.save(product).getId() ;
     }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        var productIds = request.stream()
                .map(ProductPurchaseRequest::productId)
                .toList() ;
        var storedProducts = repository.findAllByIdInOrderById(productIds) ;
        if(productIds.size() != storedProducts.size()) {
            throw new RuntimeException("one or more products do not exist !") ;
        }
        var storedRequest = request.stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList() ;
        var purchasedProducts = new ArrayList<ProductPurchaseResponse>() ;
        for(int i=0 ; i < storedProducts.size() ; i++) {
            var product = storedProducts.get(i) ;
            var productRequest = storedRequest.get(i) ;
            if(product.getAvailableQuantity()  < productRequest.quantity()) {
                throw new RuntimeException("unsufficient stock quantity") ;
            }
            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity() ;
             product.setAvailableQuantity(newAvailableQuantity);
             repository.save(product) ;
             purchasedProducts.add(mapper.toproductPurchaseResponse(product, productRequest.quantity())) ;
        }
       return purchasedProducts ;
    }

    public ProductResponse findById(Integer productId) {
        return repository.findById(productId)
                .map(mapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("product with id " + productId + " not found !" )) ;
    }

    public List<ProductResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
