package com.leegality.atlas.api;

import com.leegality.atlas.api.commondto.response.AccessTokenDTO;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.leegality.atlas.utils.CommonMethods.getSecrets;
import static com.leegality.atlas.utils.CommonMethods.getStandardUrl;
import static com.leegality.atlas.utils.Constants.AUTH_HEADER;
import static com.leegality.atlas.utils.Constants.BASIC;
import static com.leegality.atlas.utils.Constants.BEARER;
import static com.leegality.atlas.utils.Constants.CLIENT_ID;
import static com.leegality.atlas.utils.Constants.CLIENT_SECRET;
import static com.leegality.atlas.utils.Constants.E2E_PWD;
import static com.leegality.atlas.utils.Constants.E2E_USER;
import static com.leegality.atlas.utils.Constants.OAUTH_APP_LOGIN_ENDPOINT;
import static com.leegality.atlas.utils.Constants.OAUTH_LOGIN_ENDPOINT;
import static com.leegality.atlas.utils.Constants.SCOPE;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;


public class BaseApi {

    public RequestSpecification buildClientRequest() {
        String token = getClientAccessToken();
        return buildRequestWithToken(token);
    }

    public RequestSpecification buildUserRequest() {
        String token = getUserAccessToken();
        return buildRequestWithToken(token);
    }

    public RequestSpecification buildRequestWithToken(String accessToken) {
        final AllureRestAssured allureFilter = new AllureRestAssured()
                .setRequestAttachmentName("Request")
                .setResponseAttachmentName("Response");

        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .addHeader(AUTH_HEADER, BEARER + accessToken)
                .addFilter(allureFilter)
                .setBaseUri(getStandardUrl())
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setUrlEncodingEnabled(false)
                .build();

        return given(requestSpecification);
    }

    @Step("Get Client Access Token")
    public String getClientAccessToken() {
        Map<String, String> secrets = getSecrets();
        String accessToken = null;
        try {
            String clientId = secrets.get(CLIENT_ID);
            String clientSecret = secrets.get(CLIENT_SECRET);
            String clientCred = clientId + ":" + clientSecret;

            String scope = secrets.get(SCOPE);

            accessToken = getClientAccessToken(clientCred, scope);
        } catch (Exception e) {
            step("Couldn't get access token: " + e.getCause());
        }
        return accessToken;
    }

    public String getClientAccessToken(String clientCred, String scope) {
        AccessTokenDTO token =
                given()
                        .urlEncodingEnabled(true)
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .headers(AUTH_HEADER, BASIC + Base64.getEncoder().encodeToString(clientCred.getBytes()))
                        .formParam("grant_type", "client_credentials")
                        .formParam("scope", scope)
                        .when().post(OAUTH_LOGIN_ENDPOINT)
                        .then().log()
                        .ifValidationFails()
                        .statusCode(200).extract().response().getBody()
                        .as(AccessTokenDTO.class);
        return token.getAccessToken();
    }

    @Step("Get User Access Token")
    private String getUserAccessToken() {
        Map<String, String> secrets = getSecrets();
        String accessToken = null;
        try {
            String userId = secrets.get(E2E_USER);
            String pwd = secrets.get(E2E_PWD);

            String requestBody = String.format(
                    "{\"username\":\"%s\",\"password\":\"%s\",\"type\":\"q\"}",
                    userId, pwd
            );

            AccessTokenDTO token = given()
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .when()
                    .post(OAUTH_APP_LOGIN_ENDPOINT)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .extract()
                    .response()
                    .getBody()
                    .as(AccessTokenDTO.class);

            accessToken = token.getAccessToken();
        } catch (Exception e) {
            step("Couldn't get access token using user and password: " + e.getCause());
        }
        return accessToken;
    }

    @Step("Validate response status code")
    public Response validateStatusCode(Response response, Long timeInMilliSec, int statusCode) {
        // Validate the response time
        if (timeInMilliSec != null) {
            response.then().time(lessThan(timeInMilliSec), TimeUnit.MILLISECONDS);
        }
        return response.then().statusCode(statusCode).extract().response();
    }

}
