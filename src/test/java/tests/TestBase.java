package tests;

import utils.DriverHelper;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class TestBase {

    public WebDriver driver;
    protected String baseUrl = "http://localhost:7080";
    @BeforeMethod
    public void setup(){
        driver = DriverHelper.getDriver();
    }

    @AfterMethod
    public void tearDown(ITestResult iTestResult){
     driver.quit();
        }
    }
