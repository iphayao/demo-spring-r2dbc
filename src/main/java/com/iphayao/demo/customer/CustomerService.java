package com.iphayao.demo.customer;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Mono<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Flux<Customer> findCustomerByLastName(String lastName) {
        if (lastName == null) {
            return customerRepository.findAll();
        } else {
            return customerRepository.findByLastName(lastName);
        }
    }

    public Mono<Customer> createNewCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Mono<Customer> editCustomerById(long id, Customer customer) {
        return customerRepository.findById(id)
                .flatMap(e -> {
                    customer.setId(e.getId());
                    return customerRepository.save(customer);
                });

    }

    public Mono<Void> deleteCustomerById(long id) {
        return customerRepository.deleteById(id);
    }

}
