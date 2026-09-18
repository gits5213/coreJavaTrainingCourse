package com.sdet.lessons.chapter52;

/**
 * Contract for fetching test users. Callers do not care whether data came from JSON, CSV, or a fake.
 */
public interface TestDataProvider {

    String getUser(String key);
}
