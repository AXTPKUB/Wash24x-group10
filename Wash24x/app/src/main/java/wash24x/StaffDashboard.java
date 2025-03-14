package wash24x;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StaffDashboard {

    // ListView สำหรับแสดงรายการปัญหาที่ถูกรายงานเข้ามา
    private ListView<String> reportListView;

    public StaffDashboard(Stage primaryStage) {
        // สร้าง Label สำหรับหัวข้อหลักของแดชบอร์ด
        Label titleLabel = new Label("📢 Staff Dashboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        // ปุ่มสำหรับตรวจสอบสถานะเครื่องซักผ้า
        Button checkButton = new Button("View Machine Status");
        // ปุ่มสำหรับออกจากระบบ
        Button logoutButton = new Button("Logout");

        // กำหนดการกระทำเมื่อกดปุ่มตรวจสอบสถานะเครื่อง
        checkButton.setOnAction(e -> new window(primaryStage));
        // กำหนดการกระทำเมื่อกดปุ่มออกจากระบบ
        logoutButton.setOnAction(e -> new Login(primaryStage));

        // ใช้ ObservableList ที่แชร์จาก UserData เพื่อแสดงรายการปัญหาที่ถูกแจ้ง
        reportListView = new ListView<>(UserData.getReportList());
        reportListView.setPrefHeight(200);

        // Label สำหรับแสดงหัวข้อ "Reported Issues"
        Label reportLabel = new Label("Reported Issues:");
        reportLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        // จัดองค์ประกอบต่าง ๆ ลงใน VBox และกำหนดการจัดวาง
        VBox vbox = new VBox(15, titleLabel, checkButton, logoutButton, reportLabel, reportListView);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #003366; -fx-padding: 20px;");

        // สร้างและกำหนด Scene สำหรับหน้าต่างแดชบอร์ด
        Scene scene = new Scene(vbox, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff Dashboard");
        primaryStage.show();
    }

    // ถ้าต้องการเพิ่มรายงานจากที่อื่น ให้ใช้ UserData.addReport(...) โดยตรง
}
