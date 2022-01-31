package com.slavamashkov;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        createCatalog("Fantasy #17");
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
}
