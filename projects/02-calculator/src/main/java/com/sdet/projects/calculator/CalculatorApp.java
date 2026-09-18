package com.sdet.projects.calculator;

public class CalculatorApp {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        int left = 10;
        int right = 4;

        System.out.println(left + " + " + right + " = " + calculator.add(left, right));
        System.out.println(left + " - " + right + " = " + calculator.subtract(left, right));
        System.out.println(left + " * " + right + " = " + calculator.multiply(left, right));
        System.out.println(left + " / " + right + " = " + calculator.divide(left, right));

        try {
            calculator.divide(left, 0);
        } catch (IllegalArgumentException exception) {
            System.out.println(left + " / 0 -> " + exception.getMessage());
        }
    }
}
