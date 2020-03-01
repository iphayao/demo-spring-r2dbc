package com.iphayao.demo.customer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public Flux<Customer> getCustomers(@RequestParam(name = "last_name", required = false) Optional<String> lastName) {
        Flux<Customer> customers;
        if(lastName.isPresent()) {
            customers = customerService.findCustomerByLastName(lastName.get());
        } else {
            customers = customerService.getAllCustomer();
        }
        return customers;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Customer>> getCustomer(@PathVariable long id) {
        return customerService.findCustomerById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<Customer> postCustomer(@RequestBody Mono<Customer> customer) {
        return customerService.createNewCustomer(customer);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Customer>> putCustomer(@PathVariable long id, @RequestBody Mono<Customer> customer) {
        return customerService.editCustomerById(id, customer)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomer(@PathVariable long id) {
        return customerService.deleteCustomerById(id);
    }
}
