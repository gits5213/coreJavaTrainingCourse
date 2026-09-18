package com.sdet.lessons.chapter52;

/**
 * One implementation of {@link TestDataProvider}. In class we pretend the values came from JSON.
 */
public class JsonDataProvider implements TestDataProvider {

    @Override
    public String getUser(String key) {
        if ("standard".equals(key)) {
            return "qa.tester";
        }
        if ("admin".equals(key)) {
            return "qa.admin";
        }
        return "unknown.user";
    }
}
