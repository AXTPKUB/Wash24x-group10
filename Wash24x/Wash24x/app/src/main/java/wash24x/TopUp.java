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
        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));

        Label titleLabel = new Label("💲 Top-Up System");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        Label amountLabel = new Label("Amount: ");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");

        Label bankLabel = new Label("Bank: ");
        ComboBox<String> bankComboBox = new ComboBox<>();
        bankComboBox.getItems().addAll("Bank A", "Bank B", "Bank C", "Bank D");
        bankComboBox.setValue("Bank A");

        Label pinLabel = new Label("Enter PIN: ");
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("****");

        Button topUpButton = new Button("✔ Confirm Top-Up");
        topUpButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        
        Button backButton = new Button("↩ Back");
        backButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

        topUpButton.setOnAction(e -> {
            try {
                int amount = Integer.parseInt(amountField.getText());
                if (amount <= 0 || amount > 10000) { // จำกัดจำนวนเงินไม่ให้เวอร์เกินไป
                    showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Amount must be between 1 - 10,000");
                    return;
                }
                
                if (pinField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "PIN Required", "Please enter your PIN before confirming");
                    return;
                }
                
                UserData.addBalance(amount);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Top-Up Successful!");
                new window(primaryStage);  // กลับไปหน้าเมนูหลัก
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number");
            }
        });

        backButton.setOnAction(e -> new window(primaryStage));

        HBox amountBox = new HBox(10, amountLabel, amountField);
        HBox bankBox = new HBox(10, bankLabel, bankComboBox);
        HBox pinBox = new HBox(10, pinLabel, pinField);
        HBox buttonBox = new HBox(10, topUpButton, backButton);
        
        amountBox.setAlignment(Pos.CENTER);
        bankBox.setAlignment(Pos.CENTER);
        pinBox.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(15, titleLabel, amountBox, bankBox, pinBox, buttonBox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #e3f2fd; -fx-padding: 30px; -fx-border-radius: 10px;");
        
        Scene scene = new Scene(vbox, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Top-Up");
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
