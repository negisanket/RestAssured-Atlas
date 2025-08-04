package com.leegality.atlas.api;

import com.leegality.atlas.api.commondto.response.AccessTokenDTO;
import com.leegality.atlas.api.commondto.response.LoginResponseDTO;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Base64;
import java.util.HashMap;
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
import static com.leegality.atlas.utils.Constants.MAIN_APP_LOGIN_ENDPOINT;
import static com.leegality.atlas.utils.Constants.OAUTH_APP_LOGIN_ENDPOINT;
import static com.leegality.atlas.utils.Constants.OAUTH_LOGIN_ENDPOINT;
import static com.leegality.atlas.utils.Constants.SCOPE;
import static com.leegality.atlas.utils.Constants.X_AUTH_TOKEN;
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

    public RequestSpecification buildMainAppUserRequest() {
        String token = getMainAppUserAccessToken();
        return buildMainAppRequestWithToken(token);
    }

    public RequestSpecification buildMainAppRequestWithToken(String accessToken) {
        return buildRequestWithToken(accessToken, X_AUTH_TOKEN, "");
    }

    public RequestSpecification buildRequestWithToken(String accessToken) {
        return buildRequestWithToken(accessToken, AUTH_HEADER, BEARER);
    }

    private RequestSpecification buildRequestWithToken(String accessToken, String headerName, String headerPrefix) {
        final AllureRestAssured allureFilter = new AllureRestAssured()
                .setRequestAttachmentName("Request")
                .setResponseAttachmentName("Response");

        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .addHeader(headerName, headerPrefix + accessToken)
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
    public String getUserAccessToken() {
        Map<String, String> secrets = getSecrets();
        String accessToken = null;
        try {
            String userId = secrets.get(E2E_USER);
            String pwd = secrets.get(E2E_PWD);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("username", userId);
            requestBody.put("password", pwd);
            requestBody.put("type", "q");

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

    @Step("Get Main App User Access Token")
    public String getMainAppUserAccessToken() {
        Map<String, String> secrets = getSecrets();
        String accessToken = null;
        try {
            String userId = secrets.get(E2E_USER);
            String pwd = secrets.get(E2E_PWD);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("username", userId);
            requestBody.put("password", pwd);

            LoginResponseDTO token = given()
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .when()
                    .post(MAIN_APP_LOGIN_ENDPOINT)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .extract()
                    .response()
                    .getBody()
                    .as(LoginResponseDTO.class);

            accessToken = token.getData().getAccessToken();
        } catch (Exception e) {
            step("Couldn't get access token using user and password: " + e.getCause());
        }
        return accessToken;
    }

    @Step("Validate response time and status code")
    public Response validateStatusCode(Response response, Long timeInMilliSec, int statusCode) {
        // Validate the response time
        if (timeInMilliSec != null) {
            response.then().time(lessThan(timeInMilliSec), TimeUnit.MILLISECONDS);
        }
        return response.then().statusCode(statusCode).extract().response();
    }

    @Step("Validate response status code")
    public Response validateStatusCode(Response response, int statusCode) {
        return response.then().statusCode(statusCode).extract().response();
    }

}
