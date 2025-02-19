import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PaymentGatewayApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Payment Gateway");
        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("Card Number");
        TextField cardHolderField = new TextField();
        cardHolderField.setPromptText("Card Holder Name");
        PasswordField cvvField = new PasswordField();
        cvvField.setPromptText("CVV");
        DatePicker expiryDateField = new DatePicker();
        Button payButton = new Button("Pay");
        Label messageLabel = new Label();
        
        payButton.setOnAction(e -> {
            String cardNumber = cardNumberField.getText();
            String cardHolder = cardHolderField.getText();
            String cvv = cvvField.getText();
            
            if (cardNumber.isEmpty() || cardHolder.isEmpty() || cvv.isEmpty() || expiryDateField.getValue() == null) {
                messageLabel.setText("All fields are required!");
            } else {
                messageLabel.setText("Payment Successful!");
            }
        });
        
        VBox vbox = new VBox(10, label, cardNumberField, cardHolderField, cvvField, expiryDateField, payButton, messageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefSize(300, 300);
        
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Payment Gateway");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
