package ru.netology.rest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {
    private WebDriver driver;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void SendForm() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Артем Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+73495874356");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    //Невалидные данные в поле "Фамилия и имя"
    @Test
    void shouldReturnErrorNameField() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ivan");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+73495874356");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    //Не заполнено поле "Фамилия и имя"
    @Test
    void shouldReturnErrorNameFieldNotFilled() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+73495874356");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    //Невалидные данные в поле "Мобильный телефон"
    @Test
    void shouldReturnErrorPhoneField() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Павел Сергеев");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7349587435");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    //Не заполнено поле "Мобильный телефо"
    @Test
    void shouldReturnErrorPhoneFieldNotFilled() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Павел Сергеев");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    //Не заполнен чек-бокс
    @Test
    void shouldNotRegistrationWithoutCheckBox() {
        List<WebElement> input = driver.findElements(By.cssSelector("input"));
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Сергей Трофимов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+77630589265");
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__text")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text.trim());
    }
}

