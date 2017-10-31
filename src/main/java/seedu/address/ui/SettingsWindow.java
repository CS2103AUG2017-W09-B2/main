package seedu.address.ui;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class SettingsWindow {

    private static final Logger logger = LogsCenter.getLogger(SettingsWindow.class);
    private static final String ICON = "/images/address_book_32_alternative.png";
    private static final String FXML = "view/SettingsWindow.fxml";
    private static final String TITLE = "Password Settings";

    private Stage stage;

    @FXML
    private TextField oldPassword;

    @FXML
    private TextField newPassword;

    @FXML
    private TextField confirmPassword;

    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

    public void setSettingsStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {

    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {

    }

}
