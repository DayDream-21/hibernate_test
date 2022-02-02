package com.slavamashkov.shop;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ShopMain {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Product.class)
                .buildSessionFactory();
             Session session = sessionFactory.getCurrentSession()) {

            session.beginTransaction();

            Customer customer = session.get(Customer.class, 3);
            Product product = session.get(Product.class, 2);

            customer.getProducts().add(product);

            session.getTransaction().commit();
        }
    }
}
