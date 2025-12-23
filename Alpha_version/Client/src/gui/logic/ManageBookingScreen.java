package gui.logic;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;

public class ManageBookingScreen {
	
	@FXML
	private Button btnCheck;
	@FXML
	private Button btnBack;
	@FXML
	private Button btnCancel;
	@FXML
	private DatePicker datePicker;
	@FXML
	private ComboBox<String> dinersAmountComboBox;
	@FXML
	private GridPane timeSlotsGridPane;
	
	private String selectedTimeSlot = null;

}
