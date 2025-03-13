package wash24x;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StaffDashboard {
    private ListView<String> reportListView;
    private ObservableList<String> reports;
    
    public StaffDashboard(Stage primaryStage) {
        Label titleLabel = new Label("📢 Staff Dashboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        reports = FXCollections.observableArrayList();
        reportListView = new ListView<>(reports);
        reportListView.setStyle("-fx-background-color: white; -fx-border-radius: 10px;");

        Button checkButton = new Button("View Machine Status");
        Button logoutButton = new Button("Logout");
        
        checkButton.setOnAction(e -> new window(primaryStage));
        logoutButton.setOnAction(e -> new Login(primaryStage));
        
        VBox vbox = new VBox(15, titleLabel, reportListView, checkButton, logoutButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #003366; -fx-padding: 20px;");
        
        Scene scene = new Scene(vbox, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff Dashboard");
        primaryStage.show();  
    }

    public void addReport(String user, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        reports.add(user + " (" + timestamp + "): " + message);
    }
}

class MachineStatusWindow {
    public MachineStatusWindow(Stage primaryStage) {
        Label label = new Label("🛠 Machine Status Window");
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox vbox = new VBox(15, label);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #003366; -fx-padding: 20px;");

        Scene scene = new Scene(vbox, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Machine Status");
        primaryStage.show();
    }
}