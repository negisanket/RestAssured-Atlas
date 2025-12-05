package com.prac.core;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Thread-safe tracker for created resources.
 * Usage:
 *   // at test startup (listener will set test id automatically)
 *   ResourceTracker.registerCurrentTest();
 *   // after creating a resource:
 *   ResourceTracker.registerResource("mockItem", createdId, baseUri);
 */
public final class ResourceTracker {

    // Map: testId -> list of resources for that test
    private static final ConcurrentHashMap<String, CopyOnWriteArrayList<Resource>> store = new ConcurrentHashMap<>();

    // ThreadLocal current test id (per thread)
    private static final ThreadLocal<String> currentTestId = new ThreadLocal<>();

    private ResourceTracker() {}

    /** Called by the TestNG listener to initialize the current test id */
    public static String registerCurrentTest() {
        String id = UUID.randomUUID().toString();
        currentTestId.set(id);
        store.putIfAbsent(id, new CopyOnWriteArrayList<>());
        return id;
    }

    /** Returns current test id */
    public static String getCurrentTestId() {
        return currentTestId.get();
    }

    /** Register a resource for the current test */
    public static void registerResource(String type, String resourceId, String baseUri) {
        String id = getCurrentTestId();
        if (id == null) {
            throw new IllegalStateException("No current test id. Ensure TestNG listener is configured.");
        }
        Resource r = new Resource(type, resourceId, baseUri);
        store.get(id).add(r);
        System.out.println("[ResourceTracker] Registered resource for test " + id + " : " + r);
    }

    /** Get resources for a given test id (or current test if null) */
    public static List<Resource> getResourcesForTest(String testId) {
        if (testId == null) testId = getCurrentTestId();
        return store.getOrDefault(testId, new CopyOnWriteArrayList<>());
    }

    /** Remove and return resources for the given test id (clears the store entry) */
    public static List<Resource> takeResourcesForTest(String testId) {
        if (testId == null) testId = getCurrentTestId();
        CopyOnWriteArrayList<Resource> list = store.remove(testId);
        if (list == null) return new CopyOnWriteArrayList<>();
        return list;
    }

    /** Clear the current test id for current thread */
    public static void clearCurrentTest() {
        currentTestId.remove();
    }
}
