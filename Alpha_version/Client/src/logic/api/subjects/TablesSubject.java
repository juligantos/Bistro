package logic.api.subjects;

import entities.Order;
import logic.BistroClientGUI;
import logic.api.ClientRouter;

public class TablesSubject {

	public static void register(ClientRouter router) {
		router.on("orders", "order.exists", msg -> {
			Order order = (Order) msg.getData();
			BistroClientGUI.client.getTableCTRL().setUserAllocatedOrderForTable(order);
			BistroClientGUI.client.getReservationCTRL().setReadyUserReservation(null);
		});
		router.on("orders", "order.notExists", msg -> {
		});

		
	}

}
