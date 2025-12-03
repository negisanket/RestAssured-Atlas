package com.leegality.atlas.api;

import com.leegality.atlas.api.testdto.request.MockDTO;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.leegality.atlas.utils.Constants.MOCK_PATH_BY_ID;

public class MockApi extends BaseApi {


//    https://mocki.io/v1/024a4588-c15a-4dd0-9615-ef4a8a383464

    private static final long TIME_IN_MILLI_SEC = 1000L;

    @Step("Get mock request")
    public Response getMock(String id, int statusCode) {
        Response response = buildClientRequest().get(String.format(STATES_PATH_BY_ID, id);
        return validateStatusCode(response, TIME_IN_MILLI_SEC, statusCode);
    }

    @Step("Get mock request")
    public Response postMock(MockDTO payload, int statusCode) {
        Response response = buildClientRequest().body(payload).get(MOCK_PATH_BY_ID);
        return validateStatusCode(response, TIME_IN_MILLI_SEC, statusCode);
    }

}
