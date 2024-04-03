module org.auto.minwonauto {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter.api;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires org.seleniumhq.selenium.java;
    requires org.seleniumhq.selenium.edge_driver;
    opens org.auto.minwonauto to javafx.fxml;
    exports org.auto.minwonauto;
}