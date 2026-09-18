package com.sdet.projects.jdbc;

public class DatabaseValidatorApp {

    public static void main(String[] args) throws Exception {
        ValidationReport report = new DatabaseValidator().validate();
        System.out.println(report.format());
        if (!report.passed()) {
            System.exit(1);
        }
    }
}
