module com.example.chatapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.chatapplication to javafx.fxml;
    exports com.example.chatapplication;
}