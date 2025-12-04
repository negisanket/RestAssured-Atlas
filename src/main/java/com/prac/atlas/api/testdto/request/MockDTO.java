package com.prac.atlas.api.testdto.request;




import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MockDTO {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
}
