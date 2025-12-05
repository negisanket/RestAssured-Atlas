package com.prac.core;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

/**
 * Start/stop WireMock on a dynamic port. Instance-per-test-method usage is expected.
 */
public class WireMockManager {
    private WireMockServer server;

    public void start() {
        if (server == null || !server.isRunning()) {
            server = new WireMockServer(WireMockConfiguration.options().dynamicPort());
            server.start();
            configureFor("localhost", server.port());
        }
    }

    public void stop() {
        if (server != null && server.isRunning()) {
            server.stop();
            server = null;
        }
    }

    public void resetAll() {
        if (server != null && server.isRunning()) {
            server.resetAll();
        }
    }

    public String baseUrl() {
        if (server == null || !server.isRunning()) {
            throw new IllegalStateException("WireMock server not started");
        }
        return "http://localhost:" + server.port();
    }

    public WireMockServer server() {
        return server;
    }
}
