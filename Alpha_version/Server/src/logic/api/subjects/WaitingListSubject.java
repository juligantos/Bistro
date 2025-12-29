package logic.api.subjects;

import java.util.Map;

import comms.Api;
import comms.Message;
import entities.Order;
import entities.User;
import enums.OrderType;
import logic.BistroDataBase_Controller;
import logic.ServerLogger;
import logic.api.Router;

public class WaitingListSubject {
	private WaitingListSubject() {
	}
	
	public static void register(Router router, BistroDataBase_Controller dbController, ServerLogger logger) {
		// Handlers related to waiting list can be added here
		
		//join to waiting list
		router.on("waitingList", "join", (msg, client) -> {
			Map<String, Object> userData = (Map<String, Object>) msg.getData();
			Order createdOrder = dbController.addToWaitingList(userData);
			if (createdOrder.getOrderType()==OrderType.WAITLIST) {
				client.sendToClient(new Message(Api.REPLY_WAITING_LIST_JOIN_OK, createdOrder));
				logger.log("[INFO] Client: "+ client +" created a waiting list order successfully.");
			} else if(createdOrder.getOrderType()==OrderType.RESERVATION) {
				client.sendToClient(new Message(Api.REPLY_WAITING_LIST_SKIPPED, createdOrder));
				logger.log("[INFO] Client: "+ client + " joined the waiting list and got a reservation successfully.");
			}
			else {
				client.sendToClient(new Message(Api.REPLY_WAITING_LIST_JOIN_FAIL, null));
				logger.log("[ERROR] Client: "+ client + " failed to join the waiting list.");
			}
		});
		
		//leave waiting list
		router.on("waitingList", "leave", (msg, client) -> {
			String confirmationCode = (String) msg.getData();
			boolean success = dbController.removeFromWaitingList(confirmationCode);
			if (success) {
				client.sendToClient(new Message(Api.REPLY_WAITING_LIST_LEAVE_OK, null));
				logger.log("[INFO] Client: "+ client + " left the waiting list successfully.");
			} else {
				client.sendToClient(new Message(Api.REPLY_WAITING_LIST_LEAVE_FAIL, null));
				logger.log("[ERROR] Client: "+ client + " failed to leave the waiting list.");
			}
		});
		
	}
}
