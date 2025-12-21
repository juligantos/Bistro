package logic;

import entities.Guest;
import entities.Member;
import entities.User;

public class User_Controller {
	public static User_Controller instance = null;
	private Guest loggedInGuest = null;
	private Member loggedInMember = null;
	private Employee loggedInEmployee = null;
	private Manager loggedInManager = null;
	
	public static User_Controller getInstance() {
		if (instance == null) {
			instance = new User_Controller();
		}
		return instance;
	}
}
