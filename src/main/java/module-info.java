module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;


    opens com.application.main to javafx.fxml;
    opens main.dao to com.google.gson;
    exports com.application.main;
}