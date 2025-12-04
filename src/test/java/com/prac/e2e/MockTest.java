package com.prac.e2e;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.prac.atlas.api.adapter.MockAdapter;
import com.prac.atlas.api.adapter.MockDatabase;

import com.prac.atlas.api.factory.MockPayloadFactory;
import com.prac.atlas.api.testdto.request.MockDTO;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.testng.Assert.assertEquals;

public class MockTest {

    private MockAdapter adapter;
   private static WireMockServer wireMock;

    @BeforeClass
    public void setup() {
        wireMock = new WireMockServer(options().port(9090));
        wireMock.start();
        WireMock.configureFor("localhost", 9090);
    }

    @AfterClass
    public void teardown() {
        wireMock.stop();
    }


    @BeforeMethod
    public void resetEnvironment() {
        MockDatabase.clear();
        adapter = new MockAdapter();
    }

    @Test
    public void createMockAndVerifyTotalCountInList() {
        MockDTO payload = MockPayloadFactory.defaultPayload();

        adapter.create(payload); // POST /mock (in-memory)

        Response res = adapter.getAll(); // GET /mock
        assertEquals(res.statusCode(), 200);
        assertEquals(res.jsonPath().getInt("total"), 1);
    }

    @Test
    public void createMultipleMocksAndVerifyCount() {
        adapter.create(MockPayloadFactory.defaultPayload());
        adapter.create(MockPayloadFactory.defaultPayload());
        adapter.create(MockPayloadFactory.defaultPayload());

        Response res = adapter.getAll();
        assertEquals(res.statusCode(), 200);
        assertEquals(res.jsonPath().getInt("total"), 3);
    }

    @Test
    public void createMockWithOverrides() {
        MockDTO request = MockPayloadFactory.payloadWithOverrides(
                java.util.Map.of(
                        "name", "Sanket",
                        "email", "sanket@test.com",
                        "age", 40
                )
        );

        Response res = adapter.create(request);
        assertEquals(res.statusCode(), 201);

        Response list = adapter.getAll();
        assertEquals(list.jsonPath().getInt("total"), 1);
        assertEquals(list.jsonPath().getString("items[0].name"), "Sanket");
        assertEquals(list.jsonPath().getString("items[0].email"), "sanket@test.com");
        assertEquals(list.jsonPath().getInt("items[0].age"), 40);
    }

    @Test
    public void updateMockAndVerifyChange() {
        MockDTO payload = MockPayloadFactory.defaultPayload();
        Response createRes = adapter.create(payload);
        int id = createRes.jsonPath().getInt("id");

        MockDTO updated = MockDTO.builder()
                .id(id)
                .name("Updated Name")
                .email("updated@test.com")
                .age(99)
                .build();

        Response updateRes = adapter.update(updated);
        assertEquals(updateRes.statusCode(), 200);

        Response list = adapter.getAll();
        assertEquals(list.jsonPath().getInt("total"), 1);
        assertEquals(list.jsonPath().getString("items[0].name"), "Updated Name");
    }

    @Test
    public void deleteMockAndVerifyCount() {
        adapter.create(MockPayloadFactory.defaultPayload());
        adapter.create(MockPayloadFactory.defaultPayload());
        Response listBefore = adapter.getAll();
        assertEquals(listBefore.jsonPath().getInt("total"), 2);

        int idToDelete = listBefore.jsonPath().getInt("items[0].id");
        Response deleteRes = adapter.delete(idToDelete);
        assertEquals(deleteRes.statusCode(), 200);

        Response listAfter = adapter.getAll();
        assertEquals(listAfter.jsonPath().getInt("total"), 1);
    }

}
