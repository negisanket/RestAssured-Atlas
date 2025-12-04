package com.prac.e2e;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prac.atlas.api.MockApi;
import com.prac.atlas.api.testdto.request.MockDTO;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpStatus;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;

public class MockGetTest {

    MockApi mockApi = new MockApi();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createAndgetMockApiIT() throws JsonProcessingException {

    //    File file = Paths.get("src/test/resources/payloads/MockGetPayload.json").toFile();

        MockDTO requestPayload =  objectMapper.readValue("resources/payloads/MockGetPayload", MockDTO.class);

        Response resp = mockApi.postMock(requestPayload, HttpStatus.SC_CREATED);
        resp.then().assertThat().body("status", containsString("success"));
        String id = resp.jsonPath("id");

        Response respGet = mockApi.getMock(id, HttpStatus.SC_OK);
        resp.then().assertThat().body("status", containsString("Fetched Sucessfully"));

    }

    @Test
    public void postMockApiIT() throws JsonProcessingException {
        MockDTO requestPayload =  objectMapper.readValue("resources/payloads/MockGetPayload", MockDTO.class);

        Response resp = mockApi.postMock(requestPayload, HttpStatus.SC_OK);

        resp.then().assertThat().body("status", containsString("success"));
    }

}
