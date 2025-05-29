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

import static com.leegality.atlas.utils.CommonMethods.getSecrets;
import static com.leegality.atlas.utils.CommonMethods.getStandardUrl;
import static com.leegality.atlas.utils.Constants.AUTH_HEADER;
import static com.leegality.atlas.utils.Constants.BASIC;
import static com.leegality.atlas.utils.Constants.BEARER;
import static com.leegality.atlas.utils.Constants.CLIENT_ID;
import static com.leegality.atlas.utils.Constants.CLIENT_SECRET;
import static com.leegality.atlas.utils.Constants.DEFAULT_E2E_PWD;
import static com.leegality.atlas.utils.Constants.DEFAULT_E2E_USER;
import static com.leegality.atlas.utils.Constants.OAUTH_LOGIN_ENDPOINT;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

/**
 * Base API class that provides common functionality for API interactions.
 * This class includes methods for building requests, handling authentication,
 * and managing common API operations.
 */
public class BaseApi {

    /**
     * Builds a basic request specification without authentication.
     *
     * @return RequestSpecification configured with base settings
     */
    public RequestSpecification buildRequest() {
        String token = getUserAccessToken(DEFAULT_E2E_USER, DEFAULT_E2E_PWD, 200);
        return buildRequestWithToken(token);
    }

    /**
     * Builds a request specification with Bearer token authentication.
     *
     * @param accessToken The access token to be used for authentication
     * @return RequestSpecification configured with authentication and base settings
     */
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

    /**
     * Retrieves an access token for a user using username and password.
     *
     * @param username   The user's username
     * @param password   The user's password
     * @param statusCode Expected HTTP status code
     * @return String containing the access token
     */
    @Step("Get User Access Token")
    public String getUserAccessToken(String username, String password, int statusCode) {
        Map secrets = getSecrets();
        String accessToken = null;
        try {
            String clientId = secrets.get(CLIENT_ID).toString();
            String clientSecret = secrets.get(CLIENT_SECRET).toString();
            String clientCred = clientId + ":" + clientSecret;

            String userId = secrets.get(username).toString();
            String pwd = secrets.get(password).toString();
            accessToken = getAccessToken(clientCred, userId, pwd, statusCode);
        } catch (Exception e) {
            step("Couldn't get access token: " + e.getCause());
        }
        return accessToken;
    }

    /**
     * Retrieves an access token using client credentials and user authentication.
     *
     * @param clientCred Base64 encoded client credentials
     * @param userId     The user's ID
     * @param pwd        The user's password
     * @param statusCode Expected HTTP status code
     * @return String containing the access token
     */
    public String getAccessToken(String clientCred, String userId, String pwd, int statusCode) {
        AccessTokenDTO token =
                given()
                        .urlEncodingEnabled(true)
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .headers(AUTH_HEADER, BASIC + Base64.getEncoder().encodeToString(clientCred.getBytes()))
                        .formParam("grant_type", "password")
                        .formParam("username", userId)
                        .formParam("password", pwd)
                        .when().post(OAUTH_LOGIN_ENDPOINT)
                        .then().log()
                        .ifValidationFails()
                        .statusCode(statusCode).extract().response().getBody()
                        .as(AccessTokenDTO.class);
        return token.getAccessToken();
    }

    /**
     * Validates that a response has the expected status code.
     *
     * @param response   The Response object to validate
     * @param statusCode The expected HTTP status code
     * @return Response object for chaining
     * @throws AssertionError if the status code doesn't match
     */
    @Step("Validate response status code")
    public Response validateStatusCode(Response response, int statusCode) {
        return response.then().statusCode(statusCode).extract().response();
    }

}
