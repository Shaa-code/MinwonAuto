package org.auto.minwonauto;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

import java.time.Duration;


public class MinwonMainController {

    @FXML
    private PasswordField gonginAuthField;

    @FXML
    private Button startButton;

    @FXML
    private void initialize(){
        startButton.setText("시작");
        startButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill:#ffffff;");

        //
        startButton.setOnAction(event -> {
            gonginAuthField.setText("");
            WebDriver edgeDriver = new EdgeDriver();
            minwonLogin(edgeDriver);
//            edgeDriver.get("https://intra.gov.kr/my/AA040ListingOffer.do?returnApp=AA040ListingOfferApp&CP=0&PROC_STATS=0102&FROMDATE=20240328&TODATE=20240403&DP=open&ORGAN_GUBUN=1&HIGH_MENU_ID=1000062&MENU_ID=1000689&MENU_INDEX=1");

        });
    }

    private void minwonLogin(WebDriver edgeDriver) {
        final String minwonUrl = "https://intra.gov.kr/g4g_admin/AA060_new_login.jsp";

        //민원처리창구 로그인
        edgeDriver.get(minwonUrl);
        WebElement authButton = edgeDriver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/ul[1]/li[1]/button"));
        authButton.click();
        edgeDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(3000));

        //인증서 처리
        WebElement adminCertificateCell = edgeDriver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[3]/table/tbody/tr[2]/td[2]/div"));
        adminCertificateCell.click();
        adminCertificateCell.sendKeys(gonginAuthField.getText());
    }
}
