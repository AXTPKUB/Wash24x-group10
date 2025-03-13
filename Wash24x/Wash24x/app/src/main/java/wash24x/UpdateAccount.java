package wash24x;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UpdateAccount {

    public UpdateAccount(Stage primaryStage, String username) {
        Label titleLabel = new Label("🛠️ Update Account");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // เลือกโปรไฟล์โดยใช้อิโมจิ
        Label profileLabel = new Label("Select Profile:");
        ComboBox<String> profileComboBox = new ComboBox<>();
        profileComboBox.getItems().addAll("😀", "😎", "🐱", "🐶", "👤");
        profileComboBox.setValue(UserData.getUserProfile(username)); // ตั้งค่าโปรไฟล์เดิม
        
        // เปลี่ยนชื่อบัญชี
        Label nameLabel = new Label("New Username:");
        TextField nameField = new TextField(username);

        // ปุ่มเปลี่ยนรหัสผ่าน
        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setOnAction(e -> new ChangePassword(primaryStage, username));

        // ยืนยันรหัสผ่านก่อนเปลี่ยนแปลง
        Label passwordLabel = new Label("Enter Password to Confirm:");
        PasswordField passwordField = new PasswordField();

        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            String newProfile = profileComboBox.getValue();
            String newName = nameField.getText();
            String password = passwordField.getText();
            
            if (UserData.verifyPassword(username, password)) {
                UserData.updateProfile(username, newProfile);
                UserData.updateUsername(username, newName);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account Updated Successfully!");
                alert.showAndWait();
                new window(primaryStage); // กลับไปหน้าหลัก
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect Password!");
                alert.showAndWait();
            }
        });

        Button backButton = new Button("↩️ Back");
        backButton.setOnAction(e -> new window(primaryStage));
        
        VBox vbox = new VBox(10, titleLabel, profileLabel, profileComboBox, nameLabel, nameField, passwordLabel, passwordField, changePasswordButton, saveButton, backButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
