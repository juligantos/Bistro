package entities;

import java.io.Serializable;
import java.util.Map;

/**
 * MonthlyReport entity representing the data structure for monthly reservation reports.
 */
public class MonthlyReport implements Serializable {
    private static final long serialVersionUID = 1L;
    //************************ Instance Variables ************************//
	private int totalReservationsInGivenYear; // can be for given year to compare year over year
	private double totalReservationsDelta; // delta means change from previous period

	private int avgMonthlyReservations;
	private double avgMonthlyDelta; // delta means change from previous period

	private double onTimeArrivalRate; // percentage of on-time arrivals
	private double onTimeArrivalDelta; // delta means change from previous period

	private int customersThisMonth;
	private String currentMonthName;

	// **** Chart Arrival Times (Bar Chart) ****
	// Key: Category ("On Time", "Late < 15m", "Late > 15m"), Value: Count
	private Map<String, Integer> arrivalTimeDistribution; // Distribution means how many in each time category
	private int totalOnTime;
	private int totalLate;

	// **** Chart Monthly Trends (Line Chart) ****
	// Key: Month Name, Value: Count
	private Map<String, Integer> monthlyReservationsMap;

	// **** Summary Box Data ****
	private String peakMonth;
	private int peakMonthValue;
	private String lowestMonth;
	private int lowestMonthValue;
	private double growthRateYearly;

	// **** Bottom Cards: Distributions ****
	// Key: Hour ("18:00"), Value: Count
	private Map<String, Integer> peakReservationTimes;
	// Key: Diners Amount ("2 Guests"), Value: Count
	private Map<String, Integer> dinersAmountDistribution;

	//************************ Constructors ************************//
	
	public MonthlyReport() {
	}
	
	//************************ Getters and Setters ************************//
	
	public int getTotalReservationsInGivenYear() {
		return totalReservationsInGivenYear;
	}

	public void setTotalReservationsInGivenYear(int totalReservationsInGivenYear) {
		this.totalReservationsInGivenYear = totalReservationsInGivenYear;
	}

	public Map<String, Integer> getArrivalTimeDistribution() {
		return arrivalTimeDistribution;
	}

	public void setArrivalTimeDistribution(Map<String, Integer> arrivalTimeDistribution) {
		this.arrivalTimeDistribution = arrivalTimeDistribution;
	}

	public Map<String, Integer> getMonthlyReservationsMap() {
		return monthlyReservationsMap;
	}

	public void setMonthlyReservationsMap(Map<String, Integer> monthlyReservationsMap) {
		this.monthlyReservationsMap = monthlyReservationsMap;
	}

	public Map<String, Integer> getPeakReservationTimes() {
		return peakReservationTimes;
	}

	public void setPeakReservationTimes(Map<String, Integer> peakReservationTimes) {
		this.peakReservationTimes = peakReservationTimes;
	}

	public Map<String, Integer> getDinersAmountDistribution() {
		return dinersAmountDistribution;
	}

	public void setDinersAmountDistribution(Map<String, Integer> dinersAmountDistribution) {
		this.dinersAmountDistribution = dinersAmountDistribution;
	}

	public double getOnTimeArrivalRate() {
		return onTimeArrivalRate;
	}

	public void setOnTimeArrivalRate(double onTimeArrivalRate) {
		this.onTimeArrivalRate = onTimeArrivalRate;
	}

	public int getTotalOnTime() {
		return totalOnTime;
	}

	public void setTotalOnTime(int totalOnTime) {
		this.totalOnTime = totalOnTime;
	}

	public int getTotalLate() {
		return totalLate;
	}

	public void setTotalLate(int totalLate) {
		this.totalLate = totalLate;
	}

	public char[] getAvgMonthlyReservations() {
		char[] value = String.valueOf(avgMonthlyReservations).toCharArray();
		return value;
	}
	
	public void setAvgMonthlyReservations(int avgMonthlyReservations) {
		this.avgMonthlyReservations = avgMonthlyReservations;
	}
	
	public char[] getCustomersThisMonth() {
		char[] value = String.valueOf(customersThisMonth).toCharArray();
		return value;
	}
	
	public void setCustomersThisMonth(int customersThisMonth) {
		this.customersThisMonth = customersThisMonth;
	}

	public String getPeakMonth() {
		return peakMonth;
	}

	public char[] getPeakMonthValue() {
		char[] value = String.valueOf(peakMonthValue).toCharArray();
		return value;
	}

	public String getLowestMonth() {
		return lowestMonth;
	}

	public char[] getLowestMonthValue() {
		char[] value = String.valueOf(lowestMonthValue).toCharArray();
		return value;
	}

	public double getGrowthRateYearly() {
		return growthRateYearly;
	}
	
	public void setGrowthRateYearly(double growthRateYearly) {
		this.growthRateYearly = growthRateYearly;
	}
}