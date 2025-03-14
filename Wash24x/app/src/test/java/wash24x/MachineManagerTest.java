package wash24x;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MachineManagerTest {

    @Test
    public void testSingletonInstance() {
        // ทดสอบว่า getInstance() คืนค่า instance เดียวกันทุกครั้ง (Singleton Pattern)
        MachineManager instance1 = MachineManager.getInstance();
        MachineManager instance2 = MachineManager.getInstance();
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2, "ทั้งสอง instance ควรเป็น instance เดียวกัน");
    }

    @Test
    public void testInitialMachineStatuses() {
        // ทดสอบว่าหลังจากสร้าง instance ของ MachineManager
        // สถานะของทุกเครื่องควรเป็น AVAILABLE (เริ่มต้น)
        MachineManager manager = MachineManager.getInstance();
        for (int i = 0; i < 12; i++) {
            assertEquals(MachineStatus.AVAILABLE, manager.getStatus(i),
                    "เครื่องที่ index " + i + " ควรมีสถานะ AVAILABLE เริ่มต้น");
        }
    }

    @Test
    public void testSetAndGetMachineStatus() {
        // ทดสอบการเปลี่ยนแปลงสถานะของเครื่องซักผ้า
        MachineManager manager = MachineManager.getInstance();

        // เปลี่ยนสถานะเครื่องที่ index 0 เป็น WORKING แล้วตรวจสอบ
        manager.setStatus(0, MachineStatus.WORKING);
        assertEquals(MachineStatus.WORKING, manager.getStatus(0),
                "สถานะของเครื่องที่ index 0 ควรเปลี่ยนเป็น WORKING");

        // เปลี่ยนสถานะเครื่องที่ index 5 เป็น FINISH แล้วตรวจสอบ
        manager.setStatus(5, MachineStatus.FINISH);
        assertEquals(MachineStatus.FINISH, manager.getStatus(5),
                "สถานะของเครื่องที่ index 5 ควรเปลี่ยนเป็น FINISH");
    }

    @Test
    public void testSetStatusOutOfRange() {
        // ทดสอบการเข้าถึง index ที่อยู่นอกขอบเขต (ควรโยน ArrayIndexOutOfBoundsException)
        MachineManager manager = MachineManager.getInstance();

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            manager.getStatus(12); // index 12 ไม่อยู่ในช่วง 0-11
        });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            manager.setStatus(-1, MachineStatus.WORKING); // index -1 ไม่ถูกต้อง
        });
    }
}
