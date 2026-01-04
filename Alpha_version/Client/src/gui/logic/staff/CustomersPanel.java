package gui.logic.staff;

import enums.UserType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import logic.BistroClientGUI;

public class CustomersPanel {
@FXML
public Label totalCustomersLabel;

@FXML
public Label membersLabel;

@FXML
public Label walkinsLabel;

@FXML
public TextField searchField;

@FXML
public Button btnRefresh;

@FXML
public Label directoryTitleLabel;

@FXML
public TableView customersTable;
public void initialize() {
	BistroClientGUI.client.getUserCTRL().loadCustomersData();
	if(BistroClientGUI.client.getUserCTRL().isCustomersDataLoaded()) {
		int totalCustomers = BistroClientGUI.client.getUserCTRL().getCustomersData().size();
		totalCustomersLabel.setText(String.valueOf(totalCustomers));
		membersLabel.setText(String.valueOf(BistroClientGUI.client.getUserCTRL().getCustomersData().stream().filter(c -> c.getUserType()== UserType.MEMBER).count()));
		walkinsLabel.setText(String.valueOf(totalCustomers - Integer.parseInt(membersLabel.getText())));
		customersTable.setItems(BistroClientGUI.client.getUserCTRL().getCustomersData());
	}
}
}
