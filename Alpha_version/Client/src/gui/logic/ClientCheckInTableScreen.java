package gui.logic;

import entities.User;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import logic.BistroClientGUI;

/**
 * This class represents the controller for the Client Check-In Table screen in
 * the BistroClientGUI.
 */
public class ClientCheckInTableScreen {
	// ****************************** FXML Elements ******************************
	@FXML
	private Button btnCheckIn;
	
	@FXML
	private Button btnBack;
	
	@FXML
	private Hyperlink lnkForgot;
	
	@FXML
	private TextField txtConfirmCode;
	@FXML
	private Label lblUser;
	@FXML
	private Label lblError;
	@FXML
	private StackPane modalOverlay;
	
	private StackPane currentScreen;
	@FXML
	private StackPane ScreenContainer;
	
	private ClientForgotConfirmCodeScreen forgotModalsCTRL;
	
	// ****************************** Instance Methods ******************************

	/**
	 * Initializes the Client Check-In Table screen.
	 */
	private Parent ForgotIDModalRoot;
	@FXML
	public void initialize() {
		User currentUser = BistroClientGUI.client.getUserCTRL().getLoggedInUser();
		lblUser.setText(currentUser.getUserType().name());
		int maxLength = 8;
		txtConfirmCode.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > maxLength) {
				txtConfirmCode.setText(oldValue);
			}
		});
	}
	
	/**
	 * Handles the Check-In button click event.
	 *
	 * @param event The event triggered by clicking the Check-In button.
	 */
	@FXML
	public void btnCheckIn(Event event) {
		String testConfirmationCode = txtConfirmCode.getText();
		BistroClientGUI.client.getReservationCTRL().CheckConfirmationCodeCorrect(testConfirmationCode);
		if (BistroClientGUI.client.getTableCTRL().isCheckInTableSuccess()) {
			BistroClientGUI.switchScreen(event, "clientCheckInTableSucces", "clientCheckIn error messege");
		} else {
			BistroClientGUI.display(lblError, "Error has been occured!", Color.RED);																		
		}
	}
	
	/**
	 * Handles the Back button click event.
	 *
	 * @param event The event triggered by clicking the Back button.
	 */
	@FXML
	public void btnBack(Event event) {
		try {
			BistroClientGUI.switchScreen(event, "clientDashboardScreen", "Client back error messege");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void lnkForgot(Event event) {
		if (ForgotIDModalRoot == null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/ClientForgotConfirmationCode.fxml"));
			try {
				ForgotIDModalRoot = loader.load();
			} catch (Exception e) {
				e.printStackTrace();
				BistroClientGUI.display(lblError, "Unable to open Forgot Member ID screen.", Color.RED);
			}
			forgotModalsCTRL = loader.getController();
			forgotModalsCTRL.setParent(this);
			modalOverlay.getChildren().add(ForgotIDModalRoot);
			modalOverlay.setVisible(true);
			modalOverlay.setManaged(true);
		}
		return;
	}
	
	
	/**
	 * Displays an error message on the screen.
	 *
	 * @param message The error message to display.
	 */
	public void showSuccessMessage(String message) {
        BistroClientGUI.display(lblError, message, Color.GREEN);
    }
	
	
	
	/**
	 * Closes the Forgot Code modal screen.
	 */
	public void closeForgotCodeScreen() {
        // Logic to remove the modal from the screen
        if (ScreenContainer != null && currentScreen != null) {
            ScreenContainer.getChildren().remove(currentScreen);
            currentScreen = null;
        } else {
        	Alert alert = new Alert(Alert.AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText(null);
        	alert.setContentText("Unable to close the Forgot Code screen.");
        	alert.showAndWait();
        }
    }
}
//End of ClientCheckInTableScreen.java