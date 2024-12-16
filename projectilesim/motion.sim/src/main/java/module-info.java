module amarit.motionsim {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;

    opens amarit.motionsim to javafx.fxml;
    exports amarit.motionsim;
}