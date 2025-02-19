package wash24x.group10;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Login");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button loginButton = new Button("Login");
        Label messageLabel = new Label();
        
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if ("admin".equals(username) && "1234".equals(password)) {
                messageLabel.setText("Login Successful!");
            } else {
                messageLabel.setText("Invalid Username or Password");
            }
        });
        
        VBox vbox = new VBox(10, label, usernameField, passwordField, loginButton, messageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefSize(300, 200);
        
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Page");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
