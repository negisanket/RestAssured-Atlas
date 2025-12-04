package com.prac.atlas.api.factory;

import com.prac.atlas.api.testdto.request.MockDTO;

import java.util.Map;

public final class MockPayloadFactory {

    private MockPayloadFactory() {}

    public static MockDTO defaultPayload() {
        return MockDTO.builder()
                .name("Default Name")
                .email("default@example.com")
                .age(25)
                .build();
    }

    public static MockDTO payloadWithOverrides(Map<String, Object> overrides) {
        MockDTO dto = defaultPayload();
        overrides.forEach((k, v) -> {
            switch (k) {
                case "name" -> dto.setName((String) v);
                case "email" -> dto.setEmail((String) v);
                case "age" -> dto.setAge((Integer) v);
            }
        });
        return dto;
    }
}
