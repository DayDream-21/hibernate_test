package com.slavamashkov.shop;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ShopMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Command parameter(s):" +
                "\n 1) buy product_name customer_name " +
                "\n 2) showShopList customer_name" +
                "\n 3) showSales product_name" +
                "\n 4) addCustomer customer_name" +
                "\n 5) addProduct product_name price(x.xx)" +
                "\n 6) removeCustomer customer_name" +
                "\n 7) removeProduct product_name" +
                "\n 8) exit");

        while (scanner.hasNext()) {
            System.out.println("Enter command and parameter(s)");
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                break;
            }

            String[] commandParts = command.split(" ");

            switch (commandParts[0]) {
                case "buy"            -> addProductToCustomerByName(commandParts[1], commandParts[2]);
                case "showShopList"   -> showProductsByCustomerName(commandParts[1]);
                case "showSales"      -> showCustomersByProductName(commandParts[1]);
                case "addCustomer"    -> addCustomer(commandParts[1]);
                case "addProduct"     -> addProduct(commandParts[1], BigDecimal.valueOf(Double.parseDouble(commandParts[2])));
                case "removeCustomer" -> removeCustomerByCustomerName(commandParts[1]);
                case "removeProduct"  -> removeProductByProductName(commandParts[1]);
            }
        }
    }

    private static void showProductsByCustomerName(String customerName) {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate_shop.cfg.xml")
                .buildSessionFactory();
             Session session = sessionFactory.getCurrentSession()) {

            session.beginTransaction();

            Customer customer = (Customer) session
                    .createQuery("from Customer customer where customer.name = :name")
                    .setParameter("name", customerName)
                    .getSingleResult();

            List<Order> orders = customer.getOrders();
            System.out.println("Products for " + customer.getName() + ": ");
            orders.forEach(order -> System.out.println(order.getProduct().toString()));

            session.getTransaction().commit();
        }
    }

    private static void showCustomersByProductName(String productName) {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate_shop.cfg.xml")
                .buildSessionFactory();
             Session session = sessionFactory.getCurrentSession()) {

            session.beginTransaction();

            Product product = (Product) session
                    .createQuery("from Product product where product.name = :name")
                    .setParameter("name", productName)
                    .getSingleResult();

            List<Order> orders = product.getOrders();
            System.out.println("Customers for " + product.getName() + ": ");
            orders.forEach(order -> System.out.println(order.getCustomer().toString()));

            session.getTransaction().commit();
        }
    }

    private static void addCustomer(String customerName) {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate_shop.cfg.xml")
                .buildSessionFactory();
             Session session = sessionFactory.getCurrentSession()) {

            session.beginTransaction();

            Customer customer = new Customer(customerName);
            session.save(customer);

            session.getTransaction().commit();
        }
    }

    private static void addProduct(String productName, BigDecimal price) {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate_shop.cfg.xml")
                .buildSessionFactory();
             Session session = sessionFactory.getCurrentSession()) {

            session.beginTransaction();

            Product product = new Product(productName, price);
            session.save(product);

            session.getTransaction().commit();
        }
    }

    private static void removeCustomerByCustomerName(String customerName) {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate_shop.cfg.xml")
                .buildSessionFactory();
             Session session = sessionFactory.getCurrentSession()) {

            session.beginTransaction();

            Customer customer = (Customer) session
                    .createQuery("from Customer customer where customer.name = :name")
                    .setParameter("name", customerName)
                    .getSingleResult();
            session.delete(customer);

            session.getTransaction().commit();
        }
    }

    private static void removeProductByProductName(String productName) {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate_shop.cfg.xml")
                .buildSessionFactory();
             Session session = sessionFactory.getCurrentSession()) {

            session.beginTransaction();

            Product product = (Product) session
                    .createQuery("from Product product where product.name = :name")
                    .setParameter("name", productName)
                    .getSingleResult();
            session.delete(product);

            session.getTransaction().commit();
        }
    }

    private static void addProductToCustomerByName(String productName, String customerName) {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate_shop.cfg.xml")
                .buildSessionFactory();
             Session session = sessionFactory.getCurrentSession()) {

            session.beginTransaction();

            Customer customer = (Customer) session
                    .createQuery("from Customer customer where customer.name = :name")
                    .setParameter("name", customerName)
                    .getSingleResult();

            Product product = (Product) session
                    .createQuery("from Product product where product.name = :name")
                    .setParameter("name", productName)
                    .getSingleResult();

            Order order = new Order(new OrderKey(customer.getId(), product.getId()));
            order.setPrice(product.getPrice());

            session.save(order);

            session.getTransaction().commit();
        }
    }
}
