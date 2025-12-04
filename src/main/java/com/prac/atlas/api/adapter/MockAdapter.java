package com.prac.atlas.api.adapter;

import com.prac.atlas.api.testdto.request.MockDTO;
import io.restassured.builder.ResponseBuilder;
import io.restassured.response.Response;

import java.util.List;

public class MockAdapter {

    public Response create(MockDTO dto) {
        int id = MockDatabase.insert(dto);
        String body = """
            {
              "id": %d,
              "status": "created"
            }
            """.formatted(id);

        return new ResponseBuilder()
                .setStatusCode(201)
                .setBody(body)
                .build();
    }

    public Response getAll() {
        List<MockDTO> list = MockDatabase.fetchAll();

        StringBuilder items = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            MockDTO d = list.get(i);
            items.append("""
              {"id": %d, "name": "%s", "email": "%s", "age": %d}
            """.formatted(d.getId(), d.getName(), d.getEmail(), d.getAge()));
            if (i < list.size() - 1) items.append(",");
        }
        items.append("]");

        String body = """
            {
              "total": %d,
              "items": %s
            }
            """.formatted(list.size(), items);

        return new ResponseBuilder()
                .setStatusCode(200)
                .setBody(body)
                .build();
    }
    public Response update(MockDTO dto) {
        boolean exists = MockDatabase.update(dto);

        if (!exists) {
            return new ResponseBuilder()
                    .setStatusCode(404)
                    .setBody("{\"error\": \"not found\"}")
                    .build();
        }

        String body = """
                {
                  "id": %d,
                  "status": "updated"
                }
                """.formatted(dto.getId());

        return new ResponseBuilder()
                .setStatusCode(200)
                .setBody(body)
                .build();
    }

    public Response delete(Integer id) {
        boolean removed = MockDatabase.delete(id);

        if (!removed) {
            return new ResponseBuilder()
                    .setStatusCode(404)
                    .setBody("{\"error\": \"not found\"}")
                    .build();
        }

        String body = """
                {
                  "id": %d,
                  "status": "deleted"
                }
                """.formatted(id);

        return new ResponseBuilder()
                .setStatusCode(200)
                .setBody(body)
                .build();
    }
}
