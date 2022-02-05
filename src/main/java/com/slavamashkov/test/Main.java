package com.slavamashkov.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.OptimisticLockException;
import java.util.concurrent.CountDownLatch;


public class Main {
    public static void main(String[] args) {
        optimisticLockTest(8, 1000);
        countSumOfValues(); // Should print 8000
    }

    // After this method countSumOfValues should print sum = threadsAmount * cycles
    private static void optimisticLockTest(int threadsAmount, int cycles) {
        CountDownLatch countDownLatch = new CountDownLatch(threadsAmount);
        Thread[] threads = new Thread[threadsAmount];

        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate_test.cfg.xml")
                .buildSessionFactory()) {

            for (int i = 0; i < 8; i++) {
                threads[i] = new Thread(() -> {
                    System.out.println(Thread.currentThread().getName() + ": started");
                    for (int j = 0; j < cycles; j++) {

                        boolean isUpdated = false;
                        Long randomRow = (long) (Math.random() * 39) + 1;

                        while (!isUpdated) {
                            Session session = sessionFactory.getCurrentSession();
                            session.beginTransaction();
                            Item item = session.get(Item.class, randomRow);
                            item.setVal(item.getVal() + 1);

                            uncheckableSleep(5);

                            try {
                                session.save(item);
                                session.getTransaction().commit();
                                isUpdated = true;
                            } catch (OptimisticLockException e) {
                                session.getTransaction().rollback();
                            }

                            if (session != null) {
                                session.close();
                            }
                        }
                    }
                    countDownLatch.countDown();
                });
                threads[i].start();
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("End");
        }
    }

    private static void countSumOfValues() {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate_test.cfg.xml")
                .buildSessionFactory();
             Session session = sessionFactory.getCurrentSession()) {

            session.beginTransaction();

            Object o = session.createNativeQuery("SELECT SUM(val) FROM test.items ").getSingleResult();
            System.out.println(o);
        }
    }

    private static void uncheckableSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
