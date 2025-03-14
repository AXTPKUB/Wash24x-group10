/*
 * This source file was generated by the Gradle 'init' task
 */
package wash24x;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class UpdateAccountTest extends ApplicationTest {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        new UpdateAccount(primaryStage, "testUser");
    }

    @Test
    void testUIElementsExist() {
        assertNotNull(lookup("🛠️ Update Account").queryLabeled());
        assertNotNull(lookup("Select Profile:").queryLabeled());
        assertNotNull(lookup(".combo-box").query()); // Profile ComboBox
        assertNotNull(lookup("New Username:").queryLabeled());
        assertNotNull(lookup(".text-field").query()); // Username Field
        assertNotNull(lookup("Enter Password to Confirm:").queryLabeled());
        assertNotNull(lookup(".password-field").query()); // Password Field
        assertNotNull(lookup("Change Password").queryButton());
        assertNotNull(lookup("Save Changes").queryButton());
        assertNotNull(lookup("↩️ Back").queryButton());
    }

    @Test
    void testValidUsernameChange() {
        clickOn(".text-field").eraseText(10).write("newUsername");
        clickOn(".password-field").write("correctPassword");
        clickOn("Save Changes");

        Label successLabel = lookup(".label").queryAs(Label.class);
        assertTrue(successLabel.getText().contains("Account Updated Successfully!"));
    }

    @Test
    void testProfileChangeOnly() {
        clickOn(".combo-box").clickOn("🐶"); // เปลี่ยนเป็นโปรไฟล์รูปหมา
        clickOn(".password-field").write("correctPassword");
        clickOn("Save Changes");

        assertTrue(lookup("Account Updated Successfully!").queryLabeled().isVisible());
    }

    @Test
    void testWrongPassword() {
        clickOn(".text-field").eraseText(10).write("newUsername");
        clickOn(".password-field").write("wrongPassword");
        clickOn("Save Changes");

        assertTrue(lookup("Incorrect password!").queryLabeled().isVisible());
    }

    @Test
    void testDuplicateUsername() {
        clickOn(".text-field").eraseText(10).write("existingUser");
        clickOn(".password-field").write("correctPassword");
        clickOn("Save Changes");

        assertTrue(lookup("Username already exists!").queryLabeled().isVisible());
    }

    @Test
    void testEmptyPassword() {
        clickOn(".text-field").eraseText(10).write("newUsername");
        clickOn("Save Changes");

        assertTrue(lookup("Incorrect password!").queryLabeled().isVisible());
    }

    @Test
    void testNoChanges() {
        clickOn("Save Changes");

        assertTrue(lookup("No changes were made.").queryLabeled().isVisible());
    }

    @Test
    void testBackButton() {
        clickOn("↩️ Back");
        assertTrue(primaryStage.getScene().getRoot() instanceof VBox); // ตรวจสอบว่ากลับไปหน้าเมนูหลัก
    }
}
