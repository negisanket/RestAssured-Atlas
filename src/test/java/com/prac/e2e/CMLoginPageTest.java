package com.prac.e2e;

import com.prac.atlas.ui.BaseTest;
import com.prac.atlas.ui.pages.CMLoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.prac.atlas.utils.CommonMethods.getEnvVariable;
import static io.qameta.allure.Allure.step;

@Epic("Consent Manager Login Page")
@Feature("Consent Manager Login Page")
public final class CMLoginPageTest extends BaseTest {

    public static final String CONSENT_MANAGER_URL = "https://dev.consent.in/dashboard";

    CMLoginPage cmLoginPage = new CMLoginPage(driver);

    @Test(description = "Consent Manager Login page")
    @Description("Consent Manager Login page")
    @Severity(SeverityLevel.CRITICAL)
    @Story("TECH-0004")
    public void tc04() {
        step("Open CM Page");
        cmLoginPage.openURL(CONSENT_MANAGER_URL);

        String cmE2EUser = getEnvVariable("CM_E2E_USER");
        String cmE2EPwd = getEnvVariable("CM_E2E_PWD");

        step("Login In");
        cmLoginPage.signIn(cmE2EUser, cmE2EPwd);

        step("Wait for avatar");
        cmLoginPage.waitForAvatar();

        step("Click on avatar and logout");
        cmLoginPage.logout();

        step("Check current URL");
        Assert.assertTrue(driver.getCurrentUrl().contains("https://dev.consent.in/"), "Current URL is not correct");
    }

}
