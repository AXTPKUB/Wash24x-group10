package wash24x;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Login {

    public Login(Stage primaryStage) {
        // โลโก้ของแอป
        ImageView logo = new ImageView(new Image("file:logo.png"));
        logo.setFitWidth(100);
        logo.setFitHeight(100);

        // ชื่อหัวข้อ
        Label titleLabel = new Label(" 📋 Login to Wash24x 🐦‍🔥");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ฟิลด์สำหรับใส่ Username
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(80);

        // กล่องจัดเรียง Username (หากมีไอคอน user ให้เพิ่มตรงนี้)
        HBox usernameBox = new HBox(10, usernameField);
        usernameBox.setAlignment(Pos.BASELINE_CENTER);

        // ฟิลด์สำหรับใส่ Password
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(80);

        // กล่องจัดเรียง Password (หากมีไอคอน lock ให้เพิ่มตรงนี้)
        HBox passwordBox = new HBox(10, passwordField);
        passwordBox.setAlignment(Pos.BASELINE_CENTER);

        // ✅ ปุ่ม Login พร้อมไอคอน
        Button loginButton = new Button("Login", new ImageView(new Image("file:login_icon.png")));
        loginButton.setStyle("-fx-background-color:rgb(37, 198, 74); -fx-text-fill: white; -fx-pref-height: 120px;");

        // 🔙 ปุ่ม Back พร้อมไอคอน
        Button backButton = new Button("Back", new ImageView(new Image("file:back_icon.png")));
        backButton.setStyle("-fx-background-color:rgb(236, 11, 34); -fx-text-fill: white; -fx-pref-height: 120px;");

        // Label สำหรับแสดงข้อความแจ้งเตือน
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;"); // แก้ไขขนาดให้เหมาะสม (จาก 140px เป็น 14px)

        // การทำงานของปุ่ม Login
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (UserData.login(username, password)) { // ตรวจสอบการเข้าสู่ระบบ
                String role = UserData.getUserRole(username);
                messageLabel.setText("Login Successful! Role: " + role);

                // นำทางไปยังหน้าที่เหมาะสมกับบทบาทของผู้ใช้
                switch (role) {
                    case "staff":
                        new StaffDashboard(primaryStage);
                        break;
                    default:
                        new window(primaryStage);
                }
            } else {
                messageLabel.setText("Invalid Username or Password");
            }
        });

        // การทำงานของปุ่ม Back -> กลับไปที่เมนูหลัก
        backButton.setOnAction(e -> App.showMainMenu());

        // จัดองค์ประกอบทั้งหมดใน VBox
        VBox vbox = new VBox(10, logo, titleLabel, usernameBox, passwordBox, loginButton, backButton, messageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(100));
        vbox.setStyle(
                "-fx-background-color:rgb(1, 128, 255); -fx-border-color: #ced4da; -fx-border-radius: 10px; -fx-padding: 20px;");

        // ตั้งค่า Scene และแสดงหน้าต่าง
        Scene scene = new Scene(vbox, 550, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("🐦‍🔥 Login Page");
        primaryStage.show();
    }
}
