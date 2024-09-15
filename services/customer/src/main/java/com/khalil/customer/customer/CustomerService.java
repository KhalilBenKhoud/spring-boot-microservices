package com.khalil.customer.customer;

import com.ctc.wstx.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository ;
    private final CustomerMapper mapper ;
    public String createCustomer(CustomerRequest request) {
        var customer = repository.save(mapper.toCustomer(request)) ;
        return customer.getId() ;
    }

    public void updateCustomer(CustomerRequest request) {
        var customer = repository.findById(request.id()).orElseThrow(
                () -> new RuntimeException("no customer found")
        ) ;
        mergeCustomer(customer,request) ;
        repository.save(customer) ;
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
       if(StringUtils.isNotBlank(request.firstname())) {
           customer.setFirstname(request.firstname());
       }
        if(StringUtils.isNotBlank(request.lastname())) {
            customer.setLastname(request.lastname());
        }

        if(StringUtils.isNotBlank(request.email())) {
            customer.setLastname(request.email());
        }

        if(request.address() != null) {
            customer.setAddress(request.address());
        }



    }

    public List<CustomerResponse> findAllCustomers() {
        return repository.findAll()
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList()) ;
    }


    public Boolean existsById(String customerId) {
        var found = repository.findById(customerId).orElse(null) ;

        return found != null ;
    }

    public CustomerResponse findById(String customerId) {
        var found = repository.findById(customerId).orElse(null) ;
        assert found != null;
        return mapper.fromCustomer(found) ;
    }


    public CustomerResponse deleteById(String customerId) {
        return null ;
    }
}
