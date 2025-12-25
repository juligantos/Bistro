package logic;

import java.util.ArrayList;
import java.util.List;

import entities.Order;

public class Table_Controller {
	//****************************** Instance variables ******************************
	private final BistroClient client;
	private List<Order> occupiedTables;
	
	//******************************** Constructors ***********************************//
	public Table_Controller(BistroClient client) {
		this.client = client;
		this.occupiedTables = new ArrayList<>();
	}
	
	//******************************** Getters And Setters ***********************************//	
	public List<Order> getOccupiedTables() {
		return occupiedTables;
	}
	
	public void setOccupiedTables(List<Order> occupiedTables) {
		this.occupiedTables = occupiedTables;
	}
	
	//******************************** Instance Methods ***********************************//
	
}
