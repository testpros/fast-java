import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class WebDriverGenericIT {

    @Test
    public void seleniumSampleTest() {
        WebDriverManager.chromedriver().forceCache().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://google.com");
        WebElement element = driver.findElement(By.name("q"));
        element.sendKeys("cheese");
        element.submit();
        element = driver.findElement(By.name("q"));
        assertEquals(element.getAttribute("value"), "cheese");
        driver.quit();
    }

    @Test
    public void appiumBrowserSampleTest() {
        AppiumDriverLocalService service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().usingAnyFreePort());
        service.start();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60);
        WebDriverManager.chromedriver().forceCache().version("74").setup();
        capabilities.setCapability(AndroidMobileCapabilityType.CHROMEDRIVER_EXECUTABLE,
                WebDriverManager.chromedriver().getBinaryPath());
        WebDriver driver = new AndroidDriver<>(service, capabilities);
        driver.get("https://google.com");
        WebElement element = driver.findElement(By.name("q"));
        element.sendKeys("cheese");
        element.sendKeys(Keys.ENTER);
        element = driver.findElement(By.name("q"));
        assertEquals(element.getAttribute("value"), "cheese");
        driver.quit();
        service.stop();
    }

    By userId = By.id("mobileNo");
    By password = By.id("et_password");
    By loginButton = By.id("btn_mlogin");
    By existingUserLogin = By.id("btn_mlogin");
    By errorMessage = By.id("pageLevelError");

    @Test
    public void appiumNativeSampleTest() {
        AppiumDriverLocalService service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().usingAnyFreePort());
        service.start();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appPackage", "com.flipkart.android");
        capabilities.setCapability("appActivity", "com.flipkart.android.SplashActivity");
        capabilities.setCapability(MobileCapabilityType.APP, "src/test/resources/flipkart.apk");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60);
        capabilities.setCapability("autoGrantPermissions", "true");
        WebDriver driver = new AndroidDriver<>(service, capabilities);

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(existingUserLogin));
        driver.findElement(existingUserLogin).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton));
        WebElement userIdElement = driver.findElement(userId);
        userIdElement.clear();
        userIdElement.sendKeys("someone@testvagrant.com");
        driver.findElement(password).sendKeys("testvagrant123");
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        assertTrue(driver.findElement(errorMessage).getText().equalsIgnoreCase("Account does not exist"));

        driver.quit();
        service.stop();
    }
}