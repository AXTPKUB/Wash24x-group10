package wash24x;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;

public class WashOrder {

    private final Runnable onComplete;
    private final int machineIndex;
    private final Stage primaryStage;
    private final MachineManager machineManager = MachineManager.getInstance();

    private final HashMap<String, Integer> servicePrices = new HashMap<>() {
        {
            put("Regular Wash", 50);
            put("Dry Clean", 100);
            put("Ironing", 80);
            put("Deluxe Wash", 150);
            put("Drying", 70);
        }
    };
    private final HashMap<String, Integer> serviceDurations = new HashMap<>() {
        {
            put("Regular Wash", 120);
            put("Dry Clean", 180);
            put("Ironing", 90);
            put("Deluxe Wash", 240);
            put("Drying", 150);
        }
    };

    private Timeline timeline;
    private Label timerLabel;
    private String selectedService = "None";
    private boolean washingStarted = false;

    public WashOrder(Stage primaryStage, int machineIndex, Runnable onComplete) {
        this.primaryStage = primaryStage;
        this.machineIndex = machineIndex - 1;
        this.onComplete = onComplete;

        primaryStage.setTitle("🎛️ Stasus Machine 🐦‍🔥");

        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle(
                "-fx-background-color:rgb(1, 128, 255); -fx-border-color:rgb(0, 0, 0); -fx-border-radius: 10px; -fx-padding: 30px;");

        Label nameLabel = new Label("Customer Name: " + UserData.getCurrentUsername());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");

        Label balanceLabel = new Label();
        balanceLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        updateBalanceAndPrice(balanceLabel, null, null);

        Button historyButton = new Button("View Order History");
        styleButton(historyButton);
        historyButton.setOnAction(e -> showOrderHistory());

        Label typeLabel = new Label("Service Type:");
        typeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        ComboBox<String> serviceTypeComboBox = new ComboBox<>();
        serviceTypeComboBox.getItems().addAll("None", "Regular Wash", "Dry Clean", "Ironing", "Deluxe Wash", "Drying");
        serviceTypeComboBox.setValue("None");
        serviceTypeComboBox.setStyle("-fx-background-color: white; -fx-border-radius: 5px;");

        Label priceLabel = new Label("Total Price: 0 THB");
        priceLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        serviceTypeComboBox.setOnAction(e -> {
            selectedService = serviceTypeComboBox.getValue();
            updateBalanceAndPrice(balanceLabel, priceLabel, serviceTypeComboBox);
        });

        timerLabel = new Label("🕒 Remaining time: 0 sec");
        timerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Button orderButton = new Button("Place Order");
        styleButton(orderButton, "#2ecc71");
        orderButton.setOnAction(e -> {
            if ("None".equals(selectedService)) {
                showAlert("Error", "Please select a service type", Alert.AlertType.ERROR);
                return;
            }
            
            int price = servicePrices.getOrDefault(selectedService, 0);
            int duration = serviceDurations.getOrDefault(selectedService, 120);
            
            if (UserData.getCurrentBalance() < price) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Insufficient Balance! Do you want to top-up?",
                        ButtonType.YES, ButtonType.NO);
                alert.setTitle("Insufficient Balance");
                alert.setHeaderText(null);
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        new TopUp(primaryStage);
                        UserData.setLastOrderService(selectedService);
                    }
                });
            } else {
                if (UserData.deductBalanceWithHistory(price, selectedService)) {
                    updateBalanceAndPrice(balanceLabel, priceLabel, serviceTypeComboBox);
                    
                    // Update machine status to WORKING immediately
                    machineManager.setStatus(machineIndex, MachineStatus.WORKING);
                    
                    // Save the order owner's username
                    UserData.setOrderOwner(machineIndex, UserData.getCurrentUsername());
                    
                    // Execute the onComplete callback to update the UI
                    onComplete.run();
                    
                    // Start washing process with the correct duration
                    startWashingProcess(duration);
                    
                    // Disable the order button once washing has started
                    orderButton.setDisable(true);
                    serviceTypeComboBox.setDisable(true);
                    washingStarted = true;
                    
                    // Show a confirmation
                    showAlert("Order Placed", "Your " + selectedService + " order has started!", Alert.AlertType.INFORMATION);
                    UserData.setLastOrderService(selectedService);
                }
            }
        });
        
        Button cancelButton = new Button("Cancel");
        styleButton(cancelButton, "#e74c3c");
        cancelButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel the order?",
                    ButtonType.YES, ButtonType.NO);
            alert.setTitle("Cancel Order");
            alert.setHeaderText(null);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    if (timeline != null) {
                        timeline.stop();
                        UserData.setLastOrderService(selectedService);
                    }
                    
                    machineManager.setStatus(machineIndex, MachineStatus.AVAILABLE);
                    UserData.clearLastOrder();
                    UserData.removeOrderOwner(machineIndex);
                    
                    new window(primaryStage);

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION,
                            "Your order has been canceled successfully.");
                    successAlert.setTitle("Order Canceled");
                    successAlert.setHeaderText(null);
                    successAlert.show();
                    UserData.setLastOrderService(selectedService);
                }
            });
        });

        Button backButton = new Button("  ↩️ Back  ");
        styleButton(backButton, "#95a5a6");
        backButton.setOnAction(e -> new window(primaryStage));

        HBox buttonBox = new HBox(10, orderButton, cancelButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        mainLayout.getChildren().addAll(nameLabel, balanceLabel, historyButton, typeLabel, serviceTypeComboBox,
                priceLabel, timerLabel, buttonBox);

        Scene scene = new Scene(mainLayout, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void styleButton(Button button) {
        styleButton(button, "#3498db");
    }

    private void styleButton(Button button, String color) {
        button.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-padding: 5px 15px;");
    }

    private void updateBalanceAndPrice(Label balanceLabel, Label priceLabel, ComboBox<String> serviceTypeComboBox) {
        balanceLabel.setText("Balance: " + UserData.getCurrentBalance() + " THB");
        if (serviceTypeComboBox != null && priceLabel != null) {
            int price = servicePrices.getOrDefault(serviceTypeComboBox.getValue(), 0);
            priceLabel.setText(String.format("Total Price: %d THB", price));
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showOrderHistory() {
        Stage historyStage = new Stage();
        historyStage.setTitle("Order History");

        List<String> history = UserData.getOrderHistory();
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setStyle("-fx-padding: 20; -fx-background-color: #f0f0f0;");

        Label titleLabel = new Label("Order History for " + UserData.getCurrentUsername());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");
        grid.add(titleLabel, 0, 0);

        if (history.isEmpty()) {
            Label noOrderLabel = new Label("No orders yet!");
            noOrderLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
            grid.add(noOrderLabel, 0, 1);
            UserData.setLastOrderService(selectedService);
        } else {
            for (int i = 0; i < history.size(); i++) {
                String record = history.get(i).replaceAll("\\[(\\d{4}-\\d{2}-\\d{2}).*?\\]", "[$1]");
                String shortRecord = (record.length() > 50) ? record.substring(0, 50) + "..." : record;

                TextFlow textFlow = new TextFlow();
                textFlow.setMaxWidth(400);

                Text text = new Text(shortRecord);
                text.setStyle("-fx-fill: #333;");

                textFlow.getChildren().add(text);
                textFlow.setStyle(
                        "-fx-background-color: #fff; -fx-padding: 5 10; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");

                Tooltip tooltip = new Tooltip(record);
                Tooltip.install(textFlow, tooltip);

                grid.add(textFlow, 0, i + 1);
                UserData.setLastOrderService(selectedService);
            }
        }

        Button closeButton = new Button("Close");
        closeButton.setStyle(
                "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
        closeButton.setOnAction(e -> historyStage.close());
        grid.add(closeButton, 0, history.size() + 2);

        GridPane.setMargin(closeButton, new javafx.geometry.Insets(10, 0, 0, 0));

        Scene scene = new Scene(grid, 400, 400);
        historyStage.setScene(scene);
        historyStage.initOwner(primaryStage);
        historyStage.show();
    }
    
    private void startWashingProcess(int durationInSeconds) {
        if (timeline != null) {
            timeline.stop();
        }
        
        // Initialize timer with the correct duration from service
        final int[] timeLeft = {durationInSeconds};
        
        // Update the label initially
        timerLabel.setText("🕒 Remaining time: " + timeLeft[0] + " sec");
        
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft[0]--;
            
            // Update the UI with each tick
            timerLabel.setText("🕒 Remaining time: " + timeLeft[0] + " sec");
            UserData.setRemainingTime(timeLeft[0]);
            
            if (timeLeft[0] <= 0) {
                timeline.stop();
                timerLabel.setText("✅ Washing Complete!");
                
                // Update machine status to FINISH
                machineManager.setStatus(machineIndex, MachineStatus.FINISH);
                
                // Show completion alert
                showAlert("Washing Complete", "Your clothes are ready for pickup!", Alert.AlertType.INFORMATION);
                
                // Execute onComplete callback
                onComplete.run();
                
                // Return to main window
                new window(primaryStage);
            }
        }));
        
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    
}