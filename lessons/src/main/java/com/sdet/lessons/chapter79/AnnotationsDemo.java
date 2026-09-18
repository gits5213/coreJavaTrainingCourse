package com.sdet.lessons.chapter79;

/**
 * Chapter 79 — Annotations.
 * Annotations are labels on code. Some are built in ({@code @Override}); you can also write your own.
 */
public class AnnotationsDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 79: Annotations ===");
        AnnotationsDemo demo = new AnnotationsDemo();
        demo.loginTest();
        System.out.println("@Override tells the compiler this method must match a parent method.");
        System.out.println("@TestCase is a custom label. Frameworks can read it later with reflection.");
    }

    @TestCase(id = "TC-100", title = "Valid login")
    public void loginTest() {
        System.out.println("Running labeled method loginTest");
    }

    @Override
    public String toString() {
        return "AnnotationsDemo";
    }
}
