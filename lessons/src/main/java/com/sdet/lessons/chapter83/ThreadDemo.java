package com.sdet.lessons.chapter83;

/**
 * Chapter 83 — Threads.
 * A {@code Thread} runs code concurrently. {@code join} waits for that work to finish.
 */
public class ThreadDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Chapter 83: Thread ===");
        System.out.println("main thread = " + Thread.currentThread().getName());

        Thread worker = new Thread(() -> {
            System.out.println("worker thread = " + Thread.currentThread().getName());
            System.out.println("worker: simulating a short check");
        }, "sdet-worker");

        worker.start();
        worker.join();
        System.out.println("main continues after join.");
    }
}
