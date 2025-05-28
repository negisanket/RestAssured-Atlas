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
     * Retrieves secrets from AWS Secrets Manager.
     *
     * @return Map containing the retrieved secrets, where both keys and values are Strings
     */
    public static Map<String, String> getSecrets() {
        String secret;
        try (SecretsManagerClient client = SecretsManagerClient.builder()
                .region(Region.of(AWS_REGION))
                .build()) {

            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(SECRET_BUCKET)
                    .build();

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
     * @param variable The name of the environment variable
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
     *
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
