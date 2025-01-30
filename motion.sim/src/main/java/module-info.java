module amarit.motionsim {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
	requires java.desktop;
	requires com.google.gson;

	opens amarit.motionsim to javafx.fxml;
    opens data to com.google.gson;
    opens graphs to com.google.gson;

    exports amarit.motionsim;
}