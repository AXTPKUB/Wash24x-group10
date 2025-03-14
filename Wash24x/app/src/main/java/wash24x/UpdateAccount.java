package wash24x;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UpdateAccount {

    // คอนสตรักเตอร์สำหรับอัปเดตบัญชีผู้ใช้
    public UpdateAccount(Stage primaryStage, String username) {
        // หัวข้อหลักของหน้าอัปเดตบัญชี
        Label titleLabel = new Label("🛠️ Update Account");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // เลือกโปรไฟล์โดยใช้อิโมจิ
        Label profileLabel = new Label("Select Profile:");
        ComboBox<String> profileComboBox = new ComboBox<>();
        profileComboBox.getItems().addAll("😀", "😎", "🐱", "🐶", "👤", "🥷", "👨‍✈️", "👨‍⚖️",
                "👨‍🎨", "👩‍🎨", "🎅", "👼", "🤴", "👨‍🔬", "👨‍💼",
                "👨‍🎓", "👨‍🍳", "👨‍🌾", "🧞‍♂️", "🧙‍♂️", "🧝‍♀️",
                "🧝‍♂️", "🧝", "🧛‍♂️", "🧌", "🧜‍♂️", "🧟‍♂️", "🧟");
        profileComboBox.setValue(UserData.getUserProfile(username)); // ตั้งค่าโปรไฟล์ปัจจุบันของผู้ใช้

        // ช่องเปลี่ยนชื่อบัญชี
        Label nameLabel = new Label("New Username:");
        TextField nameField = new TextField(username);

        // ปุ่มเปลี่ยนรหัสผ่าน
        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setOnAction(e -> new ChangePassword(primaryStage, username));

        // ช่องให้ผู้ใช้ป้อนรหัสผ่านเพื่อยืนยันการเปลี่ยนแปลง
        Label passwordLabel = new Label("Enter Password to Confirm:");
        PasswordField passwordField = new PasswordField();

        // ปุ่มบันทึกการเปลี่ยนแปลง
        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            String oldUsername = UserData.getCurrentUsername(); // รับชื่อผู้ใช้ปัจจุบัน
            String newProfile = profileComboBox.getValue();
            String newName = nameField.getText().trim();
            String password = passwordField.getText();

            // Debug Log
            System.out.println("Verifying password for: " + oldUsername);
            System.out.println("Entered Password: " + password);
            System.out.println("New Username: " + newName);
            System.out.println("New Profile: " + newProfile);

            boolean isProfileChanged = !newProfile.equals(UserData.getUserProfile(oldUsername));
            boolean isUsernameChanged = !newName.equals(oldUsername);

            // ถ้าไม่มีอะไรเปลี่ยนแปลง ไม่ต้องดำเนินการต่อ
            if (!isProfileChanged && !isUsernameChanged) {
                new Alert(Alert.AlertType.INFORMATION, "No changes were made.").showAndWait();
                return;
            }

            // ตรวจสอบว่าชื่อใหม่ต้องไม่เป็นค่าว่าง
            if (isUsernameChanged && newName.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Username cannot be empty!").showAndWait();
                return;
            }

            // ตรวจสอบว่าชื่อผู้ใช้ใหม่มีอยู่แล้วหรือไม่
            if (isUsernameChanged && UserData.userExists(newName)) {
                new Alert(Alert.AlertType.ERROR, "Username already exists!").showAndWait();
                return;
            }

            // ตรวจสอบรหัสผ่านของผู้ใช้ปัจจุบัน
            if (UserData.verifyPassword(oldUsername, password)) {
                System.out.println("✅ Password Verified!");

                try {
                    // อัปเดตโปรไฟล์ของผู้ใช้ถ้ามีการเปลี่ยนแปลง
                    if (isProfileChanged) {
                        UserData.updateProfile(oldUsername, newProfile);
                    }

                    // อัปเดตชื่อผู้ใช้หากมีการเปลี่ยนแปลง
                    if (isUsernameChanged) {
                        boolean success = UserData.updateUsername(oldUsername, newName);
                        if (!success)
                            throw new Exception("Failed to update username");
                    }

                    // กำหนดค่าผู้ใช้ปัจจุบันใหม่หลังการเปลี่ยนแปลง
                    UserData.setCurrentUsername(isUsernameChanged ? newName : oldUsername);
                    UserData.getUserProfile(newProfile);

                    System.out.println("Updated Username: " + UserData.getCurrentUsername());
                    System.out.println("Updated Profile: " + UserData.getCurrentUserProfile());

                    new Alert(Alert.AlertType.INFORMATION, "Account Updated Successfully!").showAndWait();

                    // กลับไปหน้าเมนูหลัก
                    primaryStage.setScene(new Scene(new window(primaryStage).getRoot(), 550, 550));
                } catch (Exception ex) {
                    System.err.println("Error updating account: " + ex.getMessage());
                    new Alert(Alert.AlertType.ERROR, "Error updating account: " + ex.getMessage()).showAndWait();
                }
            } else {
                System.out.println("❌ Password Incorrect!");
                new Alert(Alert.AlertType.ERROR, "Incorrect password!").showAndWait();
            }
        });

        // ปุ่มย้อนกลับไปหน้าเมนูหลัก
        Button backButton = new Button("↩️ Back");
        backButton.setOnAction(e -> new window(primaryStage));

        // จัดเรียงองค์ประกอบ UI ในแนวตั้ง
        VBox vbox = new VBox(10, titleLabel, profileLabel, profileComboBox, nameLabel, nameField,
                passwordLabel, passwordField, changePasswordButton, saveButton, backButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        // กำหนด Scene และแสดงหน้าต่าง Update Account
        Scene scene = new Scene(vbox, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
