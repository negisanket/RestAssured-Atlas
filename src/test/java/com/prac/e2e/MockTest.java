package com.prac.e2e;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.prac.atlas.api.MockApi;
import com.prac.atlas.api.factory.MockPayloadFactory;
import com.prac.atlas.api.testdto.request.MockDTO;
import com.prac.stubs.MockStubFactory;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class MockTest {

    private WireMockServer wireMockServer;

    @BeforeClass
    public void startWireMock() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        MockStubFactory.createMockStub();
    }

    @AfterClass
    public void stopWireMock() {
        wireMockServer.stop();
    }

    @Test
    public void testCreateMock() {
        MockDTO payload = MockPayloadFactory.getDefaultMockPayload();
        Response response = new MockApi().createMock(payload);
        assertEquals(response.statusCode(), 201, "Status code mismatch");
    }
}
