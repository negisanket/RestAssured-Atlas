package com.leegality.atlas.api;

import com.leegality.atlas.api.testdto.request.StateDTO;
import io.qameta.allure.Step;
import io.restassured.response.Response;


public final class StatesApi extends BaseApi {
    private static final String STATES_PATH = "/digital-stamping/v1/states";
    private static final String STATES_PATH_BY_ID = STATES_PATH + "/%s";

    @Step("Create state request")
    public Response createState(StateDTO payload, int statusCode) {
        Response response = buildClientRequest().body(payload).post(STATES_PATH);
        return validateStatusCode(response, 2000L, statusCode);
    }

    @Step("Delete state request")
    public void deleteStateById(String id, int statusCode) {
        buildUserRequest().delete(String.format(STATES_PATH_BY_ID, id)).then().statusCode(statusCode);
    }

}
