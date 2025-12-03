package com.leegality.atlas.utils;

import static com.leegality.atlas.utils.CommonMethods.getEnvironment;


public final class Constants {

    public static final String AWS_REGION = "ap-south-1";

    public static final String AUTH_HEADER = "Authorization";

    public static final String X_AUTH_TOKEN = "X-Auth-Token";

    public static final String BEARER = "Bearer ";

    public static final String BASIC = "Basic ";

    public static final String OAUTH_LOGIN_ENDPOINT = String.format("https://%s-gateway.leegality.com/auth/oauth2/token",
            getEnvironment());

    public static final String OAUTH_APP_LOGIN_ENDPOINT = String.format("https://%s-gateway.leegality.com/auth/app/login",
            getEnvironment());

    public static final String MAIN_APP_LOGIN_ENDPOINT = String.format("https://%s.leegality.com/api/v2/login",
            getEnvironment());

    public static final String CLIENT_ID = "CLIENT_ID";

    public static final String SCOPE = "SCOPE";

    public static final String CLIENT_SECRET = "CLIENT_SECRET";

    public static final String E2E_USER = "E2E_USER";

    public static final String E2E_PWD = "E2E_PWD";

    public static final String SECRET_BUCKET = String.format("e2e/%s/", getEnvironment());


    public static final String MOCK_PATH = "/states";
    public static final String MOCK_PATH_BY_ID = MOCK_PATH + "/%s";


    private Constants() {
        // Private constructor to prevent instantiation
    }

}
