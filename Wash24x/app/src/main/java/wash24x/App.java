package wash24x;

import java.util.function.BooleanSupplier;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class App extends Application {
    private static Stage primaryStage; // เก็บ Stage หลัก เพื่อให้สามารถเรียกใช้ได้ในภายหลัง
    public Parent root; // ไม่ได้ใช้งานในโค้ด ควรลบออกหากไม่มีความจำเป็น

    // ประกาศตัวแปรปุ่ม
    private Button LoginAppBtn = new Button(" LoginApp ");
    private Button RegisterBtn = new Button("  Register  ");

    @Override
    public void start(Stage stage) {
        primaryStage = stage; // กำหนด Stage หลัก

        // สร้าง Label สำหรับข้อความ
        Label titleLabel = new Label("🐦‍🔥 Wash24x");
        Label titleLabe2 = new Label("'🧺Laundry service that you can trust'");

        // กำหนดสไตล์ให้กับ Label
        titleLabel.setStyle(
                "-fx-font-size: 64px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, black, 3, 0, 2, 2);");
        titleLabe2.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, black, 3, 0, 3, 3);");

        // ตั้งค่าการทำงานของปุ่ม -> เรียกใช้งานหน้าต่าง Login และ Register
        LoginAppBtn.setOnAction(e -> new Login(stage));
        RegisterBtn.setOnAction(e -> new Register(stage));

        // จัดเรียงองค์ประกอบใน VBox โดยมีระยะห่างระหว่างองค์ประกอบ 15px
        VBox vbox = new VBox(15, titleLabel, titleLabe2, LoginAppBtn, RegisterBtn);
        vbox.setAlignment(Pos.CENTER); // จัดให้อยู่กึ่งกลาง
        vbox.setStyle(
                "-fx-background-color: rgb(1, 128, 255); -fx-border-color: #ced4da; -fx-border-radius: 10px; -fx-padding: 20px;");

        // สร้าง Scene และกำหนดให้กับ Stage หลัก
        Scene scene = new Scene(vbox, 550, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Menu");
        primaryStage.show(); // แสดงหน้าต่างหลัก
    }

    // เมธอดสำหรับแสดงเมนูหลัก ใช้เมื่อมีการกลับมาที่หน้าหลัก
    public static void showMainMenu() {
        javafx.application.Platform.runLater(() -> {
            new App().start(primaryStage); // เรียก start() ใหม่ ทำให้เกิดการสร้าง instance ใหม่ของ App ซึ่งอาจไม่จำเป็น
        });
    }

    public static void main(String[] args) {
        launch(args); // เริ่มต้นแอปพลิเคชัน
    }

	public static BooleanSupplier isMainMenuDisplayed() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'isMainMenuDisplayed'");
	}
}
