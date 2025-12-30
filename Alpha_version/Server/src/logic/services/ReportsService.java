package logic.services;

import logic.BistroDataBase_Controller;
import logic.ServerLogger;

public class ReportsService {
	private final BistroDataBase_Controller dbController;
	private final ServerLogger logger;
	public ReportsService(BistroDataBase_Controller dbController, ServerLogger logger) {
		this.dbController = dbController;
		this.logger = logger;
	}
	
}
