package logic.services;

import java.util.Map;

import entities.User;
import logic.BistroDataBase_Controller;
import logic.ServerLogger;

public class UserService {
	private final BistroDataBase_Controller dbController;
	private final ServerLogger logger;
	
	public UserService(BistroDataBase_Controller dbController, ServerLogger logger) {
		this.dbController = dbController;
		this.logger = logger;
	}

	public User getUserInfo(Map<String, Object> loginData) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean updateUserInfo(User updatedUser) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
