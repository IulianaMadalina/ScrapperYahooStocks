package web.scraping.Project.SeleniumConfiguration;


import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.springframework.context.annotation.Bean;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class SeleniumConfig {

//
//    @PostConstruct
//    void postConstruct() {
//        System.setProperty("webdriver.chrome.driver", "C://MADA//MASTER//RFC//project//chrome-win32");
//    }
//  @PostConstruct
//    void postConstruct() {
//      System.setProperty("webdriver.chrome.driver", "C://MADA//MASTER//RFC//project//Project//Project//src//main//resources//chrome.exe");
//      WebDriver driver = new ChromeDriver();
//      driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
//      driver.manage().window().maximize();
//  }
    @Bean
    public ChromeDriver driver() {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver();
    }
}
