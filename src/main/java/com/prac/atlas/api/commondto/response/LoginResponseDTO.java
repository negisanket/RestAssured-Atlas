package com.prac.atlas.api.commondto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class LoginResponseDTO {
    private Data data;
    private Message[] messages;
    private int status;

    @Getter
    public static class Data {
        private String username;
        private String name;
        private String email;
        @JsonProperty("access_token")
        private String accessToken;
        private Boolean passwordCreated;
        private String plan;
        private String sandboxValidity;
        private String organizationId;
        private String departmentId;
        private String organizationUserId;
        private String brandPicUrl;
        private String brandName;
        private String userRole;
    }

    @Getter
    public static class Message {
        private String code;
        private String message;
    }
}
