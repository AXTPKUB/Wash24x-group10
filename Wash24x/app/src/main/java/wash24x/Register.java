package wash24x; // แพ็คเกจของโปรแกรม Wash24x

// นำเข้าไลบรารีที่จำเป็นสำหรับการสร้าง GUI ด้วย JavaFX
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

/**
 * คลาส Register ใช้สำหรับการแสดงหน้าลงทะเบียนผู้ใช้ของแอปพลิเคชัน Wash24x
 */
public class Register {
    /**
     * คอนสตรักเตอร์ของคลาส Register
     * สร้างและแสดงหน้า UI สำหรับการลงทะเบียนผู้ใช้ใหม่
     * 
     * @param primaryStage Stage หลักสำหรับแสดงหน้าลงทะเบียน
     */
    public Register(Stage primaryStage) {
        // สร้างป้ายชื่อสำหรับหน้าลงทะเบียน
        Label titleLabel = new Label("📝 Register to Wash24x");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;"); // กำหนดสไตล์ให้ข้อความหัวข้อ

        // สร้างช่องกรอกชื่อผู้ใช้
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username"); // ตั้งข้อความตัวอย่างในช่อง

        // สร้างช่องกรอกรหัสผ่าน (แสดงเป็นจุดปกปิด)
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password"); // ตั้งข้อความตัวอย่างในช่อง

        // สร้างป้ายที่แสดงข้อกำหนดของรหัสผ่าน
        Label passwordRequirementsLabel = new Label(
                "Password must be 8-16 characters, include uppercase, lowercase, and numbers");
        passwordRequirementsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill:rgb(255, 255, 255);"); // ตั้งสไตล์เป็นสีขาว
        passwordRequirementsLabel.setWrapText(true); // ให้ข้อความสามารถขึ้นบรรทัดใหม่ได้

        // สร้างช่องกรอกรหัสผ่านซ้ำเพื่อยืนยัน
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password"); // ตั้งข้อความตัวอย่างในช่อง

        // สร้างปุ่มลงทะเบียนและปุ่มย้อนกลับ
        Button registerButton = new Button("Register");
        Button backButton = new Button("Back");

        // สร้างป้ายสำหรับแสดงข้อความแจ้งเตือนต่างๆ
        Label messageLabel = new Label();
        messageLabel.setWrapText(true); // ให้ข้อความสามารถขึ้นบรรทัดใหม่ได้

        // กำหนดการทำงานเมื่อกดปุ่มลงทะเบียน
        registerButton.setOnAction(e -> {
            // ดึงข้อมูลจากช่องกรอก
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            // รีเซ็ตสีข้อความแจ้งเตือนให้เป็นสีดำ (ค่าเริ่มต้น)
            messageLabel.setTextFill(Color.BLACK);

            // ตรวจสอบว่ากรอกข้อมูลครบทุกช่องหรือไม่
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                messageLabel.setText("All fields are required!"); // แจ้งเตือนว่าต้องกรอกทุกช่อง
                messageLabel.setTextFill(Color.RED); // ตั้งสีข้อความเป็นสีแดง
            }
            // ตรวจสอบว่ารหัสผ่านและรหัสผ่านที่ยืนยันตรงกันหรือไม่
            else if (!password.equals(confirmPassword)) {
                messageLabel.setText("Passwords do not match!"); // แจ้งเตือนว่ารหัสผ่านไม่ตรงกัน
                messageLabel.setTextFill(Color.RED); // ตั้งสีข้อความเป็นสีแดง
            }
            // ตรวจสอบว่ารหัสผ่านตรงตามเงื่อนไขความซับซ้อนหรือไม่
            else if (!isValidPassword(password)) {
                messageLabel.setText(
                        "Password must be 8-16 characters and contain at least one uppercase letter, one lowercase letter, and one number!");
                messageLabel.setStyle("\"-fx-background-color:rgb(224, 54, 35); -fx-text-fill: white;\""); // ตั้งพื้นหลังเป็นสีแดง
                                                                                                           // ตัวอักษรเป็นสีขาว
                messageLabel.setTextFill(Color.RED); // ตั้งสีข้อความเป็นสีแดง
            }
            // ถ้าผ่านเงื่อนไขทั้งหมด ทำการลงทะเบียน
            else {
                // เรียกใช้ฟังก์ชัน register จากคลาส UserData เพื่อลงทะเบียนผู้ใช้
                boolean success = UserData.register(username, password);
                if (success) {
                    messageLabel.setText("  🪪Registration Successful!  "); // แจ้งเตือนว่าลงทะเบียนสำเร็จ
                    messageLabel.setTextFill(Color.GREEN); // ตั้งสีข้อความเป็นสีเขียว
                } else {
                    messageLabel.setText("Username already exists!⛔"); // แจ้งเตือนว่าชื่อผู้ใช้มีอยู่แล้ว
                    messageLabel.setTextFill(Color.RED); // ตั้งสีข้อความเป็นสีแดง
                }
            }
        });

        // กำหนดการทำงานเมื่อกดปุ่มย้อนกลับ - กลับไปที่หน้าเมนูหลัก
        backButton.setOnAction(e -> App.showMainMenu());

        // สร้าง Layout แบบ VBox สำหรับจัดเรียงองค์ประกอบในแนวตั้ง
        VBox vbox = new VBox(10, titleLabel, usernameField, passwordField, passwordRequirementsLabel,
                confirmPasswordField, registerButton, backButton, messageLabel);
        vbox.setAlignment(Pos.CENTER); // จัดให้องค์ประกอบอยู่ตรงกลาง
        vbox.setPrefSize(300, 300); // กำหนดขนาดที่ต้องการของ VBox
        vbox.setStyle(
                "-fx-background-color:rgb(1, 128, 255); -fx-border-color: #ced4da; -fx-border-radius: 10px; -fx-padding: 20px;");
        // ตั้งสไตล์ให้ VBox - พื้นหลังสีฟ้า, มีขอบสีเทา, มุมโค้ง, และมี padding รอบๆ

        // สร้าง Scene สำหรับแสดงผล GUI
        Scene scene = new Scene(vbox, 550, 550);
        primaryStage.setScene(scene); // ตั้ง Scene ให้กับ Stage
        primaryStage.setTitle("🐦‍🔥 Register Page"); // ตั้งชื่อหน้าต่าง
        primaryStage.show(); // แสดงหน้าต่าง
    }

    /**
     * ตรวจสอบความซับซ้อนของรหัสผ่าน
     * - ต้องมีความยาว 8-16 ตัวอักษร
     * - ต้องมีตัวพิมพ์ใหญ่อย่างน้อย 1 ตัว
     * - ต้องมีตัวพิมพ์เล็กอย่างน้อย 1 ตัว
     * - ต้องมีตัวเลขอย่างน้อย 1 ตัว
     * 
     * @param password รหัสผ่านที่ต้องการตรวจสอบ
     * @return true ถ้ารหัสผ่านตรงตามเงื่อนไข, false ถ้าไม่ตรง
     */
    private boolean isValidPassword(String password) {
        // ตรวจสอบความยาว (ต้องอยู่ระหว่าง 8-16 ตัวอักษร)
        if (password.length() < 8 || password.length() > 16) {
            return false;
        }

        // ตรวจสอบว่ามีตัวพิมพ์ใหญ่อย่างน้อย 1 ตัว
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        // ตรวจสอบว่ามีตัวพิมพ์เล็กอย่างน้อย 1 ตัว
        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        // ตรวจสอบว่ามีตัวเลขอย่างน้อย 1 ตัว
        if (!password.matches(".*[0-9].*")) {
            return false;
        }

        // ถ้าผ่านเงื่อนไขทั้งหมด
        return true;
    }
}