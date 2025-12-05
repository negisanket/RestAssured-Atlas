package com.prac.core;

/**
 * Simple model representing a created resource that needs cleanup.
 */
public class Resource {
    private final String type;
    private final String id;
    private final String baseUri;

    public Resource(String type, String id, String baseUri) {
        this.type = type;
        this.id = id;
        this.baseUri = baseUri;
    }

    public String getType() { return type; }
    public String getId() { return id; }
    public String getBaseUri() { return baseUri; }

    @Override
    public String toString() {
        return "Resource{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", baseUri='" + baseUri + '\'' +
                '}';
    }
}
