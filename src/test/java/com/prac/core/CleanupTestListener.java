package com.prac.core;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.List;

public class CleanupTestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        // no-op
    }

    @Override
    public void onTestStart(ITestResult result) {
        String id = ResourceTracker.registerCurrentTest();
        System.out.println("[CleanupTestListener] Test started: " + result.getName() + " id=" + id);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        runCleanup(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        runCleanup(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        runCleanup(result);
    }

    @Override
    public void onFinish(ITestContext context) {
        // no-op
    }

    private void runCleanup(ITestResult result) {
        String testId = ResourceTracker.getCurrentTestId();
        if (testId == null) {
            System.out.println("[CleanupTestListener] No resources to cleanup for " + result.getName());
            return;
        }
        List<Resource> resources = ResourceTracker.takeResourcesForTest(testId);
        CleanupService.cleanupResources(resources);
        ResourceTracker.clearCurrentTest();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        runCleanup(result);
    }
}
