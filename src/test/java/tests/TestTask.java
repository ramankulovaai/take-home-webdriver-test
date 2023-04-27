package tests;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.LoginPage;
import utils.BrowserUtils;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import static org.testng.Assert.assertTrue;


public class TestTask extends TestBase {

    @Test(dataProvider = "login", dataProviderClass = DataProviderLogin.class)
    public void loginTest(String username, String password, String message) {
        String path = "/login";
        driver.navigate().to(baseUrl + path);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
        assertTrue(loginPage.getSuccessMessage().getText().trim().contains(message));
    }

    @Test
    public void checkBoxes() {
        String path = "/checkboxes";
        driver.navigate().to(baseUrl + path);
        WebElement checkbox1 = driver.findElement(By.xpath("//input[@type = 'checkbox'][1]"));
        checkbox1.click();
        Assert.assertTrue(checkbox1.isSelected());
        WebElement checkbox2 = driver.findElement(By.xpath("//input[@type = 'checkbox'][2]"));
        checkbox2.click();
        Assert.assertFalse(checkbox2.isSelected());
    }

    @Test
    public void contextMenu() {
        String path = "/context_menu";
        driver.navigate().to(baseUrl + path);
        WebElement box = driver.findElement(By.xpath("//div[@id='hot-spot']"));
        Actions action = new Actions(driver);
        action.contextClick(box).perform();
        Alert alert = driver.switchTo().alert();
        Assert.assertEquals(alert.getText().trim(), "You selected a context menu");
        alert.accept();
    }

    @Test
    public void dragAndDrop(){
        String path = "/drag_and_drop";
        driver.navigate().to(baseUrl + path);
        WebElement box1 = driver.findElement(By.id("column-a"));
        WebElement box2 = driver.findElement(By.id("column-b"));
        Assert.assertEquals(box1.getText().trim(), "A");
        Assert.assertEquals(box2.getText().trim(), "B");
        Actions action = new Actions(driver);

        BrowserUtils.dragAndDropUsingJavaScript(driver, box1, box2);
        Assert.assertEquals(box1.getText().trim(), "B");
        Assert.assertEquals(box2.getText().trim(), "A");
    }

    @Test
    public void dropDown() {
        String path = "/dropdown";
        driver.navigate().to(baseUrl + path);

        WebElement dropDown = driver.findElement(By.xpath("//select[@id='dropdown']"));

        BrowserUtils.selectBy(dropDown, "Option 1", "text");

        Select select = new Select(dropDown);
        WebElement firstSelectedOption = select.getFirstSelectedOption();
        Assert.assertEquals(BrowserUtils.getText(firstSelectedOption), "Option 1");

        BrowserUtils.selectBy(dropDown, "Option 2", "text");
        WebElement secondSelectedOption = select.getFirstSelectedOption();
        Assert.assertEquals(BrowserUtils.getText(secondSelectedOption), "Option 2");
    }

    @Test
    public void dynamicContent() {
        String path = "/dynamic_content ";
        driver.navigate().to(baseUrl + path);
        List<WebElement> dynamicElements = driver.findElements(By.className("//div[@id='content']//div[contains(@class,'large')]"));
        List<String> contentBeforeRefresh = new ArrayList<>();

        for (int i = 1; i <= dynamicElements.size(); i++) {
            contentBeforeRefresh.add(BrowserUtils.getText(dynamicElements.get(i)));
        }

        int numberOfRefreshes = 2;

        for (int i = 0; i < numberOfRefreshes; i++) {
            driver.navigate().refresh();
            dynamicElements = driver.findElements(By.className("//div[@id='content']//div[contains(@class,'large')]"));

            for (int j = 0; j < dynamicElements.size(); j++) {
                WebElement element = dynamicElements.get(j);
                String contentAfterRefresh = element.getText();
                Assert.assertNotEquals("Dynamic content " + (j + 1) + " should change on refresh", contentBeforeRefresh.get(j), contentAfterRefresh);
                contentBeforeRefresh.set(j, contentAfterRefresh);
            }
        }
    }

    @Test
    public void dynamic_controls(){
        String path = "/dynamic_controls";
        driver.navigate().to(baseUrl + path);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45)); // 10 seconds timeout
        String commonPathForMessage = "//p[@id='message']";
        // Click on the Remove Button and assert that the checkbox is gone
        WebElement checkbox = driver.findElement(By.xpath("//div[@id='checkbox']"));
        WebElement removeButton = driver.findElement(By.xpath("//button[contains(text(), 'Remove')]"));
        removeButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(commonPathForMessage)));
        WebElement message = driver.findElement(By.xpath(commonPathForMessage));
        Assert.assertEquals(message.getText().trim(), "It's gone!");

        // Click on the Add Button and assert that the checkbox is present
        WebElement addButton = driver.findElement(By.xpath("//button[contains(text(), 'Add')]"));
        addButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(commonPathForMessage)));
        WebElement messageBack=driver.findElement(By.xpath(commonPathForMessage));
        Assert.assertEquals(BrowserUtils.getText(messageBack), "It's back!");

        driver.navigate().refresh();
        // Click on the Enable Button and assert that the textbox is enabled
        WebElement enableButton = driver.findElement(By.xpath("//button[contains(text(), 'Enable')]"));
        enableButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(commonPathForMessage)));
        WebElement messageEnabled= driver.findElement(By.xpath(commonPathForMessage));
        Assert.assertEquals(BrowserUtils.getText(messageEnabled), "It's enabled!");

        // Click on the Disable Button and assert that the textbox is disabled

        WebElement disableButton = driver.findElement(By.xpath("//button[contains(text(), 'Disable')]"));
        disableButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(commonPathForMessage)));
        WebElement messageDisabled = driver.findElement(By.xpath(commonPathForMessage));
        Assert.assertEquals(BrowserUtils.getText(messageDisabled), "It's disabled!");
    }

    @Test
    public void dynamicLoading() {
        String path = "/dynamic_loading/2";
        driver.navigate().to(baseUrl + path);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement startButton = driver.findElement(By.xpath("//button[contains(text(), 'Start')]"));
        startButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h4[contains(text(), 'Hello World!')]")));
        WebElement text = driver.findElement(By.xpath("//h4[contains(text(), 'Hello World!')]"));
        Assert.assertEquals(text.getText().trim(), "Hello World!");
    }

    String fileName = "/some-file.txt";
    String downloadPath = System.getProperty("user.home") + "/Downloads";

    @Test(priority = 1)
    public void fileDownload() {
        String path = "/download";
        driver.navigate().to(baseUrl + path);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement dnwFile = driver.findElement(By.xpath("//a[contains(text(), 'some-file.txt')]"));
        dnwFile.click();
        Path filePath = Paths.get(downloadPath, fileName);
        wait.until(driver -> Files.exists(filePath));
        assertTrue(Files.exists(filePath), "File is not downloaded");
    }

    @Test (priority = 2)
    public void fileUpload() {
        String path = "/upload";
        driver.navigate().to(baseUrl + path);

        WebElement uploadButton = driver.findElement(By.xpath("//input[@id = 'file-submit']"));
        Path filePath = Paths.get(downloadPath, fileName);
        WebElement fileInput = driver.findElement(By.xpath("//input[@id = 'file-upload']"));
        fileInput.sendKeys(filePath.toString());
        uploadButton.click();
        WebElement successMessage = driver.findElement(By.xpath("//h3[contains(text(), 'File Uploaded')]"));
        Assert.assertEquals(successMessage.getText().trim(), "File Uploaded!");
    }

    @Test
    public void floatingMenu() {
        String path = "/floating_menu";
        driver.navigate().to(baseUrl + path);
        WebElement lastElementOnAPage = driver.findElement(By.xpath("//a[contains(text(), 'Elemental Selenium')]"));
        BrowserUtils.scrollIntoView(driver, lastElementOnAPage);
        List<WebElement> menuElements = driver.findElements(By.tagName("li"));
        for (WebElement element : menuElements) {
            assertTrue(element.isDisplayed());
        }
    }

    @Test
    public void iFrame() {
        String path = "/iframe";
        driver.navigate().to(baseUrl + path);
        try {
            WebElement crossButton = driver.findElement(By.xpath("//button//div[@class = 'tox-icon']"));
            crossButton.click();
        } catch (NoSuchElementException e) {
            System.out.println("This element is not found");
        }
        driver.switchTo().frame("mce_0_ifr");
        WebElement textPlace = driver.findElement(By.xpath("//body[@id = 'tinymce']"));
        textPlace.clear();
        String text = "My text";
        textPlace.sendKeys(text);
        Assert.assertEquals(textPlace.getText().trim(), text);
    }

    @Test
    public void mouseHover() {
        String path = "/hovers";
        driver.navigate().to(baseUrl + path);
        Actions actions = new Actions(driver);
        List<WebElement> pictures = driver.findElements(By.xpath("//div[@class = 'figure']"));
        List<WebElement> texts = driver.findElements(By.tagName("h5"));
        for (int i = 0; i < pictures.size(); i++) {
            actions.moveToElement(pictures.get(i)).clickAndHold(pictures.get(i)).perform();
            assertTrue(texts.get(i).isDisplayed(), "Text is not Displayed");
        }
    }

    @Test
    public void jsAlerts() {
        String path = "/javascript_alerts";
        driver.navigate().to(baseUrl + path);

        WebElement jsClickButton = driver.findElement(By.xpath("//button[contains(text(), 'Click for JS Alert')]"));
        WebElement jsConfirmButton = driver.findElement(By.xpath("//button[contains(text(), 'Click for JS Confirm')]"));
        WebElement jsPromptButton = driver.findElement(By.xpath("//button[contains(text(), 'Click for JS Prompt')]"));
        WebElement resultText = driver.findElement(By.xpath("//p[@id = 'result']"));
        String message = "Hello";
        jsClickButton.click();
        Alert alert = driver.switchTo().alert();
        alert.accept();
        Assert.assertEquals(resultText.getText().trim(), "You successfuly clicked an alert");
        jsConfirmButton.click();
        alert = driver.switchTo().alert();
        alert.accept();
        Assert.assertEquals(resultText.getText().trim(), "You clicked: Ok");
        jsPromptButton.click();
        alert.sendKeys(message);
        alert.accept();
        assertTrue(resultText.getText().contains(message));
    }

    @Test
    public void jsError() {
        String path = "/javascript_error";
        driver.navigate().to(baseUrl + path);
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        String text = "Cannot read properties of undefined (reading 'xyz')";
        for (LogEntry entry : logEntries) {
            if (entry.getLevel() == Level.SEVERE) {
                assertTrue(entry.getMessage().contains(text));
            }
        }
    }

    @Test
    public void openInNewTab() {
        String path = "/windows";
        driver.navigate().to(baseUrl + path);
        WebElement clickHereButton = driver.findElement(By.xpath("//a[contains(text(), 'Click Here')]"));
        clickHereButton.click();
        BrowserUtils.switchById(driver);
        Assert.assertEquals(driver.getTitle().trim(), "New Window");

    }

    @Test
    public void notificationMessage() throws InterruptedException {
        String path = "/notification_message_rendered";
        driver.navigate().to(baseUrl + path);
        WebElement clickHereButton = driver.findElement(By.xpath("//a[contains(text(), 'Click here')]"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        int maxClicks = 10;
        int numClicks = 0;
        while (numClicks<maxClicks){
            clickHereButton.click();
            clickHereButton = driver.findElement(By.xpath("//a[contains(text(), 'Click here')]"));
            WebElement messageElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("flash-messages")));
            String messageText = messageElement.getText();
            if (messageText.contains("Action Successful")) {

                Assert.assertTrue(messageElement.isDisplayed());
            } else if (messageText.contains("Action unsuccesful, please try again")) {

                Assert.assertTrue(messageElement.isDisplayed());
            }
            numClicks++;
        }
    }
}






















