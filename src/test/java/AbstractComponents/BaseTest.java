package AbstractComponents;

import DemoQAPages.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class BaseTest {
    public static WebDriver driver;
    protected PractiseFormPage practiseFormPage;

    public WebDriver initializeDriver() throws IOException {
        // Load properties
        Properties prop = new Properties();
        String filePath = System.getProperty("user.dir") + "/src/main/resources/GlobalData.properties";
        FileInputStream fis = new FileInputStream(filePath);
        prop.load(fis);

        String browserName = prop.getProperty("browser");

        // Initialize WebDriver based on browser
        if (browserName.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("start-maximized");
            driver = new ChromeDriver(chromeOptions);

        } else if (browserName.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("start-maximized");
            driver = new FirefoxDriver(firefoxOptions);

        } else if (browserName.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("start-maximized");
            driver = new EdgeDriver(edgeOptions);

        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
        // Apply global browser settings
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        fis.close();
        return driver;
    }

    public <T extends BasePage> T getPage(Class<T> pageClass) {
        try {
            return pageClass.getDeclaredConstructor(WebDriver.class).newInstance(driver);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create page object: " + pageClass.getName(), e);
        }
    }

    @BeforeTest
    public void launchApplication() throws IOException {
        if (driver == null) {
            driver = initializeDriver();
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }
}
