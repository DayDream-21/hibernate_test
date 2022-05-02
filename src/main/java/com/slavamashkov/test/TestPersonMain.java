package com.slavamashkov.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestPersonMain {
    public static void main(String[] args) {
        addTestPerson("Kate", Department.HR);
    }

    private static void addTestPerson(String name, Department dep) {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate_test.cfg.xml")
                .buildSessionFactory();
            Session session = sessionFactory.getCurrentSession()){

            session.beginTransaction();

            TestPerson testPerson = new TestPerson(name, dep);
            session.save(testPerson);

            session.getTransaction().commit();
        }
    }
}
