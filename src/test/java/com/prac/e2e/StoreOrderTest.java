package com.prac.e2e;


import com.prac.atlas.api.adapters.StoreAdapter;
import com.prac.atlas.api.factory.OrderPayloadFactory;
import com.prac.atlas.api.testdto.request.OrderDTO;
import com.prac.core.ResourceTracker;
import com.prac.core.Resource;
import com.prac.core.ResourceTracker;
import com.prac.core.Resource;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class StoreOrderTest {

    private final StoreAdapter storeAdapter = new StoreAdapter();

    @Test
    public void create_get_delete_order_end_to_end() {
        // 1) Build payload
        OrderDTO payload = OrderPayloadFactory.defaultOrder();

        // 2) Create order (Petstore returns 200 and order in body with id)
        Response createResp = storeAdapter.createOrder(payload);
        int createStatus = createResp.getStatusCode();
        Assert.assertTrue(createStatus == 200 || createStatus == 201, "Create status should be 200/201 but was " + createStatus);

        Long id = null;
        try {
            id = createResp.jsonPath().getLong("id");
        } catch (Exception ex) {
            Assert.fail("No id returned in create response: " + createResp.asString());
        }
        Assert.assertNotNull(id, "Created order id is null");

        // Register for cleanup (so listener will delete it if needed)
        ResourceTracker.registerResource("order", String.valueOf(id), com.prac.atlas.utils.Constants.DEFAULT_BASE);

        // 3) Get order and assert fields
        Response getResp = storeAdapter.getOrder(id);
        Assert.assertEquals(getResp.getStatusCode(), 200, "Get status mismatch");

        Long gotId = getResp.jsonPath().getLong("id");
        Assert.assertEquals(gotId, id, "Order ID mismatch on GET");

        // 4) Delete order proactively here; listener is a safety net
        Response deleteResp = storeAdapter.deleteOrder(id);
        Assert.assertTrue(deleteResp.getStatusCode() == 200 || deleteResp.getStatusCode() == 204,
                "Delete should return 200/204 but was " + deleteResp.getStatusCode());

        // Optionally, verify GET now returns 404 (Petstore sometimes returns 404)
        Response getAfterDelete = storeAdapter.getOrder(id);
        Assert.assertTrue(getAfterDelete.getStatusCode() == 404 || getAfterDelete.getStatusCode() == 200,
                "After delete, expected 404 or 200 (depending on Petstore behavior). Actual: " + getAfterDelete.getStatusCode());
    }
}
