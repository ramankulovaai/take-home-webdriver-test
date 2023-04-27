package tests;

import org.testng.annotations.DataProvider;

public class DataProviderLogin {

    @DataProvider(name = "login")
    public Object[][] getData() {
        return new Object[][]{
                {"tomsmith", "SuperSecretPassword!", "You logged into a secure area!"},
                {"", "SuperSecretPassword!", "Your username is invalid!"},
                {"tomsmith", "","Your password is invalid!"},
                {"", "","Your username is invalid!"},
        };
    }
}
