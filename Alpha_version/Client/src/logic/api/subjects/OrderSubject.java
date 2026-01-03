package logic.api.subjects;

import javafx.application.Platform;
import logic.BistroClient;
import logic.BistroClientGUI;
import logic.api.ClientRouter;
import java.util.List;

public class OrderSubject {
	
	private OrderSubject() {}
	
	public static void register(ClientRouter router) {
		// Handler for new reservation creation messages
		router.on("orders", "createReservation.ok", msg -> {
			String confirmationCode = (String) msg.getData();
			Platform.runLater(() -> BistroClientGUI.client.getReservationCTRL().setConfirmationCode(confirmationCode));
		});
		router.on("orders","createReservation.fail", msg -> {
		});
		// This tells the router: "When the server sends 'getAvailableHours.ok', update the controller."
		router.on("orders", "getAvailableHours.ok", (msg) -> {
            @SuppressWarnings("unchecked")
            List<String> slots = (List<String>) msg.getData();
            BistroClientGUI.client.getReservationCTRL().setAvailableTimeSlots(slots);  
            BistroClient.awaitResponse = false; 
        });
		
	}
	
}
