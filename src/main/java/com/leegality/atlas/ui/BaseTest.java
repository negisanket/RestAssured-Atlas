package com.leegality.atlas.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.leegality.atlas.utils.CommonMethods.getEnvVariable;

/**
 * Base test class that provides common functionality for all test classes.
 * This class handles WebDriver setup, test configuration, and common test operations.
 * It supports both local and remote (LambdaTest) test execution.
 */
public class BaseTest {

    /** Logger instance for test logging. */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /** WebDriver instance for browser automation. */
    public static WebDriver driver;

    /** Status of the LambdaTest execution. */
    public static String lambdaTestStatus = "failed";

    /** LambdaTest username from environment variables. */
    public static String ltUsername = System.getenv("LT_USERNAME");

    /** LambdaTest access key from environment variables. */
    public static String ltAccessKey = System.getenv("LT_ACCESS_KEY");

    /**
     * Sets up the test environment before each test method.
     * Initializes the WebDriver based on the environment configuration.
     */
    @BeforeMethod(alwaysRun = true)
    @Step("Initialize Driver")
    public void setUp() {
        if (StringUtils.equalsIgnoreCase(getEnvVariable("ENV", "local"), "local")) {
            autoOpenLocalChrome(); //run locally on chrome
        } else {
            autoOpenBrowser(); //run on LambdaTest HyperExecute
        }
        setBasePageDriver(driver);
    }

    /**
     * Sets the WebDriver instance in the BasePage class.
     * @param driver WebDriver instance to set
     * @throws RuntimeException if driver is not initialized
     */
    private void setBasePageDriver(WebDriver driver) {
        if (driver != null) {
            BasePage.driver = driver;
        } else {
            throw new RuntimeException("Driver not initialized");
        }
    }

    /**
     * Tears down the test environment after each test method.
     * Updates LambdaTest status and quits the WebDriver.
     * @param result The test result
     */
    @AfterMethod(alwaysRun = true)
    @Step("Quit Driver")
    public void tearDown(ITestResult result) {
        if (result.isSuccess()) {
            lambdaTestStatus = "passed";
        }

        if (!StringUtils.equalsIgnoreCase(getEnvVariable("ENV", "local"), "local")) {
            ((JavascriptExecutor) driver).executeScript("lambda-status=" + lambdaTestStatus);
        }

        driver.quit();
    }

    /**
     * Loads test properties from the environment configuration file.
     * @return Properties instance containing test configuration
     */
    public static Properties envLocalProperties() {
        Properties properties = new Properties();
        String envFile = "src/test/resources/env_local.properties";
        File file = new File(envFile);

        try {
            if (!file.exists()) {
                file.createNewFile();
                try (FileWriter myWriter = new FileWriter(envFile)) {
                    myWriter.write("environment=");
                }
            }
            properties.load(new FileInputStream(envFile));
        } catch (IOException e) {
            return null;
        }

        return properties;
    }

    /**
     * Initializes Chrome WebDriver for local test execution.
     */
    public void autoOpenLocalChrome() {
        try {
            // Set up ChromeDriver using WebDriverManager
            WebDriverManager.chromedriver().setup();
            // Create ChromeOptions
            ChromeOptions options = new ChromeOptions();
            // Create a new ChromeDriver with the options
            driver = WebDriverManager.chromedriver().capabilities(options).create();
            logger.info("Started local session");
            driver.manage().window().maximize();
        } catch (Exception e) {
            logger.error("Driver could not be created: ", e);
        }
    }

    /**
     * Initializes WebDriver for remote test execution on LambdaTest.
     */
    public void autoOpenBrowser() {
        AbstractDriverOptions<?> browserOptions = getBrowserOptions();

        if (ltUsername == null || ltAccessKey == null) {
            throw new RuntimeException("You need to specify LT_USERNAME and LT_ACCESS_KEY");
        }

        try {
            String remoteAddress = "https://" + ltUsername + ":" + ltAccessKey + "@hub.lambdatest.com/wd/hub";
            driver = new RemoteWebDriver(new java.net.URI(remoteAddress).toURL(), browserOptions);
            logger.info("Started remote webdriver session: {}", remoteAddress);
            driver.manage().window().maximize();
        } catch (Exception e) {
            logger.error("Remote webdriver session could not be opened: ", e);
        }
    }

    private static AbstractDriverOptions<?> getBrowserOptions() {
        String browserName = System.getProperty("LT_BROWSER", "MicrosoftEdge");
        String platform = System.getProperty("LT_PLATFORM", "Windows 10");

        AbstractDriverOptions<?> browserOptions = getBrowserOptions(browserName);

        // Create LT:Options map
        Map<String, Object> ltOptions = getLtOptions(platform, browserName);

        // Set LT:Options
        browserOptions.setCapability("LT:Options", ltOptions);
        return browserOptions;
    }

    private static AbstractDriverOptions<?> getBrowserOptions(String browserName) {
        // Create browser-specific options based on the browser name
        return switch (browserName.toLowerCase()) {
            case "microsoftedge" -> new EdgeOptions();
            case "firefox" -> new FirefoxOptions();
            case "safari" -> new SafariOptions();
            default -> new ChromeOptions();
        };
    }

    private static Map<String, Object> getLtOptions(String platform, String browserName) {
        Map<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("username", ltUsername);
        ltOptions.put("accessKey", ltAccessKey);
        ltOptions.put("build", "Remote_Session_" + ltUsername);
        ltOptions.put("platform", platform);
        ltOptions.put("browserName", browserName);
        ltOptions.put("version", "latest");
        ltOptions.put("tunnel", "false");
        ltOptions.put("network", "false");
        ltOptions.put("console", "true");
        ltOptions.put("visual", "true");
        ltOptions.put("terminal", "true");
        ltOptions.put("devicelog", "true");
        ltOptions.put("w3c", true);
        return ltOptions;
    }

}
