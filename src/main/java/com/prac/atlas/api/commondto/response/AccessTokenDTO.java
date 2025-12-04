package com.prac.atlas.api.commondto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for OAuth access token responses.
 * Contains the access token and related authentication information.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenDTO {
    /**
     * The access token string
     */
    @JsonProperty("access_token")
    private String accessToken;
}
