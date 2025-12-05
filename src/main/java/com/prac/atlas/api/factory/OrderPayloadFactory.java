package com.prac.atlas.api.factory;

import com.prac.atlas.api.testdto.request.OrderDTO;

import java.time.Instant;

public final class OrderPayloadFactory {

    private OrderPayloadFactory() {}

    /**
     * Create a default order payload. id = null to let server assign.
     */
    public static OrderDTO defaultOrder() {
        return OrderDTO.builder()
                .id(null)
                .petId(0L)
                .quantity(1)
                .shipDate(Instant.now().toString())
                .status("placed")
                .complete(true)
                .build();
    }

    /**
     * Customizable helper
     */
    public static OrderDTO orderWith(Long id, Long petId, Integer quantity, String shipDate, String status, Boolean complete) {
        return OrderDTO.builder()
                .id(id)
                .petId(petId)
                .quantity(quantity)
                .shipDate(shipDate)
                .status(status)
                .complete(complete)
                .build();
    }
}
