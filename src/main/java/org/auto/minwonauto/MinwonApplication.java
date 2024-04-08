package org.auto.minwonauto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static org.auto.minwonauto.MinwonMainController.mouse;

public class MinwonApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        URL resourceUrl = MinwonApplication.class.getClassLoader().getResource("org/auto/minwonauto/minwon-main.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);

        Scene scene = new Scene(fxmlLoader.load(), 370,37);
        stage.setTitle("민원 자동처리기");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
        mouse.mouseMove(1800,600);
    }
}