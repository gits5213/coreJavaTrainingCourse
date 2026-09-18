package com.sdet.lessons.chapter58;

import java.util.HashMap;
import java.util.Map;

/**
 * Chapter 58 — {@code Map}.
 * A Map looks up a value by a key, such as username → role.
 */
public class MapDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 58: Map ===");

        Map<String, String> usernameToRole = new HashMap<>();
        usernameToRole.put("qa.tester", "tester");
        usernameToRole.put("qa.admin", "admin");
        usernameToRole.put("qa.viewer", "viewer");

        System.out.println("map = " + usernameToRole);
        System.out.println("qa.tester role = " + usernameToRole.get("qa.tester"));
        System.out.println("qa.admin role = " + usernameToRole.get("qa.admin"));
        System.out.println("containsKey qa.viewer? " + usernameToRole.containsKey("qa.viewer"));

        usernameToRole.put("qa.tester", "lead-tester");
        System.out.println("after update, qa.tester role = " + usernameToRole.get("qa.tester"));
        System.out.println("Keys are unique. Putting the same username again replaces the role.");
    }
}
