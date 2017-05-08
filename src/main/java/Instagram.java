
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by Ali Ulvi TALIPOGLU on 04.05.2017.
 */
public class Instagram {
    // System.setProperty("webdriver.chrome.driver", "C:\\geckodriver-v0.11.1-win64\\chromedriver.exe");
    public static String XpathExpression(String value)//does not work.
    {
        if (!value.contains("'"))
            return '\'' + value + '\'';

        else if (!value.contains("\""))
            return '"' + value + '"';

        else
            return "concat('" + value.replace("'", "',\"'\",'") + "')";
    }
    WebDriver driver;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String comment,pw,user="GoodiesQuality";
    public boolean chrome = false;
    public boolean success;
    public boolean login = true;


    // driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444"), DesiredCapabilities.firefox());
    public Instagram() {

    }

    public Instagram(String url, String comment) {
        this.url = url;
        this.comment = comment;
    }


    @Test
    public void test_Login() throws Exception

    {
        success = false;


        if (login) {
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");


            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            options.setExperimentalOption("prefs", prefs);
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            driver = new ChromeDriver(options);

            driver.navigate().to("https://www.instagram.com/");
            try {
                try {
                    fluentWait(By.cssSelector("._fcn8k")).click();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                fluentWait(By.xpath("//input[@name=\"username\"]")).sendKeys(user);
                fluentWait(By.xpath("//input[@name=\"password\"]")).sendKeys(pw);
                fluentWait(By.tagName("button")).click();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //  spam kapama
                fluentWait(By.xpath("//button[@class=\"_ibk5z\"]")).click();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        WebElement commentField;
        driver.navigate().to(url);
        //Thread.sleep(2000); click pic
        fluentWait(By.xpath("//div[@class=\"_ovg3g\"]")).click();
        commentField = fluentWait(By.cssSelector("input._2hc0g._qy55y"));
        //comment writing
        commentField.sendKeys(comment);
        commentField.sendKeys(Keys.ENTER);
        if (!comment.contains("\"")) {
            fluentWait2(By.xpath("//a[text()=\"" + user.toLowerCase() + "\"]/following-sibling::span/span[text()=\"" + (comment) + "\"]"));
            System.out.print("@");
        }
        else  if (!comment.contains("'")) {
            fluentWait2(By.xpath("//a[text()=\"" + user.toLowerCase() + "\"]/following-sibling::span/span[text()='" + (comment) + "']"));

        }
        else {
            Thread.sleep(2500);
        }

        //driver.close();  http://stackoverflow.com/questions/642125/encoding-xpath-expressions-with-both-single-and-double-quotes
        //  driver.quit();
        success = true;
    }

    //waiting function. may need java 1.8+.
    public WebElement fluentWait(final By locator) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(35, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        });

        return foo;
    }
    public WebElement fluentWait2(final By locator) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(8, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        });

        return foo;
    }

}
