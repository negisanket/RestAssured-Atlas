package com.prac.stubs;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.client.WireMock;

public class MockStubFactory {

    public static void createMockStub() {
        WireMock.stubFor(post(urlEqualTo("/mock/create"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"message\": \"Mock created successfully\" }")));
    }
}
