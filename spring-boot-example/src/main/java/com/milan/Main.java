package com.milan;

import com.milan.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/customers")
public class Main {

    private final CustomerRepository customerRepository;

    @Autowired
    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    record NewCustomerRequest(String name, String email, Integer age) {

    }

    @PostMapping
    public void addCustomer(@RequestBody NewCustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setAge(request.age());
        customerRepository.save(customer);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomer() {
        try {
            return new ResponseEntity<>(customerRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") Integer id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomerById(@PathVariable("customerId") Integer id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
    }

    @PutMapping("{customerId}")
    public Customer updateCustomer(@PathVariable("customerId") Integer id,@RequestBody Customer customer) {

        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    existingCustomer.setName(customer.getName());
                    existingCustomer.setEmail(customer.getEmail());
                    existingCustomer.setAge(customer.getAge());
                    return customerRepository.save(existingCustomer);
                })
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
    }



//    @GetMapping("/greet")
//    public GreetResponse greet() {
//        GreetResponse response = new GreetResponse(
//                "Hello",
//                List.of("Java", "Golang", "Javascript"),
//                new Person("Milan", 31, 30_000)
//        );
//        return response; //value="Hello"
//    }

//    record Person(String name, int age, double savings) {
//
//    }
//    record GreetResponse(
//            String greet,
//            List<String> favProgrammingLanguages,
//            Person person
//    ) {
//
//    }

    // This is the same as record
//    class GreetResponse {
//        private final String greet; //key=greet
//
//        GreetResponse(String greet) {
//            this.greet = greet;
//        }
//
//        // this get method gives JSON object.if we uncomment it,it will give whitelabel error
//        public String getGreet() {
//            return greet;
//        }
//
//        @Override
//        public String toString() {
//            return "GreetResponse{" +
//                    "greet='" + greet + '\'' +
//                    '}';
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            GreetResponse that = (GreetResponse) o;
//            return Objects.equals(greet, that.greet);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(greet);
//        }
//    }





