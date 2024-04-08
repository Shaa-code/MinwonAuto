package org.auto.minwonauto;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import util.Mouse;
import utility.ApplyCategory;

import java.awt.*;
import java.time.Duration;

public class MinwonMainController {



    public static final String EDGE_NAME = "webdriver.edge.driver";
    public static final String EDGE_PATH = "C:\\Users\\user\\Desktop\\Test\\msedgedriver.exe";

    public static Mouse mouse = new Mouse();

    @FXML
    private PasswordField gonginAuthField;
    private String gonginPassword;
    @FXML
    private Button startButton;
    @FXML
    private TextField minwonAuthField;
    @FXML
    private PasswordField minwonPasswordField;


    @FXML
    private Button chromeButton;
    private String recipientName;
    private String residentRegistrationNumber;
    private String defaultAddress;
    private String detailAddress;
    private ApplyCategory applyCategory;


    @FXML
    private void initialize(){
        System.setProperty(EDGE_NAME,EDGE_PATH);

        //startButton 초기화
        startButton.setText("시작");
        startButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill:#ffffff;");

        startButton.setOnAction(event -> {
            gonginPassword = gonginAuthField.getText();
            gonginAuthField.setText("");


//            CountDownLatch latch = new CountDownLatch(1);
//            //Driver가 실행되는 동안 프로그램이 무한로딩이라 Thread 사용함.
//            Thread t1 = new Thread(() -> {
//                WebDriver edgeDriver = new EdgeDriver();
//                minwonLogin(edgeDriver);
//                minwonAutoProcess(edgeDriver);
//
//                try{
//                    latch.await();
//                } catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//
        });

        //chromeButton 초기화
        chromeButton.setText("로그인 및 셋팅이 완료되면 눌러주세요.");
        chromeButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill:#ffffff;");

        chromeButton.setOnAction(event -> {

        });

    }

    private void minwonAutoProcess(WebDriver edgeDriver) {
        final String minwonApplyPageUrl = "https://intra.gov.kr/my/AA040ListingOffer.do?returnApp=AA040ListingOfferApp&CP=0&PROC_STATS=0102&FROMDATE=20240329&TODATE=20240404&DP=open&ORGAN_GUBUN=1&HIGH_MENU_ID=1000062&MENU_ID=1000689&MENU_INDEX=1";

        Wait<WebDriver> wait = new WebDriverWait(edgeDriver, Duration.ofSeconds(30));
        wait.until(driver -> ((JavascriptExecutor) edgeDriver).executeScript("return document.readyState")).equals("complete");

        try {
//        edgeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // 작동 안 함
            Thread.sleep(2000); // 자체 sleep을 걸었음.
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        edgeDriver.get(minwonApplyPageUrl);

        WebElement minwonSearchButton = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[1]/div[3]/div[1]/div[1]/a"));
        minwonSearchButton.click();

        WebElement receptionButton = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[1]/div[3]/div[1]/div[2]/ul/li[2]/span[1]"));
        receptionButton.click();

        wait.until(driver -> Duration.ofSeconds(10));
        WebElement firstMinwonButton = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[2]/form[2]/div[1]/table/tbody/tr/td[2]/a"));
        firstMinwonButton.click();

        //주민등록번호
        WebElement residentRegistrationField = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[2]/div/form/dl/dd/dl/dd[1]/dl/dd[4]"));
        String residentRegistrationFieldText = residentRegistrationField.getText();
        residentRegistrationNumber = residentRegistrationFieldText.replace("-", "");

        //성명
        WebElement recipientNameField = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[2]/div/form/dl/dd/dl/dd[1]/dl/dd[2]"));
        recipientName = recipientNameField.getText();

        //기본주소
        WebElement defaultAddressField = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[2]/div/form/dl/dd/dl/dd[2]/dl/dd[2]"));
        defaultAddress = recipientNameField.getText();

        //상세주소
        WebElement detailAddressField = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[2]/div/form/dl/dd/dl/dd[3]/dl/dd[2]"));
        detailAddress = detailAddressField.getText();


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
        certificatePassword.sendKeys(gonginPassword);

        WebElement certificateConfirm = edgeDriver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[6]/button[1]"));
        certificateConfirm.click();


    }
}
