package com.prac.atlas.api.adapters;

import com.prac.atlas.api.testdto.request.OrderDTO;
import com.prac.atlas.utils.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Adapter for Petstore /store/order endpoints.
 * Uses per-call baseUri (defaults to Constants.DEFAULT_BASE).
 */
public class StoreAdapter {

    private static final String CREATE_PATH = "/store/order";
    private static final String GET_PATH = "/store/order/{orderId}";
    private static final String DELETE_PATH = "/store/order/{orderId}";

    public Response createOrder(OrderDTO payload) {
        return createOrderWithBase(Constants.DEFAULT_BASE, payload);
    }

    public Response createOrderWithBase(String baseUri, OrderDTO payload) {
        return given()
                .baseUri(baseUri)
                .contentType("application/json")
                .body(payload)
                .when()
                .post(CREATE_PATH)
                .then()
                .extract()
                .response();
    }

    public Response getOrder(Long orderId) {
        return getOrderWithBase(Constants.DEFAULT_BASE, orderId);
    }

    public Response getOrderWithBase(String baseUri, Long orderId) {
        return given()
                .baseUri(baseUri)
                .pathParam("orderId", orderId)
                .when()
                .get(GET_PATH)
                .then()
                .extract()
                .response();
    }

    public Response deleteOrder(Long orderId) {
        return deleteOrderWithBase(Constants.DEFAULT_BASE, orderId);
    }

    public Response deleteOrderWithBase(String baseUri, Long orderId) {
        return given()
                .baseUri(baseUri)
                .pathParam("orderId", orderId)
                .when()
                .delete(DELETE_PATH)
                .then()
                .extract()
                .response();
    }
}
