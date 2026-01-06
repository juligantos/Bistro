package logic.api.subjects;

import java.util.List;

import entities.Bill;
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
		router.on("payment", "processManually.ok", msg -> {
			// Handle successful manual payment processing
			BistroClientGUI.client.getPaymentCTRL().setIsPaymentManuallySuccessful(true);
			BistroClientGUI.client.getTableCTRL().clearCurrentTable();
		});
		router.on("payment", "processManually.fail", msg -> {
		});
		router.on("payment", "getPendingBills.ok", msg -> {
			BistroClientGUI.client.getPaymentCTRL().setPendingBills((List<Bill>) msg.getData());
		});
		router.on("payment", "getPendingBills.fail", msg -> {
		});
	}
	
}
