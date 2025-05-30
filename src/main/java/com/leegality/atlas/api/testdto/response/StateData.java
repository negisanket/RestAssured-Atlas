package com.leegality.atlas.api.testdto.response;

/**
 * Data Transfer Object representing state data in API responses.
 * Contains detailed information about a state.
 */
public final class StateData {

    /** Unique identifier for the state. */
    public String id;

    /** Timestamp when the state was created. */
    public String createdAt;

    /** Timestamp when the state was last updated. */
    public String updatedAt;

    /** The name of the state. */
    public String name;

    /** The code identifier for the state. */
    public String code;

    /** Whether the state is enabled or not. */
    public Boolean enabled;

    /** Version number for optimistic locking. */
    public int version;
}
