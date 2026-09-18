package com.sdet.projects.parallel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
class ParallelDriverTest {

    private static final Set<Integer> DRIVER_HASHES = ConcurrentHashMap.newKeySet();

    @BeforeEach
    void setUp() {
        DriverManager.create();
    }

    @AfterEach
    void tearDown() {
        DriverManager.unload();
    }

    @Test
    void workerAHasItsOwnDriver() throws InterruptedException {
        assertIsolatedDriver();
    }

    @Test
    void workerBHasItsOwnDriver() throws InterruptedException {
        assertIsolatedDriver();
    }

    @Test
    void workerCHasItsOwnDriver() throws InterruptedException {
        assertIsolatedDriver();
    }

    private static void assertIsolatedDriver() throws InterruptedException {
        FakeDriver mine = DriverManager.get();
        int hash = System.identityHashCode(mine);

        Thread.sleep(150);

        assertSame(mine, DriverManager.get());
        assertEquals(hash, System.identityHashCode(DriverManager.get()));
        assertTrue(
                DRIVER_HASHES.add(hash),
                "each parallel test must have a unique FakeDriver instance, hash=" + hash);
    }
}
