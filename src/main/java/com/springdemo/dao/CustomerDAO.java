package com.springdemo.dao;

import com.springdemo.entity.Customer;

import java.util.List;

public interface CustomerDAO {

    List<Customer> getCustomers(int theSortField);

    void saveCustomer(Customer customer);

    Customer getCustomer(int id);

    void deleteCustomer(int id);

    List<Customer> searchCustomers(String theSearchName);
}
