package org.auto.minwonauto;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.auto.minwonauto.MinwonService.*;
import static util.EnvironmentVariable.EDGE_NAME;
import static util.EnvironmentVariable.EDGE_PATH;

public class MinwonController implements ProcessDisplayUpdater{

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

    private ExecutorService executorService;
    private final StringBuilder fieldContent;
    private MinwonService minwonService;
    public MinwonController() throws AWTException {
        this.fieldContent = new StringBuilder();
        this.minwonService = new MinwonService(this);
        this.executorService = Executors.newFixedThreadPool(10);
    }


    @FXML
    private void handleStartButtonAction(){
        String gonginPassword = gonginAuthField.getText();
        gonginAuthField.clear();
        fieldContent.setLength(0);
        fieldContent.append("프로그램을 시작합니다.\n");
        processDisplay.setText(fieldContent.toString());

        startButton.setDisable(true);
        researchButton.setDisable(false);
        executorService.submit(() -> {
            try {
                minwonService.minwonAutoProcess(gonginPassword);
            } catch (Throwable e) {
                Platform.runLater(() -> startButton.setDisable(false));
                throw new RuntimeException(e);
            }
        });


        executorService.submit(() -> {
            try {
                minwonService.minwonAnywhereAutoProcess(gonginPassword);
            } catch (Throwable e) {
                startButton.setDisable(false);
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    private void handleResearchButtonAction() {
        researchButton.setDisable(true);

        executorService.submit(() -> {
            try {
                whenMinwonFound = false;
                fieldContent.append("일반 민원 찾기 시작..").append("\n");
                processDisplay.setStyle("-fx-background-color: white");
                minwonService.busyWaitUntilFindFirstMinwon(minwonApplyPageUrl, REFRESH_SECOND);
            } catch (Exception e) {
                fieldContent.append("일반 민원 찾기 오류..").append("\n");
                processDisplay.setStyle("-fx-background-color: red");
                startButton.setDisable(false);
                processDisplay.setText(fieldContent.toString());
            }
        });

        executorService.submit(() -> {
            try {
                whenMinwonFound = false;
                fieldContent.append("어디서나 민원 찾기 시작..").append("\n");
                processDisplay.setStyle("-fx-background-color: white");
                minwonService.busyWaitUntilFindFirstAnywhereMinwon(minwonAnywhereApplyPageUrl, REFRESH_SECOND);
            } catch (Exception e) {
                fieldContent.append("어디서나 민원 찾기 오류..").append("\n");
                processDisplay.setStyle("-fx-background-color: red");
                startButton.setDisable(false);
                processDisplay.setText(fieldContent.toString());
            }
        });
    }

    private void handleChromeButtonAction(){
        Thread t1 = new Thread(() -> {
//            minwonService.chromeAutomation();
        });
        t1.start();
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
        researchButton.setDisable(true);
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

    @Override
    public void updateProcessDisplay(String message){
        Platform.runLater(() -> {
            try {
                fieldContent.append(message).append("\n");
                processDisplay.setScrollTop(Double.MAX_VALUE);
                processDisplay.setText(fieldContent.toString());
                processDisplay.appendText("");
            } catch(Exception e){
                e.printStackTrace();
            }
        });
    }
    @Override
    public void updateStyle(String code){
        Platform.runLater(() -> {
            try {
                processDisplay.setStyle(code);
            } catch(Exception e){
                e.printStackTrace();
            }
        });
    }

    @Override
    public void setResearchButtonDisabled(boolean status){
        Platform.runLater(() -> {
            try{
                researchButton.setDisable(status);
            }catch(Exception e){
                e.printStackTrace();
            }
        });
    }
}
