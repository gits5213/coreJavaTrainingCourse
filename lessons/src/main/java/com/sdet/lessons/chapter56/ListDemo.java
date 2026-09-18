package com.sdet.lessons.chapter56;

import java.util.ArrayList;
import java.util.List;

/**
 * Chapter 56 — {@code List}.
 * A List keeps order and allows duplicates. Use it for a sequence you walk by index.
 */
public class ListDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 56: List ===");

        List<String> browsers = new ArrayList<>();
        browsers.add("Chrome");
        browsers.add("Firefox");
        browsers.add("Edge");
        browsers.add("Chrome");

        System.out.println("list = " + browsers);
        System.out.println("size = " + browsers.size());
        System.out.println("get(0) = " + browsers.get(0));
        System.out.println("Chrome appears twice because List allows duplicates.");

        browsers.remove("Firefox");
        System.out.println("after remove Firefox = " + browsers);
    }
}
