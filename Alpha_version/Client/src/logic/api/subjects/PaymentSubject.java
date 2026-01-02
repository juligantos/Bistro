package logic.api.subjects;

import logic.BistroClient;
import logic.BistroClientGUI;
import logic.PaymentController.PaymentStatus;
import logic.api.ClientRouter;

public class PaymentSubject {

	public static void register(ClientRouter router) {
		router.on("payment", "complete.ok", msg -> {
			// Handle successful payment processing
			BistroClientGUI.client.getPaymentCTRL().setPaymentStatus(PaymentStatus.COMPLETED.name());
			BistroClientGUI.client.getTableCTRL().clearCurrentTable();
		});
		router.on("payment", "complete.fail", msg -> {
			// Handle failed payment processing
			BistroClientGUI.client.getPaymentCTRL().setPaymentStatus(PaymentStatus.FAILED.name());
		});
		
	}
	
}
