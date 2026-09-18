package com.sdet.lessons.chapter16;

/**
 * Chapter 16 — Comments.
 * Comments are notes for humans. The compiler ignores them.
 */
public class CommentsDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 16: Comments ===");

        // This is a single-line comment. Java skips it.
        System.out.println("Single-line comments start with //");

        /*
         * This is a multi-line comment.
         * Use it when a note needs more than one line.
         */
        System.out.println("Multi-line comments sit between /* and */");

        /**
         * Javadoc comments (like this class comment) document types and methods.
         * Students read them in IntelliJ by hovering the class name.
         */
        System.out.println("Javadoc starts with /** and is for documentation.");
        System.out.println("Comments do not change program output.");
    }
}
