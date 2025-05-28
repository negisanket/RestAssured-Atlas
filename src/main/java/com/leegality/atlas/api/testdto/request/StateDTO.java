package com.leegality.atlas.api.testdto.request;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object for state creation/update requests.
 * Contains the necessary fields to create or update a state.
 */
@Getter
@Setter
public final class StateDTO {

    /** The name of the state. */
    @JsonProperty
    public String name;

    /** The code identifier for the state. */
    @JsonProperty
    public String code;

    /** Whether the state is enabled or not. */
    @JsonProperty
    public Boolean enabled;
}
