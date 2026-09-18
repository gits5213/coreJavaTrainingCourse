package com.sdet.lessons.chapter80;

/**
 * Chapter 80 — Stack and heap.
 * Method calls and local variables live on the stack. Objects created with {@code new} live on the heap.
 */
public class StackHeapDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 80: Stack and Heap ===");

        int statusCode = 200;
        System.out.println("Local primitive statusCode=" + statusCode + " lives on the stack frame for main.");

        Ticket ticket = new Ticket("LOGIN");
        System.out.println("ticket is a stack reference pointing at a Ticket object on the heap.");
        printTicket(ticket);
        System.out.println("When printTicket returns, its stack frame is gone. The heap object can remain.");
        System.out.println("If nothing references the Ticket anymore, GC may collect it later.");
    }

    private static void printTicket(Ticket ticket) {
        System.out.println("Inside printTicket, another stack frame holds a copy of the reference.");
        System.out.println("Heap object id = " + ticket.id);
    }

    static class Ticket {
        final String id;

        Ticket(String id) {
            this.id = id;
        }
    }
}
