package com.sdet.projects.calculator;

public class Calculator {

    public int add(int left, int right) {
        return left + right;
    }

    public int subtract(int left, int right) {
        return left - right;
    }

    public int multiply(int left, int right) {
        return left * right;
    }

    public double divide(int left, int right) {
        if (right == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return (double) left / right;
    }
}
