package com.prac.atlas.api.factory;

import com.prac.atlas.api.testdto.request.MockDTO;
import java.util.List;

public class MockPayloadFactory {

    public static MockDTO getDefaultMockPayload() {
        return MockDTO.builder()
                .id(101)
                .name("Sample User")
                .tags(List.of("tag1", "tag2"))
                .active(true)
                .build();
    }

    public static MockDTO getCustomMockPayload(Integer id, String name, List<String> tags, Boolean active) {
        return MockDTO.builder()
                .id(id)
                .name(name)
                .tags(tags)
                .active(active)
                .build();
    }
}
