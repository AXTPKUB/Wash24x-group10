package wash24x;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Labeled;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class WashOrderTest extends ApplicationTest {

    private WashOrder washOrder;
    private boolean onCompleteCalled;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        // จัดเตรียมข้อมูลจำลองสำหรับการทดสอบ
        primaryStage = stage;
        UserData.setCurrentUsername("testUser");
        UserData.setCurrentBalance(200); // กำหนดให้มียอดเงินเพียงพอในเบื้องต้น
        onCompleteCalled = false;
        Runnable onComplete = () -> onCompleteCalled = true;
        washOrder = new WashOrder(primaryStage, 1, onComplete);
    }

    @BeforeEach
    public void setUp() {
        // รีเซ็ตสถานะและประวัติคำสั่งในแต่ละเทส (หากมีเมธอดดังกล่าวใน UserData)
        onCompleteCalled = false;
        UserData.clearOrderHistory();
    }

    @Test
    public void testPlaceOrderWithoutSelectingService() {
        // ตรวจสอบว่า ComboBox มีค่าเริ่มต้นเป็น "None"
        ComboBox<String> comboBox = lookup("#serviceTypeComboBox").queryAs(ComboBox.class);
        assertEquals("None", comboBox.getValue());

        // คลิกปุ่ม Place Order โดยที่ยังไม่ได้เลือกบริการ (selectedService = "None")
        clickOn("#placeOrderButton");

        // เนื่องจากยังไม่ได้เลือกบริการ ควรจะแสดง Alert (และ callback onComplete ไม่ถูกเรียก)
        assertFalse(onCompleteCalled);
    }

    @Test
    public void testPlaceOrderInsufficientBalance() {
        // ตั้งยอดเงินให้ต่ำกว่า ราคาที่ต้องการสำหรับบริการที่เลือก (Regular Wash = 50)
        UserData.setCurrentBalance(20);

        // เลือกบริการ Regular Wash จาก ComboBox
        clickOn("#serviceTypeComboBox");
        clickOn("Regular Wash");

        // คลิกปุ่ม Place Order
        clickOn("#placeOrderButton");

        // ควรแสดง Alert แจ้ง "Insufficient Balance! Do you want to top-up?"
        // callback onComplete ไม่ควรถูกเรียก
        assertFalse(onCompleteCalled);
    }

    @Test
    public void testPlaceOrderSufficientBalance() {
        // ตั้งยอดเงินให้เพียงพอ
        UserData.setCurrentBalance(200);

        // เลือกบริการ Regular Wash (ราคาคือ 50 และระยะเวลาซัก 120 วินาที)
        clickOn("#serviceTypeComboBox");
        clickOn("Regular Wash");

        // คลิกปุ่ม Place Order
        clickOn("#placeOrderButton");

        // เมื่อวางคำสั่งซักสำเร็จ ควรเรียก onComplete callback
        assertTrue(onCompleteCalled);

        // ตรวจสอบว่า ปุ่ม Place Order และ ComboBox ถูก Disable หลังจากเริ่มซัก
        Button orderButton = lookup("#placeOrderButton").queryButton();
        assertTrue(orderButton.isDisabled());
        ComboBox<String> comboBox = lookup("#serviceTypeComboBox").queryAs(ComboBox.class);
        assertTrue(comboBox.isDisabled());
    }

    @Test
    public void testCancelOrder() {
        // ตั้งยอดเงินให้เพียงพอและเลือกบริการเพื่อวางคำสั่งซัก
        UserData.setCurrentBalance(200);
        clickOn("#serviceTypeComboBox");
        clickOn("Regular Wash");
        clickOn("#placeOrderButton");

        // คลิกปุ่ม Cancel
        clickOn("#cancelButton");

        // จำลองการตอบตกลงใน Alert (TestFX อาจต้องการวิธีการจัดการ Alert ที่เฉพาะ)
        clickOn("YES");

        // หลังจากยกเลิก ควรตรวจสอบว่าเครื่องซัก (machineIndex 0) มีสถานะ AVAILABLE
        assertEquals(MachineStatus.AVAILABLE, MachineManager.getInstance().getStatus(0));
    }

    @Test
    public void testViewOrderHistory() {
        // เคลียร์ประวัติคำสั่งก่อนทดสอบ
        UserData.clearOrderHistory();

        // คลิกปุ่ม View Order History
        clickOn("#viewOrderHistoryButton");

        // ตรวจสอบว่า Stage ใหม่ที่มี title "Order History" ถูกเปิดขึ้น
        // โดยค้นหา Label ที่มีข้อความ "Order History for testUser"
        Labeled historyLabel = lookup("Order History for testUser").queryLabeled();
        assertNotNull(historyLabel);
    }
}
