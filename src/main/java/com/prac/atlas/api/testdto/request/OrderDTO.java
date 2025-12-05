package com.prac.atlas.api.testdto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderDTO {

    /**
     * order id (server may return generated id)
     */
    @JsonProperty("id")
    private final Long id;

    @JsonProperty("petId")
    private final Long petId;

    @JsonProperty("quantity")
    private final Integer quantity;

    /**
     * ISO-8601 string. e.g. Instant.now().toString()
     */
    @JsonProperty("shipDate")
    private final String shipDate;

    /**
     * Order status: "placed", "approved", "delivered"
     */
    @JsonProperty("status")
    private final String status;

    @JsonProperty("complete")
    private final Boolean complete;
}
