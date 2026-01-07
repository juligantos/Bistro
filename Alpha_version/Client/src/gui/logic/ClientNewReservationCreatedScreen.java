package gui.logic;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import logic.BistroClientGUI;

public class ClientNewReservationCreatedScreen {

    @FXML
    private Label lblDate;
    
    @FXML
    private Label lblHour;
    
    @FXML
    private Label lblDinersAmount;
    
    @FXML
    private Label lblConfirmCode;
    
    @FXML
    private Button btnBack;
    
    @FXML
    private Button btnNewReserve;

    /**
     * Better practice: Fetch data inside initialize() to ensure 
     * the Client instance is fully ready.
     */
    @FXML
    public void initialize() {
        try {
            List<Object> reservationDetails = BistroClientGUI.client.getReservationCTRL().getTempReservationData();
            String confirmCode = BistroClientGUI.client.getReservationCTRL().getConfirmationCode();

            if (reservationDetails != null && reservationDetails.size() >= 3) {
                String date = reservationDetails.get(0).toString();
                LocalTime time = (LocalTime) reservationDetails.get(1);
                int dinersAmount = (Integer) reservationDetails.get(2);

                lblDate.setText(date);
                // Formatting time to HH:mm for better UX
                lblHour.setText(time.format(DateTimeFormatter.ofPattern("HH:mm")));
                lblDinersAmount.setText(dinersAmount + " People");
                lblConfirmCode.setText(confirmCode);
            }
        } catch (Exception e) {
            System.err.println("Error loading reservation details: " + e.getMessage());
        }
    }

    @FXML
    public void btnBack(Event event) {
        cleanupAndSwitch(event, "clientDashboardScreen");
    }

    @FXML
    public void btnNewReserve(Event event) {
        cleanupAndSwitch(event, "clientNewReservationScreen");
    }

    /**
     * Helper method to reduce code duplication
     */
    private void cleanupAndSwitch(Event event, String screenName) {
        List<Object> data = BistroClientGUI.client.getReservationCTRL().getTempReservationData();
        BistroClientGUI.client.getReservationCTRL().deleteTempReservationData(data);
        
        String errorMsg = "Error navigating to " + screenName;
        BistroClientGUI.switchScreen(event, screenName, errorMsg);
    }
}