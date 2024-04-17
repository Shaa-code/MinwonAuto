package org.auto.minwonauto;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;

import java.awt.*;

import static org.auto.minwonauto.MinwonService.*;
import static util.PathVariable.EDGE_NAME;
import static util.PathVariable.EDGE_PATH;

public class MinwonController{

    @FXML
    private PasswordField gonginAuthField;
    @FXML
    private Button startButton;
    @FXML
    private Button chromeButton;
    @FXML
    private Button researchButton;
    @FXML
    private TextArea processDisplay;

    private MinwonService minwonService = new MinwonService();

    public MinwonController() throws AWTException {
    }


    @FXML
    private void handleStartButtonAction(){
        String gonginPassword = gonginAuthField.getText();
        gonginAuthField.clear();

        fieldContent.append("프로그램을 시작합니다.\n");
        processDisplay.setText(fieldContent.toString());

        Thread t1 = new Thread(() -> {
            try {
                minwonService.minwonAutoProcess(gonginPassword ,processDisplay);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();
    }

    @FXML
    private void handleResearchButtonAction(){
        Thread t2 = new Thread(() -> {
            try {
                fieldContent.append("민원 찾기 시작..");
                minwonService.busyWaitUntilFindFirstMinwon(minwonApplyPageUrl, REFRESH_SECOND, processDisplay);
            } catch (Exception e) {
                fieldContent.append("민원 찾기 오류..");
                processDisplay.setText(fieldContent.toString());
            }
        });
        t2.start();
    }

    private void handleChromeButtonAction(){
        Thread t3 = new Thread(() -> {
            minwonService.chromeAutomation();
        });
        t3.start();
    }

    @FXML
    private void initialize() {
        //환경변수 설정
        System.setProperty(EDGE_NAME, EDGE_PATH);

        //startButton
        startButton.setText("로그인 및 탐색 시작");
        startButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill:#ffffff;");
        startButton.setOnAction(event -> {handleStartButtonAction();});

        //researchButton
        researchButton.setText("재탐색");
        researchButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill:#ffffff;");
        researchButton.setOnAction(event -> {handleResearchButtonAction();});

        //chromeButton
        chromeButton.setText("민원처리실행");
        chromeButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill:#ffffff;");
        chromeButton.setDisable(true);
        chromeButton.setOnAction(event -> {handleChromeButtonAction();});

        processDisplay.setText("프로그램 초기화...");

    }

    public TextArea getProcessDisplay() {
        return processDisplay;
    }
}
