package com.sdet.projects.testngdemo;

/**
 * Compares an expected HTTP status to the actual status from a response.
 * Later projects reuse this idea inside REST Assured assertions.
 */
public final class StatusCodeValidator {

    public boolean matches(int expected, int actual) {
        return expected == actual;
    }

    public String describe(int expected, int actual) {
        String verdict = matches(expected, actual) ? "PASS" : "FAIL";
        return verdict + " expected=" + expected + " actual=" + actual;
    }
}
