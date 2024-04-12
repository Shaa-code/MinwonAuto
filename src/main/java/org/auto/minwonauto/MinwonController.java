package org.auto.minwonauto;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;

import java.awt.*;

import static util.PathVariable.EDGE_NAME;
import static util.PathVariable.EDGE_PATH;

public class MinwonController {

    @FXML
    private PasswordField gonginAuthField;
    @FXML
    private Button startButton;
    @FXML
    private Button chromeButton;

    @FXML
    public static TextArea processDisplay;
    public static StringBuilder fieldContent = new StringBuilder("");
    public static String gonginPassword;

    private static PointerInfo pt = MouseInfo.getPointerInfo();

    @FXML
    private void initialize() {

        //환경변수 설정
        System.setProperty(EDGE_NAME, EDGE_PATH);

//        //마우스 좌표
//        AtomicReference<PointerInfo> pt = new AtomicReference<>(MouseInfo.getPointerInfo());
//        Thread t1 = new Thread(() -> {
//            while (true) {
//                pt.set(MouseInfo.getPointerInfo());
//                System.out.println(pt.get().getLocation().getX() + ", "+ pt.get().getLocation().getY());
//            }
//        });
//        t1.start();


        //startButton 초기화
        startButton.setText("시작");
        startButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill:#ffffff;");

        startButton.setOnAction(event -> {
            gonginPassword = gonginAuthField.getText();
            gonginAuthField.setText("");
            fieldContent.append("프로그램을 시작합니다.\n");
            processDisplay.setText(fieldContent.toString());
            //Driver가 실행되는 동안 프로그램이 무한로딩이라 Thread 사용함.
            t1.start();
        });

        //chromeButton 초기화
        chromeButton.setOnAction(event -> {
            if (t2.getState() == Thread.State.NEW) {
                t2.start();
            }
        });

        chromeButton.setText("로그인 및 셋팅이 완료되면 눌러주세요.");
        chromeButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill:#ffffff;");

        processDisplay.setText("프로그램 초기화...");
    }
}
