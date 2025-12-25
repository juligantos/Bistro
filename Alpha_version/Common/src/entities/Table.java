package entities;

public class Table {
	//****************************** Instance variables ******************************/
	private int tableNumber;
	private int capacity;
	private boolean isAvailable;
	//******************************** Constructors ***********************************//
	public Table(int tableNumber, int capacity, boolean isAvailable) {
		this.tableNumber = tableNumber;
		this.capacity = capacity;
		this.isAvailable = isAvailable;
	}
	//******************************** Getters And Setters ***********************************//
	public int getTableNumber() {
		return tableNumber;
	}
	
	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	
}
