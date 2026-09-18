package com.sdet.projects.calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorTest {

    private final Calculator calculator = new Calculator();

    @Test
    void addReturnsSum() {
        int actual = calculator.add(10, 4);

        assertEquals(14, actual);
    }

    @Test
    void subtractReturnsDifference() {
        int actual = calculator.subtract(10, 4);

        assertEquals(6, actual);
    }

    @Test
    void multiplyReturnsProduct() {
        int actual = calculator.multiply(10, 4);

        assertEquals(40, actual);
    }

    @Test
    void divideReturnsQuotient() {
        double actual = calculator.divide(10, 4);

        assertEquals(2.5, actual);
    }

    @Test
    void divideByZeroIsRejected() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.divide(10, 0)
        );

        assertEquals("Cannot divide by zero", exception.getMessage());
    }
}
