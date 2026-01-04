package logic.api.subjects;

import entities.Order;
import enums.OrderStatus;
import javafx.application.Platform;
import logic.BistroClientGUI;
import logic.api.ClientRouter;

public class WaitListSubject {

	private WaitListSubject() {
	}

	public static void register(ClientRouter router) {
		// Handler for user on waiting list status messages
		
		router.on("waitinglist", "isInWaitingList.yes", msg -> {;
			OrderStatus status = OrderStatus.WAITING_LIST;
		BistroClientGUI.client.getReservationCTRL().getReadyUserReservation().setStatus(status);
		});
		
		router.on("waitinglist", "isInWaitingList.no", msg -> {
		});
		
		router.on("waitinglist", "join.ok", msg -> {
			Order order = (Order) msg.getData();
			BistroClientGUI.client.getReservationCTRL().setReadyUserReservation(order);
		});
		
		router.on("waitinglist", "join.fail", msg -> {
		});
		
		router.on("waitinglist", "leave.ok", msg -> {
			BistroClientGUI.client.getReservationCTRL().setReadyUserReservation(null);
		});
		router.on("waitinglist", "leave.fail", msg -> {
		});
		router.on("waitinglist", "sit.ok", msg -> {
			OrderStatus status = OrderStatus.SEATED;
		    BistroClientGUI.client.getReservationCTRL().getReadyUserReservation().setStatus(status);
		    BistroClientGUI.switchScreen("clientDashboardScreen", "Client Dashboard error message");
	    });
	}
}

