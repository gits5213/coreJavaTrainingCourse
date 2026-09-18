package com.sdet.projects.grades;

public class GradeCalculatorApp {

    public static void main(String[] args) {
        int[] scores = {88, 92, 79, 95, 84};
        GradeCalculator calculator = new GradeCalculator();
        GradeReport report = calculator.report(scores);
        System.out.println(report.format());
    }
}
