module amarit.motionsim {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
	requires java.desktop;
	requires com.google.gson;

	opens amarit.motionsim to javafx.fxml;
    exports amarit.motionsim;
}