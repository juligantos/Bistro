package logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.TreeMap;

import entities.Order;

public class WaitingList_Controller {
	
	//****************************** Instance variables ******************************//
	private final BistroClient client;
	private Map<LocalDate, TreeMap<LocalTime, Order>> waitingList;
	//******************************** Constructors ***********************************//
	public WaitingList_Controller(BistroClient client) {
		this.client = client;
		
	}
}
