package com.sdet.projects.junit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Test
    void shouldAddTwoPositiveNumbers() {
        Calculator calculator = new Calculator();

        int actual = calculator.add(2, 3);

        assertEquals(5, actual);
    }

    @Test
    void shouldAddNegativeAndPositiveNumbers() {
        Calculator calculator = new Calculator();

        int actual = calculator.add(-4, 10);

        assertEquals(6, actual);
    }

    @Test
    void shouldSubtractTwoPositiveNumbers() {
        Calculator calculator = new Calculator();

        int actual = calculator.subtract(10, 4);

        assertEquals(6, actual);
    }

    @Test
    void shouldSubtractToANegativeResult() {
        Calculator calculator = new Calculator();

        int actual = calculator.subtract(3, 8);

        assertEquals(-5, actual);
    }
}
