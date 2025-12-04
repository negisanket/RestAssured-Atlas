package com.prac.atlas.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Base page class that provides common functionality for all page objects.
 * This class includes methods for interacting with web elements, handling waits,
 * and performing common UI operations.
 */
public class BasePage {

    /** The WebDriver instance used for browser automation. */
    public static WebDriver driver;

    /**
     * Constructor for BasePage.
     * @param driver WebDriver instance to be used for browser automation
     */
    public BasePage(WebDriver driver) {
        BasePage.driver = driver;
    }

    /**
     * Opens the specified URL in the browser.
     * @param url The URL to navigate to
     */
    public void openURL(String url) {
        driver.get(url);
    }

    /**
     * Enters text into a web element.
     * @param locator The By locator for the element
     * @param text The text to enter
     */
    public void enterText(By locator, String text) {
        WebElement element = driver.findElement(locator);
        element.sendKeys(text);
    }

    /**
     * Clears text from a web element.
     * @param locator The By locator for the element
     */
    public void clearText(By locator) {
        WebElement element = driver.findElement(locator);
        element.clear();
    }

    /**
     * Clicks on a button element.
     * @param locator The By locator for the button
     */
    public void clickOnButton(By locator) {
        WebElement element = driver.findElement(locator);
        element.click();
    }

    /**
     * Gets text from a web element.
     * @param locator The By locator for the element
     * @return The text content of the element
     */
    public String getText(By locator) {
        WebElement element = driver.findElement(locator);
        return element.getText();
    }

    /**
     * Selects a value from a dropdown element.
     * @param locator The By locator for the dropdown
     * @param text The text value to select
     */
    public void selectValueOnDropDown(By locator, String text) {
        Select selectValue = new Select(driver.findElement(locator));
        selectValue.selectByVisibleText(text);
    }

    /**
     * Selects a value from a checkbox group.
     * @param chkBoxCollection The By locator for the checkbox collection
     * @param labelText The By locator for the label text
     * @param getInput The By locator for the input element
     * @param text The text value to select
     */
    public void selectValueOnCheckBox(By chkBoxCollection, By labelText, By getInput, String text) {
        List<WebElement> chkBoxCollections = driver.findElements(chkBoxCollection);
        for (WebElement webElement : chkBoxCollections) {
            WebElement getLabel = webElement.findElement(labelText);
            WebElement getInput1 = webElement.findElement(getInput);
            if (getLabel.getText().equals(text)) {
                if (!getInput1.isSelected()) {
                    getLabel.click();
                    break;
                }
            }
        }
    }

    /**
     * Waits for an element to become visible.
     * @param locator The By locator for the element
     */
    public void waitForElementVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for an element to become clickable.
     * @param locator The By locator for the element
     */
    public void waitForElementClickable(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Randomly clicks an element from a list of elements.
     * @param value The By locator for the list of elements
     */
    public void randomClickFromList(By value) {
        List<WebElement> itemsInList = driver.findElements(value);
        int size = itemsInList.size();
        int randomNumber = ThreadLocalRandom.current().nextInt(0, size);
        itemsInList.get(randomNumber).click();
    }

    /**
     * Generates a random string.
     * @return A random string of 250 characters
     */
    public static String randomString() {
        String saltChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 250) {
            int index = (int) (rnd.nextFloat() * saltChars.length());
            salt.append(saltChars.charAt(index));
        }
        return salt.toString();
    }

}
