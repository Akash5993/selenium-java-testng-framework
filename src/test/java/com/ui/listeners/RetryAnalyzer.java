package com.ui.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Re-runs a failed test once, to absorb one-off glitches (slow network, a stray browser popup).
 * CAUTION: retries can hide real flakiness - keep the limit low and investigate every retry.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    // TODO 1: Implement TestNG's IRetryAnalyzer interface.
    //         Look up: org.testng.IRetryAnalyzer.

    // TODO 2: Add a constant for the maximum number of retries (1), and a counter field for attempts so far.
    //         - TestNG creates a separate RetryAnalyzer object per test method, so a plain instance
    //           counter is enough here.
    private static final int MAX_RETRIES = 1;
    private int retryCount = 0;

    // TODO 3: Override retry(ITestResult result): return true to run the test again, false to give up.
    //         - Retry while the counter is below the maximum, increasing it each time.
    //         - Print or log which test is being retried, so retries are visible in the output.
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRIES) {
            retryCount++;
            System.out.println("[Retry] " + result.getMethod().getMethodName()
                    + " failed - retry " + retryCount + " of " + MAX_RETRIES);
            return true;
        }
        return false;
    }
}
