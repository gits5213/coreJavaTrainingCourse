package com.sdet.projects.grades;

import java.util.Arrays;

public final class GradeReport {

    private final int[] scores;
    private final double average;
    private final char letterGrade;

    public GradeReport(int[] scores, double average, char letterGrade) {
        this.scores = Arrays.copyOf(scores, scores.length);
        this.average = average;
        this.letterGrade = letterGrade;
    }

    public int[] scores() {
        return Arrays.copyOf(scores, scores.length);
    }

    public double average() {
        return average;
    }

    public char letterGrade() {
        return letterGrade;
    }

    public String format() {
        StringBuilder scoreList = new StringBuilder();
        for (int index = 0; index < scores.length; index++) {
            if (index > 0) {
                scoreList.append(", ");
            }
            scoreList.append(scores[index]);
        }
        return "Scores: " + scoreList
                + System.lineSeparator()
                + "Average: " + average
                + System.lineSeparator()
                + "Letter grade: " + letterGrade;
    }
}
