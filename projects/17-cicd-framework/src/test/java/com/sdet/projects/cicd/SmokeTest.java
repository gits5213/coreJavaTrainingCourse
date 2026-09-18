package com.sdet.projects.cicd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SmokeTest {

    @Test
    void pipelineSmokeAlwaysPasses() {
        assertTrue(true, "PR smoke gate: compile and unit tests are wired.");
    }
}
