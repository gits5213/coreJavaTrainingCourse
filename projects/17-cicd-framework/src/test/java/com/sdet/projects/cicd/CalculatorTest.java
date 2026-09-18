package com.sdet.projects.cicd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Test
    void shouldAddTwoNumbers() {
        Calculator calculator = new Calculator();

        assertEquals(9, calculator.add(4, 5));
    }

    @Test
    void shouldSubtractTwoNumbers() {
        Calculator calculator = new Calculator();

        assertEquals(3, calculator.subtract(10, 7));
    }
}
