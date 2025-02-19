import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Register");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        Button registerButton = new Button("Register");
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
                messageLabel.setText("Registration Successful!");
            }
        });
        
        VBox vbox = new VBox(10, label, usernameField, passwordField, confirmPasswordField, registerButton, messageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefSize(300, 250);
        
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Register Page");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}