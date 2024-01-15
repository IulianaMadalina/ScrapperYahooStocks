package web.scraping.Project.Service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import web.scraping.Project.Model.TableRow;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class ScraperService {

    private static final String URL = "https://finance.yahoo.com/most-active/";
    private static final int ITEMS_PER_PAGE = 25;

    private final ChromeDriver driver = new ChromeDriver();

    @PostConstruct
    void postConstruct() {
        System.out.println("START");
        scrape();
    }

    public void scrape() {
        Boolean headersDisplayed = false;

        driver.get(URL);
        System.out.println(URL);
        Duration timeout = Duration.ofSeconds(10);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement acceptButton = driver.findElement(By.name("agree"));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", acceptButton);


        acceptButton.click();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        int offset = 0;
        List<String> headerList = null;
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter("./output3.csv"), CSVFormat.DEFAULT.withHeader(headerList != null ? headerList.toArray(new String[0]) : null))) {
            while (true) {

                WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("table")));

                if (!headersDisplayed) {
//                    // Display headers only if they have not been displayed yet
//                    headerList = displayTableHeaders(table);
//                    headersDisplayed = true; // Set the flag to true after displaying headers
                    headerList = displayTableHeaders(table);
                    System.out.println(String.join("\t", headerList));
                    csvPrinter.printRecord(headerList);
                    headersDisplayed = true;
                }

                List<WebElement> rows = table.findElements(By.tagName("tr"));

                // Iterate through each row
                for (int j = 1; j < rows.size(); j++) {
                    WebElement row = rows.get(j);
//                    System.out.println(row.getText());
//                    List<String> rowDataList = Arrays.asList(row.getText().split("\\s+"));

                    List<String> rowDataList = extractRowData(row);
                    for (String value : rowDataList) {
                        System.out.print(value +"\t" );
                    }
                    System.out.println();

                    csvPrinter.printRecord(rowDataList);
                    // Find all columns in the row

                    // Create a TableRow object to hold the data
//                TableRow rowData = createTableRow(row);
//
//                // Display or process the data as needed
//                System.out.println(rowData);

                    // You can further process or store the data, e.g., add it to a list, write to a file, etc.
                    if (row.getText().length() == 0) {
                        break; // No more tables found, exit the loop
                    }
                }


                offset += ITEMS_PER_PAGE;

                // Navigate to the next page
                driver.get(URL + "?count=" + ITEMS_PER_PAGE + "&offset=" + offset);


                // Check if there are more tables on the next page
                if (rows.size() == 1) {
                    System.out.println("Header dectat");
                    break; // No more tables found, exit the loop
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver.quit();
    }
    private List<String> extractRowData(WebElement row) {
        List<String> rowDataList = new ArrayList<>();
        String[] rowDataArray = row.getText().split("\\s+");

        // Combine the company name
        int startIndex = 0;
        int endIndex = rowDataArray.length;

        if (rowDataArray[0].matches("[A-Za-z]+,[A-Za-z]+")) {
            // The first element appears to be the company name
            startIndex = 1;
            rowDataList.add(rowDataArray[0]); // Add the combined company name to the list
        }

        // Add the remaining elements
        for (int i = startIndex; i < endIndex; i++) {
            rowDataList.add(rowDataArray[i]);
        }

        return rowDataList;
    }



    private List<String> displayTableHeaders(WebElement table) {
        List<String> headerList = new ArrayList<>();
        List<WebElement> tableHead = table.findElements(By.tagName("thead"));

        for (int j = 0; j < tableHead.size(); j++) {
            WebElement col = tableHead.get(j);
            List<WebElement> col1 = col.findElements(By.tagName("th"));
            for (int k = 0; k < col1.size(); k++) {
                WebElement col2 = col1.get(k);
                String columnName = col2.getText().replace("52 Week Range", "").trim();
                headerList.add(columnName);
                // Display the content
                System.out.print(columnName + "\t");
            }
            System.out.println();
        }
        return headerList;
    }
    private int lengthTableHeader(WebElement table){
        List<WebElement> tableHead = table.findElements(By.tagName("thead"));
        System.out.println("lungime th " + tableHead.size());
        return tableHead.size();
    }

    private TableRow createTableRow(List<String> headerList, List<String> rowData) {
        TableRow tableRow = new TableRow();

        for (int i = 0; i < headerList.size(); i++) {
            String columnName = headerList.get(i);
            String columnValue = rowData.get(i);

            switch (columnName) {
                case "Symbol":
                    tableRow.setSymbol(columnValue);
                    break;
                case "Name":
                    tableRow.setName(columnValue);
                    break;
                case "Price (Intraday)":
                    tableRow.setPrice(Double.parseDouble(columnValue));
                    break;
                case "Change":
                    tableRow.setChange(Double.parseDouble(columnValue));
                    break;
                case "% Change":
                    tableRow.setPercentChange(columnValue);
                    break;
                case "Volume":
                    tableRow.setVolume(columnValue);
                    break;
                case "Avg Vol (3 month)":
                    tableRow.setAvgVol3Month(columnValue);
                    break;
                case "Market Cap":
                    tableRow.setMarketCap(columnValue);
                    break;
                case "PE Ratio (TTM)":
                    tableRow.setPeRatioTTM(columnValue);
                    break;

            }
        }
        return tableRow;
    }
}
