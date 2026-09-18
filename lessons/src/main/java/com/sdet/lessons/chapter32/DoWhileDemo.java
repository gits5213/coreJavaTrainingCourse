package com.sdet.lessons.chapter32;

/**
 * Chapter 32 — {@code do-while}.
 * The body runs once before the condition is checked. Useful when you must try at least once.
 */
public class DoWhileDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 32: do-while ===");

        int page = 1;
        int lastPage = 2;

        do {
            System.out.println("Read results page " + page);
            page++;
        } while (page <= lastPage);

        System.out.println("do-while always runs the body at least once.");

        boolean alreadyOnLastPage = true;
        do {
            System.out.println("Polling once even if we might already be done. alreadyOnLastPage="
                    + alreadyOnLastPage);
        } while (!alreadyOnLastPage);
    }
}
