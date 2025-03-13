package wash24x;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class App extends Application {
    private static Stage primaryStage; // เก็บ Stage หลัก
    public Parent root;

    // ประกาศตัวแปรปุ่ม
    private Button LoginAppBtn = new Button(" LoginApp ");
    private Button RegisterBtn = new Button("  Register  ");

    @Override
    public void start(Stage stage) {
        primaryStage = stage; // กำหนด Stage หลัก

        // สร้าง Label สำหรับข้อความ
        Label titleLabel = new Label("🐦‍🔥 Wash24x");
        Label titleLabe2 = new Label("'🧺Laundry service that you can trust'");
        titleLabel.setStyle("-fx-font-size: 64px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, black, 3, 0, 2, 2);");
        titleLabe2.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, black, 3, 0, 3, 3);");


        // ตั้งค่าการทำงานของปุ่ม
        LoginAppBtn.setOnAction(e -> new Login(stage));
        RegisterBtn.setOnAction(e -> new Register(stage));

        // สร้าง VBox และเพิ่ม titleLabel
        VBox vbox = new VBox(15, titleLabel,titleLabe2, LoginAppBtn, RegisterBtn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: rgb(1, 128, 255); -fx-border-color: #ced4da; -fx-border-radius: 10px; -fx-padding: 20px;");

        // ตั้งค่า Scene
        Scene scene = new Scene(vbox, 550, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Menu");
        primaryStage.show();
    }

    public static void showMainMenu() {
        javafx.application.Platform.runLater(() -> {
            new App().start(primaryStage);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
