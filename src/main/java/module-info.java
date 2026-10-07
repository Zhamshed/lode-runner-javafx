module com.example.java1_2023_rab0046 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.java1_2023_rab0046 to javafx.fxml;
    exports com.example.java1_2023_rab0046;
}