package com.slavamashkov.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestPersonMain {
    public static void main(String[] args) {
        addTestPerson("Soul", Department.HR, 720);
    }

    private static void addTestPerson(String name, Department dep, Integer salary) {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate_test.cfg.xml")
                .buildSessionFactory();
            Session session = sessionFactory.getCurrentSession()){

            session.beginTransaction();

            TestPerson testPerson = new TestPerson(name, dep, salary);
            session.save(testPerson);

            session.getTransaction().commit();
        }
    }
}
