package com.prac.core;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base class for tests using WireMock. Starts/stops server per method to ensure isolation
 * and allow parallel execution.
 */
public abstract class TestBase {

    protected WireMockManager wm;

    @BeforeMethod(alwaysRun = true)
    public void startWireMock() {
        wm = new WireMockManager();
        wm.start();
    }

    @AfterMethod(alwaysRun = true)
    public void stopWireMock() {
        if (wm != null) {
            wm.resetAll();
            wm.stop();
        }
    }
}
