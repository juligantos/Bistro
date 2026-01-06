package gui.logic.staff;

import java.util.List;
import java.util.stream.Collectors;

import entities.Order;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import logic.BistroClientGUI;

public class PaymentPanel {

    @FXML
    private Label lblPendingBills;
    
    @FXML
    private Label lblTodayRevenue;
    
    @FXML
    private Label lblAvgBill;
    
    @FXML
    private Label pendingTitleLabel;
    
    @FXML
    private TextField searchField;
    
    @FXML
        private Button btnRefreshList;
    
    @FXML
    private Button btnPaymentReservation;

    @FXML
    private TableView<Order> billsTable;
    
    @FXML
    private TableColumn<Order, String> colTable;
    
    @FXML
    private TableColumn<Order, Integer> colBillId;
    
    @FXML
    private TableColumn<Order, String> colCustomer; 
    
    @FXML
    private TableColumn<Order, String> colMember; 
    
    @FXML
    private TableColumn<Order, String> colCreated;
    
    @FXML
    private TableColumn<Order, String> colTotal;    

    private final ObservableList<Order> masterData = FXCollections.observableArrayList();
    private FilteredList<Order> filteredData;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupSearchLogic();
        refreshData();
    }

    private void setupTableColumns() {
        colBillId.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("confirmationCode"));
        colMember.setCellValueFactory(new PropertyValueFactory<>("userId"));

        colCreated.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getOrderDate() != null ? cell.getValue().getOrderDate().toString() : "N/A")); 
        
        colTotal.setCellValueFactory(cell -> {
            double estimatedPrice = cell.getValue().getDinersAmount() * 150.0; //TODO: Example calculation
            return new SimpleStringProperty(String.format("₪%.2f", estimatedPrice));
        });

        colTable.setCellValueFactory(cell -> {
            int orderNum = cell.getValue().getOrderNumber();
            // We look for a table where this order is sitting
            return new SimpleStringProperty("T-ID: " + orderNum);
        });
        
        billsTable.setPlaceholder(new Label("No occupied tables with pending bills."));
    }

    private void setupSearchLogic() {
        filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(order -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();

                return String.valueOf(order.getOrderNumber()).contains(lowerCaseFilter) ||
                       (order.getConfirmationCode() != null && order.getConfirmationCode().toLowerCase().contains(lowerCaseFilter)) ||
                       String.valueOf(order.getUserId()).contains(lowerCaseFilter);
            });
        });

        SortedList<Order> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(billsTable.comparatorProperty());
        billsTable.setItems(sortedData);
    }

    @FXML
    void btnRefreshList(Event event) {
        refreshData();
        searchField.clear();
    }

    private void refreshData() {
        BistroClientGUI.client.getPaymentCTRL().loadPendingBills();
        
        BistroClientGUI.client.getTableCTRL().requestTableStatus();

        if (BistroClientGUI.client.getPaymentCTRL().isBillsLoaded()) {
            updateUI(BistroClientGUI.client.getPaymentCTRL().getPendingBills());
        } else {
            clearStats();
        }
    }

    public void updateUI(List<Order> allBills) {
        if (allBills == null) return;

        // Filtering logic: only show orders that are currently "SEATED" 
        // and belong to occupied tables
        List<Order> activeBills = allBills.stream()
                .filter(order -> order.getStatus() == enums.OrderStatus.SEATED)
                .collect(Collectors.toList());

        // Calculate stats
        int count = activeBills.size();
        double totalRevenue = activeBills.stream().mapToDouble(o -> o.getDinersAmount() * 150.0).sum();//TODO: Example calculation
        double avg = count > 0 ? totalRevenue / count : 0;

        Platform.runLater(() -> {
            masterData.setAll(activeBills);
            lblPendingBills.setText(String.valueOf(count));
            lblTodayRevenue.setText(String.format("₪%.2f", totalRevenue));
            lblAvgBill.setText(String.format("₪%.2f", avg));
            pendingTitleLabel.setText("Active Table Bills (" + count + ")");
        });
    }

    private void clearStats() {
        Platform.runLater(() -> {
            masterData.clear();
            lblPendingBills.setText("0");
            lblTodayRevenue.setText("₪0.00");
            lblAvgBill.setText("₪0.00");
            pendingTitleLabel.setText("Active Table Bills (0)");
        });
    }

    @FXML
    void btnPaymentReservation(Event event) {
        Order selectedOrder = billsTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a bill to process payment.").showAndWait();
            return;
        }
        processManualPayment(selectedOrder);
    }

    private void processManualPayment(Order order) {
        // Create the custom dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Finalize Payment - Order #" + order.getOrderNumber());
        dialog.setHeaderText("Processing payment for Order: " + order.getConfirmationCode());

        // Set the button types (Complete and Cancel)
        ButtonType payButtonType = new ButtonType("Complete Payment", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(payButtonType, ButtonType.CANCEL);

        // Create the layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        String tableIdStr = "N/A";
        if (BistroClientGUI.client.getTableCTRL().getTableStatuses() != null) {
            tableIdStr = BistroClientGUI.client.getTableCTRL().getTableStatuses().entrySet().stream()
                    .filter(entry -> entry.getValue() != null && entry.getValue().contains(order.getConfirmationCode()))
                    .map(entry -> String.valueOf(entry.getKey().getTableID()))
                    .findFirst().orElse("Seated");
        }

        // Form Fields
        TextField tableField = new TextField(tableIdStr);
        tableField.setEditable(false);
        tableField.setDisable(true);

        double totalAmount = order.getDinersAmount() * 150.0; //TODO: Placeholder price calculation
        TextField amountField = new TextField(String.format("%.2f", totalAmount));
        amountField.setEditable(false);
        amountField.setDisable(true);

        ComboBox<String> paymentMethod = new ComboBox<>();
        paymentMethod.getItems().addAll("Credit Card", "Cash", "Member Balance");
        paymentMethod.setValue("Credit Card");
        paymentMethod.setMaxWidth(Double.MAX_VALUE);

        grid.add(new Label("Table Number:"), 0, 0);
        grid.add(tableField, 1, 0);
        
        grid.add(new Label("Total Amount (₪):"), 0, 1);
        grid.add(amountField, 1, 1);
        
        grid.add(new Label("Payment Method:"), 0, 2);
        grid.add(paymentMethod, 1, 2);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(paymentMethod::requestFocus);

        Node payButton = dialog.getDialogPane().lookupButton(payButtonType);
        payButton.disableProperty().bind(
        		paymentMethod.valueProperty().isNull()
            );
        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == payButtonType) {
                return paymentMethod.getValue();
            }
            return null;
        });

        // Show and Handle Result
        dialog.showAndWait().ifPresent(method -> {
        	Parent rootNode = billsTable.getScene().getRoot();
        	rootNode.setDisable(true);
        	
            // Lock UI and show waiting state
            billsTable.setCursor(Cursor.WAIT);
            
            Thread updateThread = new Thread(() -> {
                try {
                    // Send payment to server with the chosen method
                	BistroClientGUI.client.getPaymentCTRL().processPayment(order.getOrderNumber());
                    boolean success = BistroClientGUI.client.getPaymentCTRL().getIsPaymentManuallySuccessful();

                    Platform.runLater(() -> {
                        if (success) {
                            showAlert("Success", "Payment processed via " + method, Alert.AlertType.INFORMATION);
                            refreshData(); 
                        } else {
                            showAlert("Error", "Server rejected the payment processing.", Alert.AlertType.ERROR);
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> 
                        showAlert("Connection Error", "Could not reach server: " + e.getMessage(), Alert.AlertType.ERROR));
                } finally {
                    // ALWAYS unlock the UI
                    Platform.runLater(() -> {
                        rootNode.setDisable(false);
                        rootNode.setCursor(Cursor.DEFAULT);
                    });
                }
            });

            updateThread.setDaemon(true);
            updateThread.start();
        });
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}