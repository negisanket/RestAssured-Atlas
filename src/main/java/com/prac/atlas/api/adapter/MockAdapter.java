package com.prac.atlas.api.adapter;

import com.prac.atlas.api.MockApi;
import com.prac.atlas.api.testdto.request.MockDTO;
import io.restassured.response.Response;

public class MockAdapter {

    private final MockApi api;

    public MockAdapter() {
        this.api = new MockApi();
    }

    public Response createMock(MockDTO payload) {
        return api.createMock(payload);
    }
}
