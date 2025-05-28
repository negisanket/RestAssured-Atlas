package com.leegality.atlas.api;

import com.leegality.atlas.api.testdto.request.FilterDTO;
import com.leegality.atlas.api.testdto.request.StateDTO;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.leegality.atlas.utils.Constants.UN_AUTH_E2E_PWD;
import static com.leegality.atlas.utils.Constants.UN_AUTH_E2E_USER;

/**
 * Sample implementation of BaseApi demonstrating best practices for API automation.
 * This class shows how to:
 * <ul>
 *   <li>Extend BaseApi to inherit common functionality</li>
 *   <li>Structure API endpoints and operations</li>
 *   <li>Use DTOs for request/response handling</li>
 *   <li>Implement authentication scenarios</li>
 *   <li>Handle different HTTP methods (GET, POST, DELETE, etc.)</li>
 *   <li>Use Allure annotations for reporting</li>
 * </ul>
 * <p>
 * Use this class as a reference when implementing your own API automation classes.
 */
public final class StatesApi extends BaseApi {
    private static final String STATES_PATH = "/digital-stamping/v1/states";
    private static final String STATES_PATH_BY_ID = STATES_PATH + "/%s";

    /**
     * Creates a new state with the provided details.
     * Example of:
     * - POST request with request body
     * - Using DTOs for request payload
     * - Response validation with status code
     * - Using Allure steps for test reporting
     *
     * @param payload    The state data to be created
     * @param statusCode Expected HTTP status code
     * @return Response containing the created state details
     */
    @Step("Create state request")
    public Response createState(StateDTO payload, int statusCode) {
        Response response = buildRequest().body(payload).post(STATES_PATH);
        return validateStatusCode(response, statusCode);
    }

    /**
     * Retrieves a state by its ID without authentication.
     * Example of:
     * - GET request with path parameters
     * - Handling unauthorized access scenarios
     * - Using custom authentication tokens
     * - Error response validation
     *
     * @param id         The ID of the state to retrieve
     * @param statusCode Expected HTTP status code (typically 401 for unauthorized)
     * @return Response containing the error details
     */
    @Step("Get state request with UnAuth user")
    public Response getStateByIdUnAuth(String id, int statusCode) {
        String accessToken = getUserAccessToken(UN_AUTH_E2E_USER, UN_AUTH_E2E_PWD, statusCode);
        Response response = buildRequestWithToken(accessToken).get(String.format(STATES_PATH_BY_ID, id));
        return validateStatusCode(response, statusCode);
    }

    /**
     * Deletes a state by its ID.
     * Example of:
     * - DELETE request with path parameters
     * - Direct response assertion
     * - Void return type for destructive operations
     *
     * @param id         The ID of the state to delete
     * @param statusCode Expected HTTP status code
     */
    @Step("Delete state request")
    public void deleteStateById(String id, int statusCode) {
        buildRequest().delete(String.format(STATES_PATH_BY_ID, id)).then().statusCode(statusCode);
    }

    /**
     * Searches states based on filter criteria.
     * Example of:
     * - POST request for search operation
     * - Complex request payload with multiple parameters
     * - Using filter DTO for search criteria
     * - Handling paginated responses
     *
     * @param filterDTO  The filter criteria for searching states
     * @param statusCode Expected HTTP status code
     * @return Response containing the search results
     */
    @Step("Search states request")
    public Response searchStates(FilterDTO filterDTO, int statusCode) {
        Response response = buildRequest().body(filterDTO).post(STATES_PATH + "/search");
        return validateStatusCode(response, statusCode);
    }
}
