package com.sdet.lessons.chapter60;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Chapter 60 — Choosing a collection.
 * List = order. Set = uniqueness. Map = key to value. Queue = a line of work.
 */
public class CollectionChoiceDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 60: Collection Choice ===");

        List<String> testSteps = new ArrayList<>();
        testSteps.add("open");
        testSteps.add("click");
        testSteps.add("click");
        System.out.println("LIST for ordered steps (duplicates allowed): " + testSteps);

        Set<String> failedIds = new HashSet<>();
        failedIds.add("TC-1");
        failedIds.add("TC-1");
        System.out.println("SET for unique ids: " + failedIds);

        Map<String, String> usernameToRole = new HashMap<>();
        usernameToRole.put("qa.tester", "tester");
        System.out.println("MAP for username → role: " + usernameToRole);

        Queue<String> pendingJobs = new ArrayDeque<>();
        pendingJobs.add("retry login");
        pendingJobs.add("capture screenshot");
        System.out.println("QUEUE for jobs in line: " + pendingJobs);
        System.out.println("Ask: order? unique? lookup by name? a waiting line?");
    }
}
