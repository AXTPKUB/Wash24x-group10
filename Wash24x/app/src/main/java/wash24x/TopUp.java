package wash24x;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TopUp {
    public TopUp(Stage primaryStage) {
        // สร้าง GridPane เพื่อจัดวาง UI
        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));

        // หัวข้อหลักของหน้าจอเติมเงิน
        Label titleLabel = new Label("💲 Top-Up System");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ช่องกรอกจำนวนเงิน
        Label amountLabel = new Label("Amount: ");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");

        // เลือกธนาคารสำหรับทำรายการ
        Label bankLabel = new Label("Bank: ");
        ComboBox<String> bankComboBox = new ComboBox<>();
        bankComboBox.getItems().addAll("Bank A", "Bank B", "Bank C", "Bank D");
        bankComboBox.setValue("Bank A"); // กำหนดค่าเริ่มต้นเป็น "Bank A"

        // ช่องกรอกรหัส PIN
        Label pinLabel = new Label("Enter PIN: ");
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("****");

        // ปุ่มยืนยันการเติมเงิน
        Button topUpButton = new Button("✔ Confirm Top-Up");
        topUpButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");

        // ปุ่มย้อนกลับไปยังหน้าหลัก
        Button backButton = new Button("↩ Back");
        backButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

        // กำหนดการกระทำเมื่อกดปุ่มยืนยันเติมเงิน
        topUpButton.setOnAction(e -> {
            try {
                int amount = Integer.parseInt(amountField.getText());

                // ✅ ตรวจสอบยอดเติมเงินต้องมากกว่า 0
                if (amount <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Amount must be greater than 0.");
                    return;
                }
                // ✅ จำกัดจำนวนเงินสูงสุดต่อครั้งที่ 1,000 บาท
                if (amount > 1000) {
                    showAlert(Alert.AlertType.ERROR, "Limit Exceeded", "Maximum top-up per transaction is 1,000.");
                    return;
                }

                // ✅ ตรวจสอบว่าผู้ใช้กรอก PIN หรือยัง
                if (pinField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "PIN Required", "Please enter your PIN before confirming.");
                    return;
                }

                // ✅ เติมเงินเข้า UserData และแจ้งเตือนผู้ใช้
                UserData.addBalance(amount);
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Top-Up Successful!\nNew Balance: " + UserData.getBalance() + " บาท");
                new window(primaryStage); // กลับไปหน้าเมนูหลัก
            } catch (NumberFormatException ex) {
                // แสดงข้อความแจ้งเตือนเมื่อผู้ใช้ป้อนค่าที่ไม่ใช่ตัวเลข
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number.");
            }
        });

        // กำหนดการกระทำเมื่อกดปุ่มย้อนกลับ
        backButton.setOnAction(e -> new window(primaryStage));

        // จัดเรียง UI องค์ประกอบแต่ละส่วน
        HBox amountBox = new HBox(10, amountLabel, amountField);
        HBox bankBox = new HBox(10, bankLabel, bankComboBox);
        HBox pinBox = new HBox(10, pinLabel, pinField);
        HBox buttonBox = new HBox(10, topUpButton, backButton);

        amountBox.setAlignment(Pos.CENTER);
        bankBox.setAlignment(Pos.CENTER);
        pinBox.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);

        // จัดกลุ่ม UI ทั้งหมดไว้ใน VBox
        VBox vbox = new VBox(15, titleLabel, amountBox, bankBox, pinBox, buttonBox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #e3f2fd; -fx-padding: 30px; -fx-border-radius: 10px;");

        // กำหนด Scene และแสดงหน้าต่าง Top-Up
        Scene scene = new Scene(vbox, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Top-Up");
        primaryStage.show();
    }

    // เมธอดสำหรับแสดง Alert แจ้งเตือนหรือข้อความต่าง ๆ
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
