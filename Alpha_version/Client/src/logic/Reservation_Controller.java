package logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import comms.Message;
import entities.Order;
import enums.DaysOfWeek;

public class Reservation_Controller {
	
	//****************************** Static variables ******************************//
	
	public static final int SLOT_MINUTES = 15;
	public static final int DURATION_MINUTES = 120;
	private static final int SLOTS_PER_RESERVATION = DURATION_MINUTES / SLOT_MINUTES;
	public static final int CAPACITY = 80;
	
	//****************************** Instance variables ******************************//
	
	private  final BistroClient client;
	
	
	//******************************** Constructors ***********************************//
	
	public Reservation_Controller(BistroClient client) {
		this.client = client;
		
	}
	
	//******************************** Getters And Setters ***********************************//
	
	
	//******************************** Instance Methods ***********************************//
	
	
	
}
