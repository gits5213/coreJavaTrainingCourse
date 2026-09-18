package com.sdet.lessons.chapter59;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Chapter 59 — {@code Queue}.
 * A Queue is a line of work: first in, first out.
 */
public class QueueDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 59: Queue ===");

        Queue<String> jobs = new ArrayDeque<>();
        jobs.add("open login page");
        jobs.add("submit credentials");
        jobs.add("assert dashboard");

        System.out.println("queue = " + jobs);
        System.out.println("peek (next job, do not remove) = " + jobs.peek());

        while (!jobs.isEmpty()) {
            String job = jobs.poll();
            System.out.println("processing: " + job);
        }

        System.out.println("queue after processing = " + jobs);
    }
}
