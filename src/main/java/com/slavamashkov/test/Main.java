package com.slavamashkov.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;


public class Main {
    public static void main(String[] args) {
        long optStart = System.currentTimeMillis();
        optimisticLockTest(8, 1000);
        countSumOfValues(); // Should print 8000
        long optFinish = System.currentTimeMillis() - optStart;


        long pesStart = System.currentTimeMillis();
        pessimisticLockTest(8, 1000);
        countSumOfValues(); // Should print 16000
        long pesFinish = System.currentTimeMillis() - pesStart;

        System.out.println("Optimistic lock time: " + optFinish);
        System.out.println("Pessimistic lock time: " + pesFinish);
    }

    // After this method countSumOfValues should print sum = threadsAmount * cycles
    private static void optimisticLockTest(int threadsAmount, int cycles) {
        CountDownLatch countDownLatch = new CountDownLatch(threadsAmount);
        Thread[] threads = new Thread[threadsAmount];

        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate_test.cfg.xml")
                .buildSessionFactory()) {

            for (int i = 0; i < 8; i++) {
                threads[i] = new Thread(() -> { // Add threads in array
                    System.out.println(Thread.currentThread().getName() + ": started");
                    for (int j = 0; j < cycles; j++) { // In single thread create for cycle that changes field val

                        boolean isUpdated = false; // This flag indicates if we successfully change field val
                        Long randomRow = (long) (Math.random() * 39) + 2; // Get random row

                        while (!isUpdated) { // Start cycle that ends if we change field
                            Session session = sessionFactory.getCurrentSession();
                            session.beginTransaction();
                            Item item = session.get(Item.class, randomRow);
                            item.setVal(item.getVal() + 1);

                            uncheckableSleep(5);

                            try { // Try to save object with new value
                                session.save(item); // Item will be successfully saved if not any transaction made that before on the same row
                                session.getTransaction().commit();
                                isUpdated = true; // If success than we can exit from while cycle
                            } catch (OptimisticLockException e) {
                                session.getTransaction().rollback(); // If fail than we rollback and try to do this again
                            }

                            session.close();
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

    private static void pessimisticLockTest(int threadsAmount, int cycles) {
        CountDownLatch countDownLatch = new CountDownLatch(threadsAmount);
        Thread[] threads = new Thread[threadsAmount];

        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate_test.cfg.xml")
                .buildSessionFactory()) {

            for (int i = 0; i < 8; i++) {
                threads[i] = new Thread(() -> { // Add threads in array
                    System.out.println(Thread.currentThread().getName() + ": started");
                    for (int j = 0; j < cycles; j++) { // In single thread create for cycle that changes field val
                        Session session = sessionFactory.getCurrentSession();
                        Long randomRow = (long) (Math.random() * 39) + 2; // Get random row

                        session.beginTransaction();

                        Item item = session
                                .createQuery("FROM Item WHERE id = :id", Item.class)
                                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                                .setParameter("id", randomRow)
                                .getSingleResult();

                        item.setVal(item.getVal() + 1);

                        uncheckableSleep(5);

                        try {
                            session.save(item);
                            session.getTransaction().commit();
                        } catch (OptimisticLockException e) {
                            session.getTransaction().rollback();
                        }

                        session.close();
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
