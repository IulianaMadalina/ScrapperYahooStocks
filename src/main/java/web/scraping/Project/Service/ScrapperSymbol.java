package web.scraping.Project.Service;

import com.opencsv.CSVReader;
        import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileReader;
        import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
        import java.util.Random;
import java.util.function.Function;

public class ScrapperSymbol {
    private static final String BASE_URL = "https://finance.yahoo.com/quote/";
    private static final ChromeDriver driver = new ChromeDriver();

    public static void scrapeForSymbol(String symbol) {
        ArrayList<String> links = new ArrayList<>();
        String urlForSymbol = buildUrlForSymbol(symbol);
        driver.get(urlForSymbol);
        System.out.println(urlForSymbol);
        Duration timeout = Duration.ofSeconds(10);


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement acceptButton = driver.findElement(By.name("agree"));

// Scroll the button into view
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", acceptButton);

// Click the button
        acceptButton.click();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement section = driver.findElement(By.tagName("section"));

        List<WebElement> liElements = section.findElements(By.xpath(".//div/ul/li"));

        // Parcurgerea elementelor <li> pentru a verifica condițiile specificate
        for (WebElement li : liElements) {
            WebElement spanElement = li.findElement(By.tagName("span"));
            String spanText = spanElement.getText();

            // Verificare dacă <span> are textul "Summary", "Statistics" sau "Profile"
            if (spanText.equals("Summary") || spanText.equals("Statistics") || spanText.equals("Profile")) {
                // Procesare sau afișare a informațiilor necesare
                System.out.println("Text: " + spanText + ", Link: " + li.findElement(By.tagName("a")).getAttribute("href"));
                links.add( li.findElement(By.tagName("a")).getAttribute("href"));
//                driver.get(link);

//                WebElement tabel = driver.findElement(By.tagName("table"));
//                List<WebElement> rows = tabel.findElements(By.tagName("tr"));
//                for (int j = 0; j < 4; j++) {
//                    WebElement row = rows.get(j);
//                    System.out.println(row.getText());
//                }

            }

        }
        for (String link : links) {
            driver.get(link);
            System.out.println("Vizitat " + link);


            WebElement table = driver.findElement(By.tagName("table"));
            System.out.println("Tabel text: " + table.getText());
//            try {
//                Thread.sleep(15000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        driver.get(links.get(links.size()-1));
        WebElement divElement = driver.findElement(By.cssSelector("div[class='Mb(25px)']"));

        WebElement secondPElement = divElement.findElements(By.tagName("p")).get(1);

        List<WebElement> spanElements = secondPElement.findElements(By.cssSelector("span"));
        for (WebElement spanElement : spanElements) {
            System.out.println("Text: " + spanElement.getText());
        }//        String industryInfo = secondPElement.findElement(By.xpath(".//span[contains(text(), 'Industry')]/following-sibling::span")).getText();
//        String fullTimeEmployeesInfo = secondPElement.findElement(By.xpath(".//span[contains(text(), 'Full Time Employees')]/following-sibling::span")).getText();

    }
    public static String buildUrlForSymbol(String symbol) {
        String finalUrl = BASE_URL + symbol + "?p=" + symbol + "&.tsrc=fin-srch";
        return finalUrl;
    }

    public static String extractRandomSymbol(String csvFilePath) {
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            List<String[]> records = reader.readAll();

            // Skip the header row
            records.remove(0);

            // Generate a random index to select a row
            Random random = new Random();
            int randomIndex = random.nextInt(records.size());

            // Extract the symbol from the randomly selected row
            String[] randomRow = records.get(randomIndex);
            String symbol = randomRow[0];

            return symbol;
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String csvFilePath = "./output.csv";
        String randomSymbol = extractRandomSymbol(csvFilePath);

        if (randomSymbol != null) {
            System.out.println("Randomly extracted symbol: " + randomSymbol);
        } else {
            System.out.println("Error reading the CSV file or extracting a symbol.");
        }
        scrapeForSymbol(randomSymbol);
    }
}
