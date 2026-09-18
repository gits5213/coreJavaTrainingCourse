package com.sdet.lessons.chapter57;

import java.util.HashSet;
import java.util.Set;

/**
 * Chapter 57 — {@code Set}.
 * A Set stores unique values. Adding the same item twice does not create a second copy.
 */
public class SetDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 57: Set ===");

        Set<String> failedTestIds = new HashSet<>();
        failedTestIds.add("TC-101");
        failedTestIds.add("TC-202");
        failedTestIds.add("TC-101");

        System.out.println("set = " + failedTestIds);
        System.out.println("size = " + failedTestIds.size());
        System.out.println("contains TC-202? " + failedTestIds.contains("TC-202"));
        System.out.println("TC-101 was added twice but stored once.");
    }
}
