package App;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class App {
    public static void main(String [] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver","/usr/bin/chromedriver");
        WebDriver driver = new ChromeDriver();

        driver.get("http://localhost:4500");
        driver.manage().window().maximize();
        driver.findElement(By.id("select")).sendKeys("Svet fizike");
        driver.findElement(By.id("button")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("selectPayment")).sendKeys(("Banka"));
        driver.findElement(By.id("submitPayment")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("pan")).sendKeys("4381311001277948");
        driver.findElement(By.id("holderName")).sendKeys("Darko Jandric");
        driver.findElement(By.id("securityCode")).sendKeys("639");
        driver.findElement(By.id("validTo")).sendKeys("02/01/2020");
        driver.findElement(By.id("payButton")).click();
    }
}
