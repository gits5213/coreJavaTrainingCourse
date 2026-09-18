package com.sdet.projects.grades;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GradeCalculatorTest {

    private final GradeCalculator calculator = new GradeCalculator();

    @Test
    void averageOfSampleScores() {
        int[] scores = {88, 92, 79, 95, 84};

        double actual = calculator.average(scores);

        assertEquals(87.6, actual, 0.0001);
    }

    @Test
    void letterGradeUsesStandardScale() {
        assertEquals('A', calculator.letterGrade(90));
        assertEquals('B', calculator.letterGrade(87.6));
        assertEquals('C', calculator.letterGrade(70));
        assertEquals('D', calculator.letterGrade(60));
        assertEquals('F', calculator.letterGrade(59.9));
    }

    @Test
    void emptyScoresAreRejected() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.average(new int[0])
        );

        assertEquals("scores must not be empty", exception.getMessage());
    }
}
