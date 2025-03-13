package wash24x;

import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class window {

    // AI customer simulation
    private static final String[] AI_NAMES = { "JAMPEE", "ball245", "aeicat", "Atipatno.1", "TAOX" };
    private static final String[] AI_SERVICES = { "Regular Wash", "Dry Clean", "Ironing", "Deluxe Wash", "Drying" };
    private Timeline aiSimulationTimeline;
    private Random random = new Random();
    private static final int NUM_MACHINES = 12;
    private final MachineManager machineManager = MachineManager.getInstance();
    private Timeline[] machineTimelines = new Timeline[NUM_MACHINES];
    private Button[] machineButtons = new Button[NUM_MACHINES];
    private Text[] statusTexts = new Text[NUM_MACHINES];
    private int[] aiPickupTimers = new int[NUM_MACHINES];
    String lastService = UserData.getLastOrderService();

    public window(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < NUM_MACHINES; i++) {
            final int machineIndex = i;
            MachineStatus status = machineManager.getStatus(machineIndex);

            Button machineButton = new Button("Machine " + (i + 1) + " 🧺");
            Text statusText = new Text(getStatusText(status));
            updateButtonStyle(machineButton, status);

            // Store references to the UI elements
            machineButtons[machineIndex] = machineButton;
            statusTexts[machineIndex] = statusText;

            machineButton.setOnAction(event -> {
                MachineStatus currentStatus = machineManager.getStatus(machineIndex);
                String orderOwner = UserData.getOrderOwner(machineIndex);
                String currentUser = UserData.getCurrentUsername();
                String currentUserRole = UserData.getUserRole(currentUser);

                if (currentStatus == MachineStatus.AVAILABLE) {
                    // Create WashOrder with a callback that updates the machine's status display
                    new WashOrder(primaryStage, machineIndex + 1, () -> {
                        // Get the CURRENT status from the manager (it might have changed)
                        MachineStatus updatedStatus = machineManager.getStatus(machineIndex);

                        // Update the UI based on the current status
                        statusText.setText(getStatusText(updatedStatus));
                        updateButtonStyle(machineButton, updatedStatus);

                        // If the machine is working, start a timeline to keep the UI updated
                        if (updatedStatus == MachineStatus.WORKING) {
                            startMachineStatusUpdater(machineIndex);
                        }
                    });
                } else if (currentStatus == MachineStatus.WORKING) {
                    // Only show order details to staff or the order owner
                    if ("staff".equals(currentUserRole)  || 
                        (orderOwner != null && orderOwner.equals(currentUser))) {
                        showOrderDetails(machineIndex);
                    } else {
                        showAccessDeniedAlert();
                    }
                } else if (currentStatus == MachineStatus.FINISH) {
                    // Only allow pickup for staff or the order owner
                    if ("staff".equals(currentUserRole)  || 
                        (orderOwner != null && orderOwner.equals(currentUser))) {
                        showPickupConfirmation(machineIndex);
                    } else {
                        showAccessDeniedAlert();
                    }
                } else {
                    // For other statuses, show a message that the machine cannot be used
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Machine Status");
                    alert.setHeaderText(null);
                    alert.setContentText("Machine " + (machineIndex + 1) + " is currently " +
                            currentStatus.toString().toLowerCase() + " and cannot be used.");
                    alert.show();
                }
            });

            VBox box = new VBox(5, machineButton, statusText);
            box.setAlignment(Pos.CENTER);
            grid.add(box, i % 3, i / 3);

            // Start status updater for any already working machines
            if (status == MachineStatus.WORKING || status == MachineStatus.FINISH) {
                startMachineStatusUpdater(machineIndex);
            }
        }

        // Start AI customer simulation
        startAISimulation(primaryStage);

        // Display current user
        Label userLabel = new Label("Current User: " + UserData.getCurrentUsername());
        userLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: rgb(219, 0, 40); -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            stopAllTimelines();
            new Login(primaryStage);

            
        });

        
        Button Accountbutton = new Button("👤Account");
        Accountbutton.setStyle("-fx-background-color: rgb(155, 249, 13) ; -fx-text-fill: white;");
        Accountbutton.setOnAction(e -> new UpdateAccount(primaryStage, lastService));

        Button transactionButton = new Button("Transaction");
        transactionButton.setStyle("-fx-background-color: rgb(9, 164, 241) ; -fx-text-fill: white;");
        transactionButton.setOnAction(e -> new TopUp(primaryStage));

        HBox buttonBox = new HBox(10, Accountbutton , transactionButton, logoutButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, userLabel, grid, buttonBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle(
                "-fx-background-color:rgb(1, 128, 255); -fx-border-color:rgb(0, 0, 0); -fx-border-radius: 10px; -fx-padding: 20px;");

        Scene scene = new Scene(root, 550, 550);
        primaryStage.setTitle("🎛️ Status Machine 🐦‍🔥");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void showAccessDeniedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Access Denied");
        alert.setHeaderText(null);
        alert.setContentText("You don't have permission to access this machine's orders. Only staff or the customer who placed the order can access it.");
        alert.show();
    }

    // Method to show pickup confirmation dialog
    private void showPickupConfirmation(int machineIndex) {
        String lastService = UserData.getLastOrderService();
        String orderOwner = UserData.getOrderOwner(machineIndex);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Laundry Ready");
        alert.setHeaderText("Machine " + (machineIndex + 1) + " - Order Complete");
        alert.setContentText("Customer: " + (orderOwner != null ? orderOwner : "Unknown") + "\n" +
                "Service: " + (lastService != null ? lastService : "laundry") + "\n" +
                "Ready for pickup. Confirm pickup?");

        ButtonType pickupButton = new ButtonType("Pickup");
        ButtonType cancelButton = ButtonType.CANCEL;

        alert.getButtonTypes().setAll(pickupButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == pickupButton) {
                // Reset machine to AVAILABLE status
                machineManager.setStatus(machineIndex, MachineStatus.AVAILABLE);

                // Update UI
                Text statusText = statusTexts[machineIndex];
                Button button = machineButtons[machineIndex];
                statusText.setText(getStatusText(MachineStatus.AVAILABLE));
                updateButtonStyle(button, MachineStatus.AVAILABLE);

                // Clear the order data
                UserData.clearLastOrder();
                UserData.removeOrderOwner(machineIndex);

                // Show confirmation message
                Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION);
                confirmAlert.setTitle("Pickup Confirmed");
                confirmAlert.setHeaderText(null);
                confirmAlert.setContentText("Thank you for using our service!");
                confirmAlert.show();
            }
        });
    }

    private void showOrderDetails(int machineIndex) {
        String lastService = UserData.getLastOrderService();
        int remainingTime = UserData.getRemainingTime();
        String orderOwner = UserData.getOrderOwner(machineIndex);
        
        MachineStatus status = machineManager.getStatus(machineIndex);
        String statusMessage = (status == MachineStatus.FINISH) ? 
                "Ready for pickup" : "Currently working - " + remainingTime + " seconds remaining";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Details");
        alert.setHeaderText("Machine " + (machineIndex + 1));

        String contentText = "Customer: " + (orderOwner != null ? orderOwner : "Unknown") + "\n" +
                "Service: " + (lastService != null ? lastService : "Unknown") + "\n" +
                "Status: " + statusMessage;

        alert.setContentText(contentText);
        alert.show();
    }

    private void startMachineStatusUpdater(int machineIndex) {
        // Stop previous timeline if it exists
        if (machineTimelines[machineIndex] != null) {
            machineTimelines[machineIndex].stop();
        }

        // Create new timeline to periodically check and update machine status
        machineTimelines[machineIndex] = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            MachineStatus currentStatus = machineManager.getStatus(machineIndex);
            Button button = machineButtons[machineIndex];
            Text statusText = statusTexts[machineIndex];

            // Update UI elements
            statusText.setText(getStatusText(currentStatus));
            updateButtonStyle(button, currentStatus);

            // Apply different visual effects based on status
            if (currentStatus == MachineStatus.WORKING) {
                // Blinking effect for working machines
                if (System.currentTimeMillis() / 500 % 2 == 0) {
                    button.setStyle("-fx-background-color: " + MachineColor.WORKING_COLOR + "; -fx-text-fill: black;");
                } else {
                    button.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                }
            } else if (currentStatus == MachineStatus.FINISH) {
                // Pulse effect for machines with completed orders
                if (System.currentTimeMillis() / 800 % 2 == 0) {
                    button.setStyle(
                            "-fx-background-color: " + MachineColor.FINALORDER_COLOR + "; -fx-text-fill: black;");
                } else {
                    button.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
                }
                
                // If this is an AI order that's finished, handle AI pickup timing
                String orderOwner = UserData.getOrderOwner(machineIndex);
                if (orderOwner != null && orderOwner.startsWith("AI-")) {
                    if (aiPickupTimers[machineIndex] == 0) {
                        // Initialize random pickup timer between 10-15 seconds
                        aiPickupTimers[machineIndex] = 10 + random.nextInt(6);
                    } else {
                        aiPickupTimers[machineIndex]--;
                        if (aiPickupTimers[machineIndex] <= 0) {
                            // AI user picks up their laundry
                            machineManager.setStatus(machineIndex, MachineStatus.AVAILABLE);
                            UserData.removeOrderOwner(machineIndex);
                            aiPickupTimers[machineIndex] = 0;
                        }
                    }
                }
            }

            // Stop timeline if machine is no longer in a state we need to monitor
            if (currentStatus != MachineStatus.WORKING && currentStatus != MachineStatus.FINISH) {
                machineTimelines[machineIndex].stop();
                machineTimelines[machineIndex] = null;
            }
        }));

        machineTimelines[machineIndex].setCycleCount(Animation.INDEFINITE);
        machineTimelines[machineIndex].play();
    }

    private void startAISimulation(Stage primaryStage) {
        // Stop existing simulation if running
        if (aiSimulationTimeline != null) {
            aiSimulationTimeline.stop();
        }
        
        aiSimulationTimeline = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            // Check for available machines
            int availableMachineCount = 0;
            for (int i = 0; i < NUM_MACHINES; i++) {
                if (machineManager.getStatus(i) == MachineStatus.AVAILABLE) {
                    availableMachineCount++;
                }
            }
            
            // 30% chance of AI customer appearing if machines are available
            if (availableMachineCount > 0 && random.nextDouble() < 0.3) {
                // Find a random available machine
                int[] availableMachines = new int[availableMachineCount];
                int index = 0;
                for (int i = 0; i < NUM_MACHINES; i++) {
                    if (machineManager.getStatus(i) == MachineStatus.AVAILABLE) {
                        availableMachines[index++] = i;
                    }
                }
                
                // Select a random available machine
                int selectedMachineIndex = availableMachines[random.nextInt(availableMachineCount)];
                
                // Select random AI customer and service
                String aiName = AI_NAMES[random.nextInt(AI_NAMES.length)];
                String aiService = AI_SERVICES[random.nextInt(AI_SERVICES.length)];
                
                // Set machine to WORKING status
                machineManager.setStatus(selectedMachineIndex, MachineStatus.WORKING);
                
                // Register AI as owner of this order
                UserData.setOrderOwner(selectedMachineIndex, aiName);
                
                // Set service type for status displays
                UserData.setLastOrderService(aiService);
                
                // Calculate service duration (using shorter times for AI to make simulation more dynamic)
                int duration = 30 + random.nextInt(60); // 30-90 seconds for AI customers
                
                // Start countdown for this machine
                startAIMachineCountdown(selectedMachineIndex, duration);
                
                // Update UI
                Text statusText = statusTexts[selectedMachineIndex];
                Button button = machineButtons[selectedMachineIndex];
                if (statusText != null && button != null) {
                    statusText.setText(getStatusText(MachineStatus.WORKING));
                    updateButtonStyle(button, MachineStatus.WORKING);
                    startMachineStatusUpdater(selectedMachineIndex);
                }
            }
        }));
        
        aiSimulationTimeline.setCycleCount(Animation.INDEFINITE);
        aiSimulationTimeline.play();
    }
    
    private void startAIMachineCountdown(int machineIndex, int durationInSeconds) {
        Timeline aiWashingTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            // Decrement remaining time counter (this is just for display purposes)
            int remainingTime = UserData.getRemainingTime();
            if (remainingTime > 0) {
                UserData.setRemainingTime(remainingTime - 1);
            }
        }));
        
        aiWashingTimeline.setCycleCount(durationInSeconds);
        aiWashingTimeline.setOnFinished(event -> {
            // Set machine to FINISH status when washing completes
            machineManager.setStatus(machineIndex, MachineStatus.FINISH);
            
            // Update UI
            Text statusText = statusTexts[machineIndex];
            Button button = machineButtons[machineIndex];
            if (statusText != null && button != null) {
                statusText.setText(getStatusText(MachineStatus.FINISH));
                updateButtonStyle(button, MachineStatus.FINISH);
            }
        });
        
        aiWashingTimeline.play();
    }

    private String getStatusText(MachineStatus status) {
        return switch (status) {
            case AVAILABLE -> MachineColor.AVAILABLE_ICON;
            case WORKING -> MachineColor.WORKING_ICON;
            case IN_USE -> MachineColor.IN_USE_ICON;
            case FINISH -> MachineColor.FINALORDER_ICON;
            default -> "Status: " + status.toString();
        };
    }

    private void updateButtonStyle(Button button, MachineStatus status) {
        switch (status) {
            case AVAILABLE ->
                button.setStyle("-fx-background-color: " + MachineColor.AVAILABLE_COLOR + "; -fx-text-fill: black;");
            case WORKING ->
                button.setStyle("-fx-background-color: " + MachineColor.WORKING_COLOR + "; -fx-text-fill: black;");
            case IN_USE ->
                button.setStyle("-fx-background-color: " + MachineColor.IN_USE_COLOR + "; -fx-text-fill: black;");
            case FINISH ->
                button.setStyle("-fx-background-color: " + MachineColor.FINALORDER_COLOR + "; -fx-text-fill: black;");
        }
    }

    private void stopAllTimelines() {
        // Stop the AI simulation timeline
        if (aiSimulationTimeline != null) {
            aiSimulationTimeline.stop();
        }
        
        // Stop all machine timelines
        for (int i = 0; i < NUM_MACHINES; i++) {
            if (machineTimelines[i] != null) {
                machineTimelines[i].stop();
            }
        }
    }
}