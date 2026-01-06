package logic.api.subjects;

import java.util.HashMap;

import entities.Order;
import entities.Table;
import logic.BistroClient;
import logic.BistroClientGUI;
import logic.api.ClientRouter;

public class TablesSubject {

	public static void register(ClientRouter router) {
		router.on("orders", "order.exists", msg -> {
            BistroClient.awaitResponse = false;
			Order order = (Order) msg.getData();
			BistroClientGUI.client.getTableCTRL().setUserAllocatedOrderForTable(order);
			BistroClientGUI.client.getReservationCTRL().setReadyUserReservation(null);
		});
		router.on("orders", "order.notExists", msg -> {
		});

		router.on("table", "getStatus", msg -> {
            BistroClient.awaitResponse = false;
			HashMap<Table, String> tableStatuses = (HashMap<Table, String>) msg.getData();
			BistroClientGUI.client.getTableCTRL().updateTableStatuses(tableStatuses);
		});
	}

}
