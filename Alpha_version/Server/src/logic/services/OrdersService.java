package logic.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import entities.Order;
import entities.Table;
import enums.OrderType;
import logic.BistroDataBase_Controller;
import logic.BistroServer;
import logic.ServerLogger;

public class OrdersService {
	// ******************************** Instance variables ***********************************
	private final BistroServer server;
	private final BistroDataBase_Controller dbController;
	private final ServerLogger logger;
	
	private final List<Integer> tableSizes; // e.g. [2,2,4,4,6,6,8]
	private final int slotStepMinutes; // 30
	private final int reservationDurationMinutes;// 120 

	
	// ******************************** Constructors***********************************
	public OrdersService(BistroServer server,BistroDataBase_Controller dbController, ServerLogger logger) {
		this.dbController = dbController;
		this.logger = logger;
		this.server = server;
		this.tableSizes = new ArrayList<Integer>();
		this.slotStepMinutes = 30;
		this.reservationDurationMinutes = 120;
		getTableSizes();
	}
	// ******************************** Methods***********************************
	
	/*
	 * Fetches all table sizes from the database and stores them in the tableSizes list.
	 */
	public void getTableSizes() {
		List<Table> tables = dbController.getAllTablesFromDB();
		for (Table table : tables) {
			tableSizes.add(table.getCapacity());
		}
		return;
	}
	


	public boolean createNewOrder(List<Object> data, OrderType orderType) {
		List<Object> orderData = data; //Received data contain : userId, date ,dinersAmount , time
		String confimationCode = generateConfirmationCode();
		int orderNumber = generateOrderNumber();
		orderData.add(orderNumber);
		orderData.add(confimationCode);
		orderData.add(orderType);
		orderData.add("PENDING");
		//orderData order: userId, date ,dinersAmount , time, orderNumber, confirmationCode, orderType, status
		return dbController.setNewOrderToDataBase(orderData); //DB should provide date of placing order
		
	}
	
	
	private int generateOrderNumber() {
		int num = 100000 + new Random().nextInt(900000);
		return num;
	}


	private String generateConfirmationCode() {
		int num = 100000 + new Random().nextInt(900000);
	    return "R-" + num;
	}
	
	
	public Order getOrderByConfirmationCode(String confirmationCode, OrderType reservation) {
		// TODO Auto-generated method stub
		return null;
	}

	//Available time slots logic methods:
	
	public List<String> getAvailableReservationHours(Map<String, Object> requestData) {
		List<LocalTime> openingHours = dbController.getOpeningHoursFromDB();
		LocalTime openingTime = openingHours.get(0);
		LocalTime closingTime = openingHours.get(1);
		LocalDate date = (LocalDate) requestData.get("date");
		int dinersAmount = (int) requestData.get("dinersAmount");
		List<Order> reservationsByDate = dbController.getReservationsbyDate(date);
		return computeAvailableSlots(openingTime, closingTime, dinersAmount, reservationsByDate);
	}
	
	

	public List<String> computeAvailableSlots(LocalTime openingTime, LocalTime closingTime, int newDinersAmount,
			List<Order> reservationsByDate) {
		List<LocalTime> possibleTimeSlots = buildPossibleTimeSlots(openingTime, closingTime);

		// Build buckets: slotTime -> list of guests for reservations overlapping slot window
		Map<LocalTime, List<Integer>> tablesPerTime = new HashMap<>();
		for (LocalTime slot : possibleTimeSlots){
			tablesPerTime.put(slot, new ArrayList<>()); 
		}
		// For each reservation, add it to every slot that overlaps [slot, slot+window) with [res, res+window)
		for (Order o : reservationsByDate) {
			LocalTime orderStart = o.getOrderHour();
			LocalTime orderEnd = orderStart.plusMinutes(reservationDurationMinutes);
			
			for (LocalTime slot : possibleTimeSlots) {
				LocalTime slotStartTime = slot;
				LocalTime slotEndTime = slotStartTime.plusMinutes(reservationDurationMinutes);
				// Check overlap
				if (overlaps(slotStartTime, slotEndTime, orderStart, orderEnd)) {
					tablesPerTime.get(slot).add(o.getDinersAmount());
				}
			}
		}

		List<String> available = new ArrayList<>();
		for (LocalTime slot : possibleTimeSlots) {
			List<Integer> overlappingDinersAmounts = new ArrayList<>(tablesPerTime.get(slot));
			overlappingDinersAmounts.add(newDinersAmount);
			
			if (canAssignAllDinersToTables(overlappingDinersAmounts, tableSizes)) {
				available.add(timeToString(slot));
			}
		}
		return available;
	}
	
	/**
	 * Builds all possible time slots within opening hours that can accommodate
	 * a full planning window.
	 */
	private List<LocalTime> buildPossibleTimeSlots(LocalTime openingTime, LocalTime closingTime) {
		// Candidate slot must allow full planning window inside opening hours
		LocalTime lastTimeSlot = closingTime.minusMinutes(reservationDurationMinutes);

		List<LocalTime> slots = new ArrayList<>();
		for (LocalTime t = openingTime; !t.isAfter(lastTimeSlot); t = t.plusMinutes(slotStepMinutes)) {
			slots.add(t);
		}
		return slots;
	}
	
	/**
	 * Checks if two time intervals overlap.
	 */
    private boolean overlaps(LocalTime slotStartTime, LocalTime slotEndTime, LocalTime orderStart, LocalTime orderEnd) {
        return slotStartTime.isBefore(orderEnd) && orderStart.isBefore(slotEndTime);
    }

    /**
	 * Checks if it is possible to assign all diners amounts to available tables.
	 */
	public static boolean canAssignAllDinersToTables(List<Integer> overlappingDinersAmounts, List<Integer> tableSizes) {
		List<Integer> overlappingDinersAmountsCopy = new ArrayList<>(overlappingDinersAmounts);
		overlappingDinersAmountsCopy.sort(Comparator.reverseOrder());
		TreeMap<Integer, Integer> tableSizeCounts = new TreeMap<>();
		for (int t : tableSizes) {
			if (tableSizeCounts.containsKey(t)) {
				tableSizeCounts.put(t, tableSizeCounts.get(t) + 1);
			} else {
				tableSizeCounts.put(t, 1);
			}
		}

		for (int i : overlappingDinersAmountsCopy) {
			Integer chosen = tableSizeCounts.ceilingKey(i); // smallest table >= p
			if (chosen == null) {
				return false;
			}
			int count = tableSizeCounts.get(chosen);
			if (count == 1) {
				tableSizeCounts.remove(chosen);
			} else {
				tableSizeCounts.put(chosen, count - 1);
			}
		}
		return true;
	}

    private String timeToString(LocalTime time) {
        // "HH:mm"
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }


}
