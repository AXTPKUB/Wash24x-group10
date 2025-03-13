package wash24x;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChangePassword {
    public ChangePassword(Stage primaryStage, String username) {
        Stage passwordStage = new Stage();
        passwordStage.setTitle("Change Password");

        Label titleLabel = new Label("🔑 Change Password");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        PasswordField oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("Current Password");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm New Password");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (!UserData.verifyPassword(username, oldPassword)) {
                showAlert(Alert.AlertType.ERROR, "Incorrect current password!");
            } else if (!newPassword.equals(confirmPassword)) {
                showAlert(Alert.AlertType.ERROR, "New passwords do not match!");
            } else if (newPassword.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Password cannot be empty!");
            } else {
                UserData.updatePassword(username, newPassword);
                showAlert(Alert.AlertType.INFORMATION, "Password updated successfully!");
                passwordStage.close();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> passwordStage.close());

        VBox vbox = new VBox(10, titleLabel, oldPasswordField, newPasswordField, confirmPasswordField, saveButton, cancelButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 300, 300);
        passwordStage.setScene(scene);
        passwordStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.showAndWait();
    }
}
