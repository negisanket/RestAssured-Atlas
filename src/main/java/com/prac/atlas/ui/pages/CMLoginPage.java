package com.prac.atlas.ui.pages;

import com.prac.atlas.ui.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object class for the Consent Manager Login page.
 * Provides methods to interact with login page elements and perform login-related actions.
 */
public final class CMLoginPage extends BasePage {

    private final By emailInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By signInButton = By.id("sign-in-button");
    private final By avatar = By.className("avatar-image");
    private final By logoutButton = By.xpath("//*[@id=\":r0:\"]/div/div[3]");

    /**
     * Constructor for CMLoginPage.
     * @param driver WebDriver instance to be used for browser automation
     */
    public CMLoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Signs in to the Consent Manager using provided credentials.
     * @param cmE2eUser Email/username for login
     * @param cmE2ePwd Password for login
     */
    public void signIn(String cmE2eUser, String cmE2ePwd) {
        enterText(emailInput, cmE2eUser);
        enterText(passwordInput, cmE2ePwd);
        clickOnButton(signInButton);
    }

    /**
     * Waits for the avatar element to become visible after login.
     */
    public void waitForAvatar() {
        waitForElementVisible(avatar);
    }

    /**
     * Performs logout by clicking on the avatar and logout button.
     */
    public void logout() {
        clickOnButton(avatar);
        clickOnButton(logoutButton);
    }

}
