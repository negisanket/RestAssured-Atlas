package com.leegality.atlas.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static com.leegality.atlas.utils.Constants.AWS_REGION;
import static com.leegality.atlas.utils.Constants.SECRET_BUCKET;
import static io.qameta.allure.Allure.step;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Utility class providing common methods used across the application.
 * Contains static helper methods for environment variables and secrets management.
 */
public final class CommonMethods {

    static ObjectMapper objectMapper = new ObjectMapper();

    private CommonMethods() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets the STANDARD_URL from environment variables.
     * If STANDARD_URL is not set, defaults to "<a href="https://dev-gateway.leegality.com">...</a>".
     *
     * @return STANDARD_URL from environment variables or default value
     */
    public static String getStandardUrl() {
        return getEnvVariable("STANDARD_URL", "https://dev-gateway.leegality.com");
    }

    /**
     * Gets the DEFAULT_ENVIRONMENT from environment variables.
     * If DEFAULT_ENVIRONMENT is not set, defaults to "dev".
     *
     * @return DEFAULT_ENVIRONMENT from environment variables or default value
     */
    public static String getEnvironment() {
        return getEnvVariable("DEFAULT_ENVIRONMENT", "dev");
    }

    /**
     * Gets the application name from environment variables.
     *
     * @return Application name from environment variables or default value
     */
    public static String getAppName() {
        return getEnvVariable("E2E_APP_NAME");
    }

    /**
     * Retrieves secrets from AWS Secrets Manager.
     *
     * @return Map containing the retrieved secrets, where both keys and values are Strings
     */
    public static Map<String, String> getSecrets() {
        String secret;
        try (SecretsManagerClient client = SecretsManagerClient.builder()
                .region(Region.of(AWS_REGION))
                .build()) {

            String secretsBucket = SECRET_BUCKET + getAppName();
            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(secretsBucket)
                    .build();

            step("SecretBucket: " + secretsBucket);
            secret = client.getSecretValue(getSecretValueRequest).secretString();
            return objectMapper.readValue(secret, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets an environment variable with a default fallback value.
     *
     * @param variable     The name of the environment variable
     * @param defaultValue The default value to return if the variable is not set
     * @return The value of the environment variable or the default value
     */
    public static String getEnvVariable(String variable, String defaultValue) {
        String value = System.getenv(variable);
        return isNotBlank(value) ? value : defaultValue;
    }

    /**
     * Retrieves the value of an environment variable. If the variable is not set or is blank,
     * throws a RuntimeException.
     *
     * @param variable the name of the environment variable to retrieve
     * @return the value of the environment variable if it exists and is not blank
     * @throws RuntimeException if the environment variable is not set or is blank
     * @see System#getenv(String)
     * @see org.apache.commons.lang3.StringUtils#isNotBlank(CharSequence)
     */
    public static String getEnvVariable(String variable) {
        String value = System.getenv(variable);
        if (isNotBlank(value)) {
            return value;
        } else {
            throw new RuntimeException("Missing environment variable: " + variable);
        }
    }

    /**
     * Reads error messages from the properties file.
     * Loads error messages from src/test/resources/message.properties file into a Properties object.
     *
     * @return Properties object containing error messages
     * @throws RuntimeException if the properties file cannot be read
     */
    public static Properties readErrorMsgPropertiesFile() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/test/resources/message.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
