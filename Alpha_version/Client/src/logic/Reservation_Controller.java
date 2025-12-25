package logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import entities.Order;

public class Reservation_Controller {
	//****************************** Instance variables ******************************//
	private  final BistroClient client;
	private Map <LocalDate,TreeMap<LocalTime,Order>> activeReservations;
	
	//******************************** Constructors ***********************************//
	public Reservation_Controller(BistroClient client) {
		this.client = client;
		this.activeReservations = new TreeMap<>();
	}
	
	//******************************** Getters And Setters ***********************************//
	public Map<LocalDate, TreeMap<LocalTime, Order>> getActiveReservations() {
		return activeReservations;
	}
	
	public void setActiveReservations(Map<LocalDate, TreeMap<LocalTime, Order>> activeReservations) {
		this.activeReservations = activeReservations;
	}
	
	//******************************** Instance Methods ***********************************//
	
	
	
}
