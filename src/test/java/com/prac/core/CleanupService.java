package com.prac.core;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CleanupService - knows how to delete resources of supported types.
 * It uses simple RestAssured DELETE calls. Add additional resource type -> delete path mappings as needed.
 *
 * NOTE: This service will skip cleanup for baseUri that looks like localhost (WireMock) or when cleanup.enabled=false.
 */
public final class CleanupService {

    // mapping resource type -> delete URL template (must contain {id})
    private static final Map<String, String> DELETE_PATHS = new HashMap<>();

    static {
        // register supported resource types and their delete endpoint templates
        // e.g. "mockItem" -> "/mock/items/{id}"
        DELETE_PATHS.put("mockItem", "/mock/items/{id}");
        DELETE_PATHS.put("order", "/store/order/{id}");

        // DELETE_PATHS.put("tenant", "/api/tenants/{id}");
    }

    private CleanupService() {}

    public static void cleanupResources(List<Resource> resources) {
        if (resources == null || resources.isEmpty()) {
            return;
        }

        // we group by baseUri because each resource might belong to a different host (unlikely but safe)
        Map<String, List<Resource>> byBase = resources.stream().collect(
                Collectors.groupingBy(Resource::getBaseUri)
        );

        for (Map.Entry<String, List<Resource>> e : byBase.entrySet()) {
            String baseUri = e.getKey();
            List<Resource> resList = e.getValue();

            if (shouldSkipCleanupFor(baseUri)) {
                System.out.println("[CleanupService] Skipping cleanup for baseUri=" + baseUri);
                continue;
            }

            for (Resource r : resList) {
                try {
                    deleteResource(baseUri, r);
                } catch (Exception ex) {
                    System.err.println("[CleanupService] Failed to delete resource " + r + " : " + ex.getMessage());
                    // Don't throw - continue best-effort cleanup
                }
            }
        }
    }

    private static boolean shouldSkipCleanupFor(String baseUri) {
        String cleanupProp = System.getProperty("cleanup.enabled", "true");
        if ("false".equalsIgnoreCase(cleanupProp)) return true;
        if (baseUri == null) return true;
        String lower = baseUri.toLowerCase();
        // skip if running against local wiremock / localhost / 127.0.0.1
        return lower.contains("localhost") || lower.contains("127.0.0.1") || lower.contains("wiremock");
    }

    private static void deleteResource(String baseUri, Resource r) {
        String pathTemplate = DELETE_PATHS.get(r.getType());
        if (pathTemplate == null) {
            System.out.println("[CleanupService] No delete path configured for resource type: " + r.getType());
            return;
        }
        String path = pathTemplate.replace("{id}", r.getId());

        System.out.println("[CleanupService] Deleting resource: type=" + r.getType() + ", id=" + r.getId() + ", url=" + baseUri + path);

        Response resp = RestAssured
                .given()
                .baseUri(baseUri)
                .delete(path);

        System.out.println("[CleanupService] Delete response for id=" + r.getId() + " => " + resp.getStatusCode());
    }
}
