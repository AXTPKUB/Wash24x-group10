package wash24x;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Register {

    public Register(Stage primaryStage) {
        Label titleLabel = new Label("📝 Register to Wash24x");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        Button registerButton = new Button("Register");
        Button backButton = new Button("Back"); // ปุ่มกลับหน้า
        Label messageLabel = new Label();

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                messageLabel.setText("All fields are required!");
            } else if (!password.equals(confirmPassword)) {
                messageLabel.setText("Passwords do not match!");
            } else {
                // 🔄 เรียกใช้ UserData.register
                boolean success = UserData.register(username, password);
                if (success) {
                    messageLabel.setText("  🪪Registration Successful!  ");
                } else {
                    messageLabel.setText("Username already exists!⛔");
                }
            }
        });

        // ปุ่มกลับไปหน้า Login
        backButton.setOnAction(e -> App.showMainMenu());

        VBox vbox = new VBox(10, titleLabel, usernameField, passwordField, confirmPasswordField, registerButton, backButton, messageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefSize(300, 300);
        vbox.setStyle("-fx-background-color:rgb(1, 128, 255); -fx-border-color: #ced4da; -fx-border-radius: 10px; -fx-padding: 20px;");

        Scene scene = new Scene(vbox, 550, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("🐦‍🔥 Register Page");
        primaryStage.show();
    }
}
