module project {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens view.graphic to javafx.fxml;
    exports view.graphic;
}