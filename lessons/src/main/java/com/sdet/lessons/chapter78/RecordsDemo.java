package com.sdet.lessons.chapter78;

/**
 * Chapter 78 — Records.
 * {@link LoginData} stores login fields without writing getters by hand.
 */
public class RecordsDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 78: Records ===");

        LoginData first = new LoginData("qa.tester", "Test123");
        LoginData second = new LoginData("qa.tester", "Test123");
        LoginData admin = new LoginData("qa.admin", "Test123");

        System.out.println("first = " + first);
        System.out.println("username accessor = " + first.username());
        System.out.println("password accessor = " + first.password() + " (demo password, not a real secret)");
        System.out.println("first.equals(second) = " + first.equals(second));
        System.out.println("first.equals(admin) = " + first.equals(admin));
        System.out.println("Records are immutable. There is no setUsername().");
    }
}
