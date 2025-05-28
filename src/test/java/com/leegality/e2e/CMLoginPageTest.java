package com.leegality.e2e;

import com.leegality.atlas.ui.BaseTest;
import com.leegality.atlas.ui.pages.CMLoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.leegality.atlas.utils.CommonMethods.getEnvVariable;
import static io.qameta.allure.Allure.step;

@Epic("Consent Manager Login Page")
@Feature("Consent Manager Login Page")
public final class CMLoginPageTest extends BaseTest {

    public static final String CONSENT_MANAGER_URL = "https://dev.consent.in/dashboard";
    public static final String CONSENT_MANAGER_LANDING_URL = "https://dev.consent.in/landing";
    public static final String CM_E2E_USER = getEnvVariable("CM_E2E_USER");
    public static final String CM_E2E_PWD = getEnvVariable("CM_E2E_PWD");

    CMLoginPage cmLoginPage = new CMLoginPage(driver);

    @Test(description = "Consent Manager Login page")
    @Description("Consent Manager Login page")
    @Severity(SeverityLevel.CRITICAL)
    @Story("TECH-0004")
    public void tc04() {
        step("Open CM Page");
        cmLoginPage.openURL(CONSENT_MANAGER_URL);

        step("Login In using: " + CM_E2E_USER + "&" + CM_E2E_PWD);
        cmLoginPage.signIn(CM_E2E_USER, CM_E2E_PWD);

        step("Wait for avatar");
        cmLoginPage.waitForAvatar();

        step("Click on avatar and logout");
        cmLoginPage.logout();

        step("Check current URL");
        Assert.assertEquals(driver.getCurrentUrl(), CONSENT_MANAGER_LANDING_URL, "Current URL is not correct");
    }

}
