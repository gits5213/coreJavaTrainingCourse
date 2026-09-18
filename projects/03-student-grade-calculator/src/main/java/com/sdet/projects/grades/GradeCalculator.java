package com.sdet.projects.grades;

public class GradeCalculator {

    public double average(int[] scores) {
        if (scores == null || scores.length == 0) {
            throw new IllegalArgumentException("scores must not be empty");
        }

        int total = 0;
        for (int score : scores) {
            total += score;
        }
        return (double) total / scores.length;
    }

    public char letterGrade(double average) {
        if (average >= 90) {
            return 'A';
        }
        if (average >= 80) {
            return 'B';
        }
        if (average >= 70) {
            return 'C';
        }
        if (average >= 60) {
            return 'D';
        }
        return 'F';
    }

    public GradeReport report(int[] scores) {
        double average = average(scores);
        return new GradeReport(scores, average, letterGrade(average));
    }
}
