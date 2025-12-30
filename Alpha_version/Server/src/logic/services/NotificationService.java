package logic.services;

import logic.BistroDataBase_Controller;
import logic.ServerLogger;

public class NotificationService {
	
	private final BistroDataBase_Controller dbController;
	private final ServerLogger logger;

	public NotificationService(BistroDataBase_Controller dbController, ServerLogger logger) {
		this.dbController = dbController;
		this.logger = logger;
	}

}
