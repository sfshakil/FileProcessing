package com.orange.fileprocessing.service;

import com.orange.fileprocessing.entity.Customer;
import com.orange.fileprocessing.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepo customerRepo;

    public Customer saveCustomer(Customer customer) {
        return customerRepo.save(customer);
    }
}
