package com.sdet.lessons.wrappers;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrappers and autoboxing.
 * Collections store objects, so {@code int} becomes {@code Integer}. {@code List<int>} does not compile.
 */
public class WrapperDemo {

    public static void main(String[] args) {
        System.out.println("=== Wrappers and Autoboxing ===");

        int primitiveStatus = 200;
        Integer wrappedStatus = primitiveStatus;
        int unboxedAgain = wrappedStatus;

        System.out.println("primitive int = " + primitiveStatus);
        System.out.println("autoboxed Integer = " + wrappedStatus);
        System.out.println("unboxed back to int = " + unboxedAgain);

        List<Integer> statusCodes = new ArrayList<>();
        statusCodes.add(200);
        statusCodes.add(404);
        statusCodes.add(500);
        System.out.println("List<Integer> = " + statusCodes);
        System.out.println("list.get(0) unboxes for arithmetic: " + (statusCodes.get(0) + 1));

        Integer missing = null;
        System.out.println("Integer can be null: " + missing);
        System.out.println("Unboxing a null Integer would throw NullPointerException.");
        System.out.println("Daily pairings: int↔Integer, double↔Double, boolean↔Boolean.");
    }
}
