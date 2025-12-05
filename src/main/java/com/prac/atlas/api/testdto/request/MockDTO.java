package com.prac.atlas.api.testdto.request;

//{
//        "id": 101,
//        "name": "Test Group",
//        "tags": ["alpha", "beta", "gamma"],
//        "active": true
//        }

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
public class MockDTO {
    private Integer id;
    private String name;
    private List<String> tags;
    private Boolean active;
}

