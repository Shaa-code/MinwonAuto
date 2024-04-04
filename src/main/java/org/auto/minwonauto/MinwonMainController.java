package org.auto.minwonauto;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MinwonMainController {

    public static final String EDGE_NAME = "webdriver.edge.driver";
    public static final String EDGE_PATH = "C:\\Users\\user\\Desktop\\Test\\msedgedriver.exe";


    @FXML
    private PasswordField gonginAuthField;

    private String inputPassword;

    @FXML
    private Button startButton;

    @FXML
    private void initialize(){
        System.setProperty(EDGE_NAME,EDGE_PATH);

        startButton.setText("시작");
        startButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill:#ffffff;");


        startButton.setOnAction(event -> {
            inputPassword = gonginAuthField.getText();
            gonginAuthField.setText("");

            //Driver가 실행되는 동안 프로그램이 무한로딩이라 Thread 사용함.
            Thread t = new Thread(() -> {
                WebDriver edgeDriver = new EdgeDriver();
                minwonLogin(edgeDriver);
                minwonAutoProcess(edgeDriver);
            });
            t.start();
        });
    }

    private void minwonAutoProcess(WebDriver edgeDriver) {
        final String minwonApplyPageUrl = "https://intra.gov.kr/my/AA040ListingOffer.do?returnApp=AA040ListingOfferApp&CP=0&PROC_STATS=0102&FROMDATE=20240329&TODATE=20240404&DP=open&ORGAN_GUBUN=1&HIGH_MENU_ID=1000062&MENU_ID=1000689&MENU_INDEX=1";

        Wait<WebDriver> wait = new WebDriverWait(edgeDriver, Duration.ofSeconds(30));
        wait.until(driver -> ((JavascriptExecutor) edgeDriver).executeScript("return document.readyState")).equals("complete");

        try {
//        edgeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // 작동 안 함
            Thread.sleep(5000); // 자체 sleep을 걸었음.
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        edgeDriver.get(minwonApplyPageUrl);

        wait.until(driver -> Duration.ofSeconds(10));
        WebElement table = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[2]/form[2]/div[1]/table"));
        List<WebElement> tbody = table.findElements(By.tagName("tbody"));
        //*[@id="contents"]/form[2]/div[1]/table/tbody[1]/tr/td[2]/a[1]
        //위의 코드에서 table/tbody를 뽑은 것이다.
//            tableRows.get(0).click();

    }

    private void minwonLogin(WebDriver edgeDriver) {
        final String minwonMainUrl = "https://intra.gov.kr/g4g_admin/AA060_new_login.jsp";

        edgeDriver.get(minwonMainUrl);

        Wait<WebDriver> wait = new WebDriverWait(edgeDriver, Duration.ofSeconds(10));

        WebElement authButton = edgeDriver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/ul[1]/li[1]/button"));

        edgeDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));

        wait.until(edge -> authButton.isDisplayed());
        authButton.click();

        wait = new WebDriverWait(edgeDriver, Duration.ofSeconds(10));
        WebElement selectAdmin = edgeDriver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[3]/table/tbody/tr[2]/td[2]/div"));
        wait.until(edge -> selectAdmin.isDisplayed());
        selectAdmin.click();

        WebElement certificatePassword = edgeDriver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[5]/table/tbody/tr/td[2]/form/div[1]/input[1]"));
        certificatePassword.sendKeys(inputPassword);

        WebElement certificateConfirm = edgeDriver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[6]/button[1]"));
        certificateConfirm.click();


    }
}
