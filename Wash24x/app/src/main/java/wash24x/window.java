package wash24x;

import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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
    private BorderPane root;
    ListView<String> lastview = new ListView<>();
    String lastService = UserData.getLastOrderService();
    String currentUser = UserData.getCurrentUsername();
    String userProfile = UserData.getCurrentUserProfile();

    // Constructor หลักสำหรับการสร้างหน้าต่างหลักของแอปพลิเคชัน
    public window(Stage primaryStage) {
        // สร้าง GridPane สำหรับจัดวางเครื่องซัก
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        // วนลูปสร้างปุ่มสำหรับแต่ละเครื่องซัก
        for (int i = 0; i < NUM_MACHINES; i++) {
            final int machineIndex = i;
            MachineStatus status = machineManager.getStatus(machineIndex);

            // สร้างปุ่มและข้อความสถานะสำหรับแต่ละเครื่อง
            Button machineButton = new Button("Machine " + (i + 1) + " 🧺");
            Text statusText = new Text(getStatusText(status));
            updateButtonStyle(machineButton, status);

            // เก็บอ้างอิงถึง UI elements
            machineButtons[machineIndex] = machineButton;
            statusTexts[machineIndex] = statusText;

            // กำหนด event handler เมื่อกดปุ่มเครื่องซัก
            machineButton.setOnAction(event -> {
                // ตรวจสอบสถานะปัจจุบันของเครื่อง
                MachineStatus currentStatus = machineManager.getStatus(machineIndex);
                String orderOwner = UserData.getOrderOwner(machineIndex);
                String currentUser = UserData.getCurrentUsername();
                String currentUserRole = UserData.getUserRole(currentUser);

                // จัดการตามสถานะของเครื่อง
                if (currentStatus == MachineStatus.AVAILABLE) {
                    // สร้างออเดอร์ใหม่พร้อม callback สำหรับอัปเดตหน้าจอ
                    new WashOrder(primaryStage, machineIndex + 1, () -> {
                        // รับสถานะล่าสุดจาก manager (อาจมีการเปลี่ยนแปลงแล้ว)
                        MachineStatus updatedStatus = machineManager.getStatus(machineIndex);

                        // อัปเดต UI ตามสถานะปัจจุบัน
                        statusText.setText(getStatusText(updatedStatus));
                        updateButtonStyle(machineButton, updatedStatus);

                        // ถ้าเครื่องกำลังทำงาน ให้เริ่ม timeline สำหรับอัปเดต UI
                        if (updatedStatus == MachineStatus.WORKING) {
                            startMachineStatusUpdater(machineIndex);
                        }
                    });
                } else if (currentStatus == MachineStatus.WORKING) {
                    // แสดงรายละเอียดออเดอร์เฉพาะพนักงานหรือเจ้าของออเดอร์
                    if ("staff".equals(currentUserRole) ||
                            (orderOwner != null && orderOwner.equals(currentUser))) {
                        showOrderDetails(machineIndex);
                    } else {
                        showAccessDeniedAlert();
                    }
                } else if (currentStatus == MachineStatus.FINISH) {
                    // อนุญาตให้รับผ้าเฉพาะพนักงานหรือเจ้าของออเดอร์
                    if ("staff".equals(currentUserRole) ||
                            (orderOwner != null && orderOwner.equals(currentUser))) {
                        showPickupConfirmation(machineIndex);
                    } else {
                        showAccessDeniedAlert();
                    }
                } else {
                    // สำหรับสถานะอื่นๆ แสดงข้อความว่าไม่สามารถใช้เครื่องได้
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Machine Status");
                    alert.setHeaderText(null);
                    alert.setContentText("Machine " + (machineIndex + 1) + " is currently " +
                            currentStatus.toString().toLowerCase() + " and cannot be used.");
                    alert.show();
                }
            });

            // สร้าง VBox สำหรับจัดวางปุ่มและข้อความสถานะ
            VBox box = new VBox(5, machineButton, statusText);
            box.setAlignment(Pos.CENTER);
            grid.add(box, i % 3, i / 3);

            // เริ่มตัวอัปเดตสถานะสำหรับเครื่องที่กำลังทำงานอยู่แล้ว
            if (status == MachineStatus.WORKING || status == MachineStatus.FINISH) {
                startMachineStatusUpdater(machineIndex);
            }
        }

        // เริ่มการจำลองลูกค้า AI
        startAISimulation(primaryStage);

        // แสดงผู้ใช้ปัจจุบัน
        Label userLabel = new Label(userProfile + " Current User: " + currentUser);
        userLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        // สร้างปุ่มออกจากระบบ
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: rgb(219, 0, 40); -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            stopAllTimelines();
            new Login(primaryStage);
        });

        // สร้างตัวแปรสำหรับหน้าการชำระเงิน (ไม่ได้ใช้งานทั้งหมด - ควรตรวจสอบ)
        TextField textField = new TextField();
        ListView<String> listView = new ListView<>();
        Label label = new Label("ยอดเงินคงเหลือ: 0 บาท");

        // ปุ่มรีเซ็ต
        Button resetButton = new Button("รีเซ็ต");
        resetButton.setOnAction(e -> {
            textField.clear();
            listView.getItems().clear(); // ✅ ใช้ listView ที่ประกาศไว้
            label.setText("ยอดเงินคงเหลือ: 0 บาท");
        });

        // ปุ่มรายงานปัญหา
        Button reportIssueButton = new Button("Report Issue");
        reportIssueButton.setStyle("-fx-background-color: rgb(255, 165, 0); -fx-text-fill: white;");
        reportIssueButton.setOnAction(e -> showReportIssueDialog());

        // ปุ่มจัดการบัญชีผู้ใช้
        Button Accountbutton = new Button("👤Account");
        Accountbutton.setStyle("-fx-background-color: rgb(82, 137, 0) ; -fx-text-fill: white;");
        Accountbutton.setOnAction(e -> new UpdateAccount(primaryStage, currentUser));

        // ปุ่มสำหรับการทำธุรกรรม
        Button transactionButton = new Button("Transaction");
        transactionButton.setStyle("-fx-background-color: rgb(9, 164, 241) ; -fx-text-fill: white;");
        transactionButton.setOnAction(e -> new TopUp(primaryStage));

        // ปุ่มดูปัญหาที่รายงาน (เฉพาะพนักงาน)
        Button backviewissisButton = new Button("View issue");
        backviewissisButton.setOnAction(e -> {
            // ตรวจสอบว่าผู้ใช้เป็นพนักงานหรือไม่
            String currentUser = UserData.getCurrentUsername();
            String currentUserRole = UserData.getUserRole(currentUser);
            if ("staff".equals(currentUserRole)) {
                // ถ้าเป็น staff ให้ไปหน้า StaffDashboard
                new StaffDashboard(primaryStage);
            } else {
                // ถ้าไม่ใช่ staff ให้แสดง Alert แจ้งเตือน
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Access Denied");
                alert.setHeaderText(null);
                alert.setContentText("Access Denied: Only staff can view issues.");
                alert.show();
            }
        });

        // จัดวางปุ่มต่างๆ ในแนวนอน
        HBox buttonBox = new HBox(10, backviewissisButton, reportIssueButton, Accountbutton, transactionButton,
                logoutButton);
        buttonBox.setAlignment(Pos.CENTER);

        // สร้าง BorderPane หลักและกำหนดค่าต่างๆ
        this.root = new BorderPane();
        this.root.setCenter(new VBox(10, userLabel, grid, buttonBox));
        root.setPadding(new Insets(20));
        root.setStyle(
                "-fx-background-color:rgb(1, 128, 255); -fx-border-color:rgb(0, 0, 0); -fx-border-radius: 10px; -fx-padding: 20px;");

        // สร้าง Scene และแสดงหน้าต่าง
        Scene scene = new Scene(root, 550, 550);
        primaryStage.setTitle("🎛️ Status Machine 🐦‍🔥");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    // แสดง Alert เมื่อผู้ใช้ไม่มีสิทธิ์เข้าถึง
    private void showAccessDeniedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Access Denied");
        alert.setHeaderText(null);
        alert.setContentText(
                "You don't have permission to access this machine's orders. Only staff or the customer who placed the order can access it.");
        alert.show();
    }

    // แสดงกล่องยืนยันการรับผ้า
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

        // จัดการการตอบสนองเมื่อกดปุ่มยืนยัน
        alert.showAndWait().ifPresent(response -> {
            if (response == pickupButton) {
                // รีเซ็ตสถานะเครื่องเป็น AVAILABLE
                machineManager.setStatus(machineIndex, MachineStatus.AVAILABLE);

                // อัปเดต UI
                Text statusText = statusTexts[machineIndex];
                Button button = machineButtons[machineIndex];
                statusText.setText(getStatusText(MachineStatus.AVAILABLE));
                updateButtonStyle(button, MachineStatus.AVAILABLE);

                // ล้างข้อมูลออเดอร์
                UserData.clearLastOrder();
                UserData.removeOrderOwner(machineIndex);

                // แสดงข้อความยืนยัน
                Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION);
                confirmAlert.setTitle("Pickup Confirmed");
                confirmAlert.setHeaderText(null);
                confirmAlert.setContentText("Thank you for using our service!");
                confirmAlert.show();
            }
        });
    }

    // แสดงรายละเอียดออเดอร์
    private void showOrderDetails(int machineIndex) {
        String lastService = UserData.getLastOrderService();
        int remainingTime = UserData.getRemainingTime();
        String orderOwner = UserData.getOrderOwner(machineIndex);

        MachineStatus status = machineManager.getStatus(machineIndex);
        String statusMessage = (status == MachineStatus.FINISH) ? "Ready for pickup"
                : "Currently working - " + remainingTime + " seconds remaining";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Details");
        alert.setHeaderText("Machine " + (machineIndex + 1));

        String contentText = "Customer: " + userProfile + (orderOwner != null ? orderOwner : "Unknown") + "\n" +
                "Service: " + (lastService != null ? lastService : "Unknown") + "\n" +
                "Status: " + statusMessage;

        alert.setContentText(contentText);
        alert.show();
    }

    // ยกเลิกออเดอร์ (ไม่ได้ใช้งานในโค้ดปัจจุบัน)
    @SuppressWarnings("unused")
    private void cancelOrder(int machineIndex) {
        // บันทึกสถานะออเดอร์ก่อนยกเลิก
        UserData.setLastCanceledOrder(machineIndex, UserData.getLastOrderService());

        // รีเซ็ตสถานะเครื่อง
        machineManager.setStatus(machineIndex, MachineStatus.AVAILABLE);
        UserData.removeOrderOwner(machineIndex);

        // อัปเดต UI
        statusTexts[machineIndex].setText(getStatusText(MachineStatus.AVAILABLE));
        updateButtonStyle(machineButtons[machineIndex], MachineStatus.AVAILABLE);

        // แจ้งเตือน
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Canceled");
        alert.setHeaderText(null);
        alert.setContentText("Your order has been canceled. You can restore it later.");
        alert.show();
    }

    // แสดงหน้าต่างรายงานปัญหา
    private void showReportIssueDialog() {
        Stage dialogStage = new Stage();
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20px; -fx-background-color: white;");

        Label instructionLabel = new Label("Please describe the issue:");
        TextArea issueTextArea = new TextArea();
        issueTextArea.setPromptText("Describe the problem...");

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String issueText = issueTextArea.getText();
            if (!issueText.trim().isEmpty()) {
                // เพิ่มรายละเอียดเพิ่มเติม เช่น เวลาและผู้ใช้งาน (ปรับตามความต้องการ)
                String timestamp = java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String reportEntry = UserData.getCurrentUsername() + " (" + timestamp + "): " + issueText;
                UserData.addReport(reportEntry);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your issue has been reported!", ButtonType.OK);
                alert.show();
                dialogStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a valid issue.", ButtonType.OK);
                alert.show();
            }
        });

        layout.getChildren().addAll(instructionLabel, issueTextArea, submitButton);

        Scene scene = new Scene(layout, 400, 250);
        dialogStage.setScene(scene);
        dialogStage.setTitle("Report an Issue");
        dialogStage.show();
    }

    // คืนค่าออเดอร์ล่าสุดที่ถูกยกเลิก
    private void restoreLastOrder(int machineIndex) {
        String lastService = UserData.getLastCanceledOrder(machineIndex);
        if (lastService != null) {
            UserData.setLastOrderService(lastService);
            machineManager.setStatus(machineIndex, MachineStatus.WORKING);

            statusTexts[machineIndex].setText(getStatusText(MachineStatus.WORKING));
            updateButtonStyle(machineButtons[machineIndex], MachineStatus.WORKING);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Restored");
            alert.setHeaderText(null);
            alert.setContentText("Your last order (" + lastService + ") has been restored.");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Order to Restore");
            alert.setHeaderText(null);
            alert.setContentText("No previous order found.");
            alert.show();
        }
    }

    // เริ่ม Timeline สำหรับอัปเดตสถานะเครื่อง
    private void startMachineStatusUpdater(int machineIndex) {
        // หยุด timeline ก่อนหน้าถ้ามี
        if (machineTimelines[machineIndex] != null) {
            machineTimelines[machineIndex].stop();
        }

        // สร้าง timeline ใหม่เพื่อตรวจสอบและอัปเดตสถานะเครื่องเป็นระยะ
        machineTimelines[machineIndex] = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            MachineStatus currentStatus = machineManager.getStatus(machineIndex);
            Button button = machineButtons[machineIndex];
            Text statusText = statusTexts[machineIndex];

            // อัปเดต UI elements
            statusText.setText(getStatusText(currentStatus));
            updateButtonStyle(button, currentStatus);

            // ใช้เอฟเฟกต์ภาพที่แตกต่างกันตามสถานะ
            if (currentStatus == MachineStatus.WORKING) {
                // เอฟเฟกต์กะพริบสำหรับเครื่องที่กำลังทำงาน
                if (System.currentTimeMillis() / 500 % 2 == 0) {
                    button.setStyle("-fx-background-color: " + MachineColor.WORKING_COLOR + "; -fx-text-fill: black;");
                } else {
                    button.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                }
            } else if (currentStatus == MachineStatus.FINISH) {
                // เอฟเฟกต์เต้นสำหรับเครื่องที่ทำงานเสร็จแล้ว
                if (System.currentTimeMillis() / 800 % 2 == 0) {
                    button.setStyle(
                            "-fx-background-color: " + MachineColor.FINALORDER_COLOR + "; -fx-text-fill: black;");
                } else {
                    button.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
                }

                // ถ้านี่เป็นออเดอร์ AI ที่เสร็จแล้ว ให้จัดการเวลารับผ้าของ AI
                String orderOwner = UserData.getOrderOwner(machineIndex);
                if (orderOwner != null && orderOwner.startsWith("AI-")) {
                    if (aiPickupTimers[machineIndex] == 0) {
                        // กำหนดตัวจับเวลาสุ่มสำหรับการรับผ้าระหว่าง 10-15 วินาที
                        aiPickupTimers[machineIndex] = 10 + random.nextInt(6);
                    } else {
                        aiPickupTimers[machineIndex]--;
                        if (aiPickupTimers[machineIndex] <= 0) {
                            // ผู้ใช้ AI รับผ้า
                            machineManager.setStatus(machineIndex, MachineStatus.AVAILABLE);
                            UserData.removeOrderOwner(machineIndex);
                            aiPickupTimers[machineIndex] = 0;
                        }
                    }
                }
            }

            // หยุด timeline ถ้าเครื่องไม่อยู่ในสถานะที่เราต้องการติดตาม
            if (currentStatus != MachineStatus.WORKING && currentStatus != MachineStatus.FINISH) {
                machineTimelines[machineIndex].stop();
                machineTimelines[machineIndex] = null;
            }
        }));

        machineTimelines[machineIndex].setCycleCount(Animation.INDEFINITE);
        machineTimelines[machineIndex].play();
    }

    // เริ่มการจำลองลูกค้า AI
    private void startAISimulation(Stage primaryStage) {
        // หยุดการจำลองที่มีอยู่ถ้ากำลังทำงาน
        if (aiSimulationTimeline != null) {
            aiSimulationTimeline.stop();
        }

        aiSimulationTimeline = new Timeline(new KeyFrame(Duration.minutes(10), e -> {
            // ตรวจสอบเครื่องที่พร้อมใช้งาน
            int availableMachineCount = 0;
            for (int i = 0; i < NUM_MACHINES; i++) {
                if (machineManager.getStatus(i) == MachineStatus.AVAILABLE) {
                    availableMachineCount++;
                }
            }

            // โอกาส 30% ที่ลูกค้า AI จะปรากฏหากมีเครื่องพร้อมใช้งาน
            if (availableMachineCount > 0 && random.nextDouble() < 0.3) {
                // หาเครื่องที่พร้อมใช้งานสุ่มหนึ่งเครื่อง
                int[] availableMachines = new int[availableMachineCount];
                int index = 0;
                for (int i = 0; i < NUM_MACHINES; i++) {
                    if (machineManager.getStatus(i) == MachineStatus.AVAILABLE) {
                        availableMachines[index++] = i;
                    }
                }

                // เลือกเครื่องที่พร้อมใช้งานสุ่มหนึ่งเครื่อง
                int selectedMachineIndex = availableMachines[random.nextInt(availableMachineCount)];

                // เลือกลูกค้า AI และบริการสุ่ม
                String aiName = AI_NAMES[random.nextInt(AI_NAMES.length)];
                String aiService = AI_SERVICES[random.nextInt(AI_SERVICES.length)];

                // ตั้งค่าสถานะเครื่องเป็น WORKING
                machineManager.setStatus(selectedMachineIndex, MachineStatus.WORKING);

                // ลงทะเบียน AI เป็นเจ้าของออเดอร์นี้
                UserData.setOrderOwner(selectedMachineIndex, aiName);

                // ตั้งค่าประเภทบริการสำหรับการแสดงสถานะ
                UserData.setLastOrderService(aiService);

                // คำนวณระยะเวลาบริการ (ใช้เวลาสั้นกว่าสำหรับ AI เพื่อให้การจำลองมีพลวัตมากขึ้น)
                int duration = 30 + random.nextInt(60); // 30-90 วินาทีสำหรับลูกค้า AI

                // เริ่มนับถอยหลังสำหรับเครื่องนี้
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

    // เริ่มนับถอยหลังสำหรับเครื่องของ AI
    private void startAIMachineCountdown(int machineIndex, int durationInSeconds) {
        Timeline aiWashingTimeline = new Timeline(new KeyFrame(Duration.minutes(1), e -> {
            // ลดตัวนับเวลาที่เหลือ (นี่เป็นเพียงเพื่อการแสดงผล)
            int remainingTime = UserData.getRemainingTime();
            if (remainingTime > 0) {
                UserData.setRemainingTime(remainingTime - 1);
            }
        }));

        aiWashingTimeline.setCycleCount(durationInSeconds);
        aiWashingTimeline.setOnFinished(event -> {
            // ตั้งค่าสถานะเครื่องเป็น FINISH เมื่อการซักเสร็จสิ้น
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
    // รับข้อความสถานะตามสถานะเครื่อง

    private String getStatusText(MachineStatus status) {
        return switch (status) {
            case AVAILABLE -> MachineColor.AVAILABLE_ICON;
            case WORKING -> MachineColor.WORKING_ICON;
            case IN_USE -> MachineColor.IN_USE_ICON;
            case FINISH -> MachineColor.FINALORDER_ICON;
            default -> "Status: " + status.toString();
        };
    }

    // อัปเดตสไตล์ปุ่มตามสถานะเครื่อง
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

    // หยุดทุก Timeline
    private void stopAllTimelines() {
        // หยุด Timeline การจำลอง AI
        if (aiSimulationTimeline != null) {
            aiSimulationTimeline.stop();
        }

        // หยุดทุก Timeline ของเครื่อง
        for (int i = 0; i < NUM_MACHINES; i++) {
            if (machineTimelines[i] != null) {
                machineTimelines[i].stop();
            }
        }
    }

    // รับ root node สำหรับใช้ใน Scene
    public Parent getRoot() {
        return root;
    }

}