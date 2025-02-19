import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class WashOrderApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Wash Order");

        // สร้าง Layout เป็น GridPane
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        // Label และ TextField สำหรับข้อมูลคำสั่งซักผ้า
        Label nameLabel = new Label("Customer Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");

        Label typeLabel = new Label("Service Type:");
        ComboBox<String> serviceTypeComboBox = new ComboBox<>();
        serviceTypeComboBox.getItems().addAll("Regular Wash", "Dry Clean", "Ironing", "Deluxe Wash");
        serviceTypeComboBox.setValue("Regular Wash");

        Label clothesLabel = new Label("Number of Clothes:");
        TextField clothesField = new TextField();
        clothesField.setPromptText("Enter number of clothes");

        Label detergentLabel = new Label("Detergent Type:");
        ComboBox<String> detergentComboBox = new ComboBox<>();
        detergentComboBox.getItems().addAll("Regular", "Sensitive Skin", "Eco-Friendly");
        detergentComboBox.setValue("Regular");

        Label pickupDateLabel = new Label("Pickup Date (YYYY-MM-DD):");
        TextField pickupDateField = new TextField();
        pickupDateField.setPromptText("Enter pickup date");

        // ปุ่มสำหรับส่งคำสั่ง
        Button orderButton = new Button("Place Order");

        // เมื่อคลิกปุ่ม Place Order
        orderButton.setOnAction(e -> {
            String name = nameField.getText();
            String serviceType = serviceTypeComboBox.getValue();
            String clothesCount = clothesField.getText();
            String detergentType = detergentComboBox.getValue();
            String pickupDate = pickupDateField.getText();

            // ตรวจสอบข้อมูลที่กรอก
            if (name.isEmpty() || clothesCount.isEmpty() || pickupDate.isEmpty()) {
                showAlert("Error", "Please fill in all fields.");
            } else {
                // แสดงข้อความยืนยันคำสั่งซักผ้า
                showAlert("Order Placed", "Order successfully placed for " + name + "\n" +
                        "Service: " + serviceType + "\n" +
                        "Number of Clothes: " + clothesCount + "\n" +
                        "Detergent Type: " + detergentType + "\n" +
                        "Pickup Date: " + pickupDate);
            }
        });

        // จัดวาง Label, TextField, ComboBox และปุ่มใน GridPane
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(typeLabel, 0, 1);
        grid.add(serviceTypeComboBox, 1, 1);
        grid.add(clothesLabel, 0, 2);
        grid.add(clothesField, 1, 2);
        grid.add(detergentLabel, 0, 3);
        grid.add(detergentComboBox, 1, 3);
        grid.add(pickupDateLabel, 0, 4);
        grid.add(pickupDateField, 1, 4);
        grid.add(orderButton, 1, 5);

        // สร้าง Scene และแสดงผล
        Scene scene = new Scene(grid, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ฟังก์ชันแสดง Alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
