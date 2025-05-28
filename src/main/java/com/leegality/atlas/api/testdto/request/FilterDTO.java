package com.leegality.atlas.api.testdto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for filtering API requests.
 * Contains filter criteria for API endpoints that support filtering.
 */
@Getter
@Setter
public final class FilterDTO {

    private String name;
    private String code;
    private Boolean enabled;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortOrder;
}
