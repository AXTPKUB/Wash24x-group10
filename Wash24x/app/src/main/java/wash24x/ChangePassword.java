package wash24x;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChangePassword {
    // คอนสตรักเตอร์สำหรับหน้าต่างเปลี่ยนรหัสผ่าน
    public ChangePassword(Stage primaryStage, String username) {
        Stage passwordStage = new Stage(); // สร้าง Stage ใหม่เพื่อแสดงหน้าต่างเปลี่ยนรหัสผ่าน
        passwordStage.setTitle("Change Password");

        // สร้าง Label สำหรับหัวข้อ
        Label titleLabel = new Label("🔑 Change Password");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ฟิลด์รหัสผ่านเดิม
        PasswordField oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("Current Password");

        // ฟิลด์รหัสผ่านใหม่
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");

        // ฟิลด์ยืนยันรหัสผ่านใหม่
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm New Password");

        // ปุ่มบันทึกรหัสผ่านใหม่
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            // ตรวจสอบรหัสผ่านเก่า
            if (!UserData.verifyPassword(username, oldPassword)) {
                showAlert(Alert.AlertType.ERROR, "Incorrect current password!");
            }
            // ตรวจสอบว่ารหัสผ่านใหม่ที่ใส่ทั้งสองช่องตรงกันหรือไม่
            else if (!newPassword.equals(confirmPassword)) {
                showAlert(Alert.AlertType.ERROR, "New passwords do not match!");
            }
            // ตรวจสอบว่ารหัสผ่านใหม่ไม่ว่างเปล่า
            else if (newPassword.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Password cannot be empty!");
            }
            // หากผ่านเงื่อนไขทั้งหมด อัปเดตรหัสผ่าน
            else {
                UserData.updatePassword(username, newPassword);
                showAlert(Alert.AlertType.INFORMATION, "Password updated successfully!");
                passwordStage.close(); // ปิดหน้าต่างหลังจากอัปเดตรหัสผ่านสำเร็จ
            }
        });

        // ปุ่มยกเลิก
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> passwordStage.close());

        // จัดองค์ประกอบใน VBox
        VBox vbox = new VBox(10, titleLabel, oldPasswordField, newPasswordField, confirmPasswordField, saveButton,
                cancelButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        // ตั้งค่า Scene และแสดงหน้าต่าง
        Scene scene = new Scene(vbox, 300, 300);
        passwordStage.setScene(scene);
        passwordStage.show();
    }

    // เมธอดสำหรับแสดง Alert แจ้งเตือน
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.showAndWait();
    }
}
