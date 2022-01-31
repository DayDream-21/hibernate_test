package com.slavamashkov;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.NoSuchElementException;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        System.out.println(readFromCatalogsById(3));
    }

    private static void createCatalog(String catalogTitle) {
        try (SessionFactory catalogSessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Catalog.class)
                .buildSessionFactory();
            Session session = catalogSessionFactory.getCurrentSession()) {

            Catalog catalog = new Catalog(catalogTitle);
            session.beginTransaction();
            session.save(catalog);
            session.getTransaction().commit();
        }
    }

    private static Catalog readFromCatalogsById(long id) {
        try (SessionFactory catalogSessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Catalog.class)
                .buildSessionFactory();
             Session session = catalogSessionFactory.getCurrentSession()) {

            session.beginTransaction();
            Catalog catalog = session.get(Catalog.class, id);
            session.getTransaction().commit();

            return Optional.ofNullable(catalog).orElseThrow(NoSuchElementException::new);
        }
    }
}
