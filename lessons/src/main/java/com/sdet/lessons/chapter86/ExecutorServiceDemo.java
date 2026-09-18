package com.sdet.lessons.chapter86;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Chapter 86 — {@code ExecutorService}.
 * A pool runs tasks on worker threads so you do not create raw {@code Thread} objects by hand.
 */
public class ExecutorServiceDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Chapter 86: ExecutorService ===");

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            pool.submit(() -> printJob("check login"));
            pool.submit(() -> printJob("check search"));
            pool.submit(() -> printJob("check checkout"));
        } finally {
            pool.shutdown();
        }

        boolean finished = pool.awaitTermination(2, TimeUnit.SECONDS);
        System.out.println("pool finished = " + finished);
        System.out.println("Always shut down the pool so the JVM can exit.");
    }

    private static void printJob(String name) {
        System.out.println(Thread.currentThread().getName() + " running " + name);
    }
}
