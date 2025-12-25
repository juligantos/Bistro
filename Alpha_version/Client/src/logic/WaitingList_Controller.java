package logic;

import java.util.Map;

public class WaitingList_Controller {
	
	//****************************** Instance variables ******************************
	private final BistroClient client;
	private Map<LocalDate, TreeMap<LocalTime, Order>> waitingList;
	
	//******************************** Constructors ***********************************//
	public WaitingList_Controller(BistroClient client) {
		this.client = client;
		
	}
}
