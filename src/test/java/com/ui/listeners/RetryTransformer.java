package com.ui.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * Attaches RetryAnalyzer to EVERY @Test automatically, so no test has to write retryAnalyzer = ... itself.
 * CONCEPT: changing annotations at runtime, before TestNG runs anything.
 */
public class RetryTransformer implements IAnnotationTransformer {

    // TODO 1: Implement TestNG's IAnnotationTransformer interface.
    //         Look up: org.testng.IAnnotationTransformer.

    // TODO 2: Override transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor,
    //         Method testMethod) and set RetryAnalyzer as the annotation's retry analyzer.
    //         - The Class/Constructor parameters use raw types in TestNG's own signature - copy it exactly.
    //         Look up: ITestAnnotation.setRetryAnalyzer.
    @Override
    @SuppressWarnings("rawtypes")
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
