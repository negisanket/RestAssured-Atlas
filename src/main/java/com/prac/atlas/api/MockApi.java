package com.prac.atlas.api;

import com.prac.atlas.api.testdto.request.MockDTO;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static com.prac.atlas.utils.Constants.DEFAULT_BASE;

public class MockApi {

    private static final String ENDPOINT = "/mock/create";

    public Response createMock(MockDTO payload) {
        return given()
                .baseUri(DEFAULT_BASE)
                .header("Content-Type", "application/json")
                .body(payload)
                .post(ENDPOINT)
                .then()
                .log().all()
                .extract()
                .response();
    }
}
