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

        Label titleLabel = new Label(" 📋 Login to Wash24x 🐦‍🔥");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(80);
        HBox usernameBox = new HBox(10,  usernameField); //userIcon,
        usernameBox.setAlignment(Pos.BASELINE_CENTER);

       
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(80);
        HBox passwordBox = new HBox(10,  passwordField); //lockIcon,
        passwordBox.setAlignment(Pos.BASELINE_CENTER);

        // ✅ ปุ่ม Login
        Button loginButton = new Button("Login", new ImageView(new Image("file:login_icon.png")));
        loginButton.setStyle("-fx-background-color:rgb(37, 198, 74); -fx-text-fill: white; -fx-pref-height: 120px;");

        // 🔙 ปุ่ม Back
        Button backButton = new Button("Back", new ImageView(new Image("file:back_icon.png")));
        backButton.setStyle("-fx-background-color:rgb(236, 11, 34); -fx-text-fill: white; -fx-pref-height: 120px;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 140px;");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
        
            if (UserData.login(username, password)) {
                String role = UserData.getUserRole(username);
                messageLabel.setText("Login Successful! Role: " + role);
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
        

        backButton.setOnAction(e -> App.showMainMenu());

        VBox vbox = new VBox(10, logo, titleLabel, usernameBox, passwordBox, loginButton, backButton, messageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(100));
        vbox.setStyle("-fx-background-color:rgb(1, 128, 255); -fx-border-color: #ced4da; -fx-border-radius: 10px; -fx-padding: 20px;");

        Scene scene = new Scene(vbox, 550, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("🐦‍🔥 Login Page");
        primaryStage.show();
    }
}
