module dev.meoftbanana {
    requires javafx.controls;
    requires javafx.fxml;

    opens dev.meoftbanana to javafx.fxml;
    exports dev.meoftbanana;
}
