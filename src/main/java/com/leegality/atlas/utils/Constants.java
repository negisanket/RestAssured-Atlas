package com.leegality.atlas.utils;

import static com.leegality.atlas.utils.CommonMethods.getEnvVariable;

/**
 * Constants class containing configuration values and environment variables
 * used throughout the test automation framework.
 */
public final class Constants {

    /** Application name from environment variables, defaults to "digitalStamping". */
    public static final String APP_NAME = getEnvVariable("APP_NAME");

    /** Environment name from environment variables, defaults to "dev". */
    public static final String ENVIRONMENT = getEnvVariable("DEFAULT_ENVIRONMENT", "dev");

    /** Standard API URL for the application. */
    public static final String STANDARD_URL =
            getEnvVariable("STANDARD_URL", "https://dev-gateway.leegality.com");

    /** AWS region for AWS services. */
    public static final String AWS_REGION = "ap-south-1";

    /** Authorization header name. */
    public static final String AUTH_HEADER = "Authorization";

    /** Bearer token prefix. */
    public static final String BEARER = "Bearer ";

    /** Basic auth prefix. */
    public static final String BASIC = "Basic ";

    /** OAuth2 token endpoint URL. */
    public static final String OAUTH_LOGIN_ENDPOINT = String.format("https://%s-gateway.leegality.com/auth/oauth2/token",
            ENVIRONMENT);

    /** Client ID environment variable name. */
    public static final String CLIENT_ID = "CLIENT_ID";

    /** Client secret environment variable name. */
    public static final String CLIENT_SECRET = "CLIENT_SECRET";

    /** Default E2E test user environment variable name. */
    public static final String DEFAULT_E2E_USER = "E2E_USER";

    /** Default E2E test password environment variable name. */
    public static final String DEFAULT_E2E_PWD = "E2E_PWD";

    /** Unauthorized E2E test user environment variable name. */
    public static final String UN_AUTH_E2E_USER = "UN_AUTH_E2E_USER";

    /** Unauthorized E2E test password environment variable name. */
    public static final String UN_AUTH_E2E_PWD = "UN_AUTH_E2E_PWD";

    /** S3 bucket path for E2E test artifacts. */
    public static final String SECRET_BUCKET = String.format("e2e/%s/%s", ENVIRONMENT, APP_NAME);

    private Constants() {
        // Private constructor to prevent instantiation
    }

}
