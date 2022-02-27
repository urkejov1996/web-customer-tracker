package com.springdemo.dao;

import com.springdemo.entity.Customer;
import com.springdemo.util.SortUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    // need to inject the session factory
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Customer> getCustomers(int theSortField) {

        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();

        // determine sort field
        String theFieldName = null;

        switch (theSortField) {
            case SortUtils.FIRST_NAME:
                theFieldName = "firstName";
                break;
            case SortUtils.LAST_NAME:
                theFieldName = "lastName";
                break;
            case SortUtils.EMAIL:
                theFieldName = "email";
                break;
            default:
                // if nothing matches the default to sort by lastName
                theFieldName = "lastName";
        }

        // create a query
        String queryString = "from Customer order by " + theFieldName;
        Query<Customer> theQuery =
                currentSession.createQuery(queryString, Customer.class);

        // execute query and get result list
        List<Customer> customers = theQuery.getResultList();

        // return the results
        return customers;

    }

    @Override
    public void saveCustomer(Customer customer) {

        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();

        // save/update the customer
        currentSession.saveOrUpdate(customer);

    }

    @Override
    public Customer getCustomer(int id) {

        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();

        // now retrieve/read from database using the primary key
        Customer customer = currentSession.get(Customer.class, id);

        return customer;
    }

    @Override
    public void deleteCustomer(int id) {

        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();

        // delete the customer with primary key from database
        Query query =
                currentSession.createQuery("delete from Customer where id =:customerId");
        query.setParameter("customerId", id);

        query.executeUpdate();
    }

    @Override
    public List<Customer> searchCustomers(String theSearchName) {

        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();

        Query query = null;

        //
        // only search by name if theSearchName is not empty
        //

        if (theSearchName != null && theSearchName.trim().length() > 0) {
            // search for firstName or lastName ... case insensitive

            query = currentSession.createQuery(
            "from Customer where lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);
            query.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
        }else {
            // theSearchName is empty ... so just get all customers
            query= currentSession.createQuery("from Customer", Customer.class);
        }

        // execute query and get result list
        List<Customer> customers = query.getResultList();

        // return the results
        return customers;
    }

}
