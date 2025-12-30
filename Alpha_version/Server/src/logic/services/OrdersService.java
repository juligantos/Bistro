package logic.services;

import java.util.List;
import java.util.Map;

import entities.Order;
import enums.OrderType;
import logic.BistroDataBase_Controller;
import logic.BistroServer;
import logic.ServerLogger;

public class OrdersService {
	private final BistroServer server;
	private final BistroDataBase_Controller dbController;
	private final ServerLogger logger;
	
	
	public OrdersService(BistroServer server,BistroDataBase_Controller dbController, ServerLogger logger) {
		this.dbController = dbController;
		this.logger = logger;
		this.server = server;
	}


	public boolean createNewOrder(List<Object> data) {
		
		return dbController.setNewOrderToDataBase(data);
	}


	public Order getOrderByConfirmationCode(String confirmationCode, OrderType reservation) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<String> getAvailableReservationHours(Map<String, Object> requestData) {
		// TODO Auto-generated method stub
		return null;
	}

}
