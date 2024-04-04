module org.auto.minwonauto {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter.api;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires org.seleniumhq.selenium.java;
    requires org.seleniumhq.selenium.edge_driver;
    requires org.seleniumhq.selenium.os;
    requires org.seleniumhq.selenium.devtools_v123;
    requires org.seleniumhq.selenium.http;
    requires org.seleniumhq.selenium.manager;
    requires org.seleniumhq.selenium.remote_driver;
    requires org.seleniumhq.selenium.support;
    requires org.seleniumhq.selenium.json;
    requires org.seleniumhq.selenium.devtools_v85;
    requires org.seleniumhq.selenium.devtools_v121;
    requires org.seleniumhq.selenium.devtools_v122;
    requires dev.failsafe.core;

    opens org.auto.minwonauto to javafx.fxml;
    exports org.auto.minwonauto;
}