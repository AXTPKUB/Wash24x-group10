package wash24x;

import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;

class RegisterTest extends ApplicationTest {

    private Register registerPage;

    @Override
    public void start(Stage stage) {
        registerPage = new Register(stage);
    }

    @Test
    void testRegisterWithValidInputs() {
        String username = "newUser";
        String password = "Password123";
        String confirmPassword = "Password123";

        UserData.clearUsers(); // ล้างข้อมูลผู้ใช้ก่อนทดสอบ
        clickOn(".text-field").write(username);
        clickOn(".password-field").write(password);
        clickOn(".password-field").write(confirmPassword);
        clickOn(".button:contains('Register')");

        assertEquals("🪪Registration Successful!", lookup(".label").queryLabeled().getText());
        assertTrue(UserData.isUserRegistered(username));
    }

    @Test
    void testRegisterWithMismatchedPasswords() {
        clickOn(".text-field").write("user123");
        clickOn(".password-field").write("Password123");
        clickOn(".password-field").write("WrongPass123");
        clickOn(".button:contains('Register')");

        assertEquals("Passwords do not match!", lookup(".label").queryLabeled().getText());
    }

    @Test
    void testRegisterWithWeakPassword() {
        clickOn(".text-field").write("user456");
        clickOn(".password-field").write("weak");
        clickOn(".password-field").write("weak");
        clickOn(".button:contains('Register')");

        assertEquals("Password must be 8-16 characters and contain at least one uppercase letter, one lowercase letter, and one number!", lookup(".label").queryLabeled().getText());
    }

    @Test
    void testRegisterWithExistingUsername() {
        UserData.register("existingUser", "Password123"); // ลงทะเบียนผู้ใช้เดิมก่อน

        clickOn(".text-field").write("existingUser");
        clickOn(".password-field").write("Password123");
        clickOn(".password-field").write("Password123");
        clickOn(".button:contains('Register')");

        assertEquals("Username already exists!⛔", lookup(".label").queryLabeled().getText());
    }

    @Test
    void testBackButtonNavigation() {
        clickOn(".button:contains('Back')");
        assertTrue(App.isMainMenuDisplayed()); // ตรวจสอบว่ากลับไปที่เมนูหลักสำเร็จ
    }
}
