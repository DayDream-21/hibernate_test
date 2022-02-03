package com.slavamashkov.library;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.NoSuchElementException;
import java.util.Optional;

public class LibraryMain {
    public static void main(String[] args) {
        addBookToReaderById(3, 2);
    }

    private static void addBookToReaderById(int bookId, int readerId) {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate_library.cfg.xml")
                .buildSessionFactory();
            Session session = sessionFactory.getCurrentSession()) {

            session.beginTransaction();

            Reader reader = session.get(Reader.class, readerId);
            Book book = session.get(Book.class, bookId);
            reader.getBooks().add(book);

            session.getTransaction().commit();
        }
    }

    private static void createCatalog(String catalogTitle) {
        try (SessionFactory catalogSessionFactory = new Configuration()
                .configure("hibernate_library.cfg.xml")
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
                .configure("hibernate_library.cfg.xml")
                .buildSessionFactory();
             Session session = catalogSessionFactory.getCurrentSession()) {

            session.beginTransaction();
            Catalog catalog = session.get(Catalog.class, id);
            session.getTransaction().commit();

            return Optional.ofNullable(catalog).orElseThrow(NoSuchElementException::new);
        }
    }
}
