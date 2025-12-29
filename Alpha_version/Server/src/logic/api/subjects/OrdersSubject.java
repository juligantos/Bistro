package logic.api.subjects;

import java.util.List;

import comms.Api;
import comms.Message;
import entities.Order;
import logic.BistroDataBase_Controller;
import logic.BistroServerGUI;
import logic.ServerLogger;
import logic.api.Router;
import enums.OrderType;



/**
 * API handlers related to orders.
 */
public final class OrdersSubject {

    private OrdersSubject() {}
    /**
     * Registers all order related handlers.
     * @param router
     * @param logger 
     * @param dbController
     */
    public static void register(Router router, BistroDataBase_Controller dbController) {
    	
        // Get all orders
        router.on("orders", "list", (msg, client) -> {
            List<Order> orders = dbController.getAllOrders();
            client.sendToClient(new Message(Api.REPLY_ORDERS_LIST_RESULT, orders));
        });

        // Update order status
        router.on("orders", "updateStatus", (msg, client) -> {
            Order order = (Order) msg.getData();

            if (order.getOrderType() == OrderType.RESERVATION && order.getOrderDate() != null) {
                boolean available = dbController.isDateAvailable(
                        order.getOrderDate(),
                        order.getConfirmationCode());

                if (!available) {
                    client.sendToClient(new Message(Api.REPLY_ORDERS_UPDATE_DATE_NOT_AVAILABLE, null));
                    return;
                }
            }


            boolean updated = dbController.updateOrder(order);

            if (updated) {
                client.sendToClient(
                        new Message(Api.REPLY_ORDERS_UPDATE_OK, null));
            } else {
                client.sendToClient(
                        new Message(Api.REPLY_ORDERS_UPDATE_INVALID_CONFIRM_CODE, null));
            }
        });

        // Get order by confirmation code
        router.on("orders", "getByCode", (msg, client) -> {
            int code = (int) msg.getData();
            client.sendToClient(
                    new Message(
                            Api.REPLY_ORDERS_GET_BY_CODE_RESULT,
                            dbController.getOrderByConfirmationCode(code)));
        });
    }
}
