package com.sdet.lessons.chapter71;

/**
 * One-method contract. A lambda can implement this instead of a whole extra class.
 */
@FunctionalInterface
public interface Validator {

    boolean isValid(String value);
}
