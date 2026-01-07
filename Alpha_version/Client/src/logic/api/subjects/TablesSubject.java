package logic.api.subjects;

import java.util.HashMap;

import entities.Order;
import entities.Table;
import javafx.scene.control.Alert;
import logic.BistroClient;
import logic.BistroClientGUI;
import logic.api.ClientRouter;

public class TablesSubject {

	public static void register(ClientRouter router) {

		router.on("table", "getStatus.ok", msg -> {
            BistroClient.awaitResponse = false;
			HashMap<Table, String> tableStatuses = (HashMap<Table, String>) msg.getData();
			BistroClientGUI.client.getTableCTRL().updateTableStatuses(tableStatuses);
		});
		router.on("table", "getStatus.fail", msg -> {
			BistroClient.awaitResponse = false;
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not retrieve table statuses");
			alert.setContentText("An error occurred while fetching table statuses. Please try again later.");
			alert.showAndWait();
			
		});
	}

}
