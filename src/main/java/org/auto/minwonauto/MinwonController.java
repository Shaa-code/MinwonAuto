package org.auto.minwonauto;

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

    private String gonginPassword;

    private Thread t1;

    private Thread t2;

    private MinwonService minwonService = new MinwonService();

    public MinwonController() throws AWTException {
    }


    //    private static PointerInfo pt = MouseInfo.getPointerInfo();


    @FXML
    private void initialize() {
        //환경변수 설정
        System.setProperty(EDGE_NAME, EDGE_PATH);

        //startButton 초기화
        startButton.setText("로그인 및 탐색 시작");
        startButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill:#ffffff;");

        startButton.setOnAction(event -> {
            t1 = new Thread(() -> {
                gonginPassword = gonginAuthField.getText();
                gonginAuthField.setText("");

                fieldContent.append("프로그램을 시작합니다.\n");
                processDisplay.setText(fieldContent.toString());

                minwonService.minwonAutoProcess(gonginPassword);

            });
            t1.start();
        });

        researchButton.setText("재탐색");
        researchButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill:#ffffff;");
        researchButton.setOnAction(event -> {
            t2 = new Thread(() -> {
                minwonService.busyWaitUntilFindFirstMinwon(minwonApplyPageUrl,10);

            });
            t2.start();
        });


        chromeButton.setText("민원처리실행");
        chromeButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill:#ffffff;");
        chromeButton.setDisable(true);
        //chromeButton 초기화
        chromeButton.setOnAction(event -> {
//            t3 = new Thread(() -> {
//                minwonService.chromeAutomation();
//            });
//            t3.start();
        });

        processDisplay.setText("프로그램 초기화...");

    }

    public TextArea getProcessDisplay() {
        return processDisplay;
    }
}
