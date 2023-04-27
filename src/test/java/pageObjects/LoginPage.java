package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.PageFactoryFinder;
import org.testng.Assert;
import utils.BrowserUtils;

public class LoginPage {
    public LoginPage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[@id = 'username']")
    WebElement username;

    @FindBy(xpath = "//input[@id = 'password']")
    WebElement password;

    public WebElement getSuccessMessage() {
        return successMessage;
    }

    @FindBy(xpath = "//div[@id = 'flash']")
    WebElement successMessage;
    public void login(String userName, String passWord){
     username.sendKeys(userName);
     password.sendKeys(passWord, Keys.ENTER);
    }
}
