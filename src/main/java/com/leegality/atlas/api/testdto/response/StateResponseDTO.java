package com.leegality.atlas.api.testdto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for state API responses.
 * Contains the response code, message, and state data.
 */
@Getter
@Setter
public final class StateResponseDTO {

    /** Response code indicating the status of the operation. */
    public String code;

    /** Response message providing details about the operation result. */
    public String message;

    /** The state data returned in the response. */
    public StateData data;
}
