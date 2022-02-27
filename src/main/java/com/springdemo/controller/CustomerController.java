package com.springdemo.controller;

import com.springdemo.dao.CustomerDAO;
import com.springdemo.entity.Customer;
import com.springdemo.service.CustomerService;
import com.springdemo.util.SortUtils;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    // need to inject customer service
    @Autowired
    private CustomerService customerService;

    @GetMapping ("/list")
    public String listCustomers(Model model, @RequestParam(required = false) String sort) {

        // get customers from the service
        List<Customer> theCustomers = null;

        // check for sort field
        if (sort != null) {
            int theSortField = Integer.parseInt(sort);
            theCustomers = customerService.getCustomers(theSortField);
        }
        else {
            // no sort field provided ... default to sorting by last name
            theCustomers = customerService.getCustomers(SortUtils.LAST_NAME);
        }

        // add the customers to the model
        model.addAttribute("customers", theCustomers);

        return "list-customers";

    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model){

        // create model attribute to bind form data
        Customer customer = new Customer();

        model.addAttribute("customer", customer);

        return "customer-form";
    }

    @PostMapping("/saveCustomer")
    public String saveCustomer(@ModelAttribute("customer") Customer customer){

        // save the customer using service
        customerService.saveCustomer(customer);

        return "redirect:/customer/list";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("customerId")int id, Model model){

        // get the customer from the service
        Customer customer = customerService.getCustomer(id);

        // set customer as a model attribute to pre-populate the form
        model.addAttribute("customer", customer);

        // send over to form
        return  "customer-form";
    }

    @GetMapping("/delete")
    public String deleteCustomer(@RequestParam("customerId") int id){

        // delete the customer
        customerService.deleteCustomer(id);

        return "redirect:/customer/list";
    }

    @GetMapping("/search")
    public String searchCustomers(@RequestParam("theSearchName") String theSearchName, Model model){

        // search customers from the service
        List<Customer> customers = customerService.searchCustomers(theSearchName);

        // add the customer to the model
        model.addAttribute("customers" , customers);

        return "list-customers";
    }


}
