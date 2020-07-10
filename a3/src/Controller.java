import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

// This is the controller class that handles input for our FXML form
// We specify this class in the FXML file when we create it.

public class Controller {

    @FXML
    private Label label;

    public void initialize() {
        // TODO
    }

    // General ActionEvent Handler

    @FXML
    private void handleButtonAction(ActionEvent event) {
        label.setText("Hello World!");
    }
}
