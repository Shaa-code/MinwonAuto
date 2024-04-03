package org.auto.minwonauto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        URL resourceUrl = HelloApplication.class.getClassLoader().getResource("org/auto/minwonauto/hello-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);

        Scene scene = new Scene(fxmlLoader.load(), 380, 240);
        stage.setTitle("민원 자동처리기");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}