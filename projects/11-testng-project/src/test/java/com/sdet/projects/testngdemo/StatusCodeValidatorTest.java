package com.sdet.projects.testngdemo;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StatusCodeValidatorTest {

    private StatusCodeValidator validator;

    @BeforeMethod
    public void setUp() {
        validator = new StatusCodeValidator();
    }

    @AfterMethod
    public void tearDown() {
        validator = null;
    }

    @DataProvider(name = "statusCodes")
    public Object[][] statusCodes() {
        return new Object[][] {
                {200, 200, true},
                {404, 404, true},
                {500, 500, true},
                {200, 404, false},
                {404, 500, false},
                {500, 200, false}
        };
    }

    @Test(dataProvider = "statusCodes")
    public void shouldCompareExpectedAndActualStatus(int expected, int actual, boolean shouldMatch) {
        boolean matched = validator.matches(expected, actual);

        Assert.assertEquals(matched, shouldMatch, validator.describe(expected, actual));
        Assert.assertNotNull(validator);
    }
}
