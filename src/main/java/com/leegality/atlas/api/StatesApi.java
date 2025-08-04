package com.leegality.atlas.api;

import com.leegality.atlas.api.testdto.request.StateDTO;
import io.qameta.allure.Step;
import io.restassured.response.Response;


public final class StatesApi extends BaseApi {
    private static final String STATES_PATH = "/states";
    private static final String STATES_PATH_BY_ID = STATES_PATH + "/%s";

    private static final long TIME_IN_MILLI_SEC = 1000L;

    @Step("Create state request")
    public Response createState(StateDTO payload, int statusCode) {
        Response response = buildClientRequest().body(payload).post(STATES_PATH);
        return validateStatusCode(response, TIME_IN_MILLI_SEC, statusCode);
    }

    @Step("Delete state request")
    public void deleteStateById(String id, int statusCode) {
        buildUserRequest().delete(String.format(STATES_PATH_BY_ID, id)).then().statusCode(statusCode);
    }

    @Step("Create state request : MainApp User")
    public Response createStateUsingMainAppUser(StateDTO payload, int statusCode) {
        Response response = buildMainAppUserRequest().body(payload).post(STATES_PATH);
        return validateStatusCode(response, statusCode);
    }

}
