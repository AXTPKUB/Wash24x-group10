package wash24x;

public class MachineManager {
    private static final int NUM_MACHINES = 12; // กำหนดจำนวนเครื่องซักผ้าเป็นค่าคงที่
    private static MachineManager instance; // ใช้ Singleton Pattern เพื่อให้มีเพียงอินสแตนซ์เดียวของคลาสนี้
    private final MachineStatus[] machineStatuses; // เก็บสถานะของเครื่องซักผ้าแต่ละเครื่อง

    // คอนสตรักเตอร์แบบ private เพื่อป้องกันการสร้างอินสแตนซ์จากภายนอก
    private MachineManager() {
        machineStatuses = new MachineStatus[NUM_MACHINES];

        // กำหนดค่าเริ่มต้นให้ทุกเครื่องซักผ้ามีสถานะ AVAILABLE
        for (int i = 0; i < NUM_MACHINES; i++) {
            machineStatuses[i] = MachineStatus.AVAILABLE;
        }
    }

    // เมธอดสำหรับเรียกใช้อินสแตนซ์เดียวของ MachineManager (Singleton)
    public static MachineManager getInstance() {
        if (instance == null) {
            instance = new MachineManager();
        }
        return instance;
    }

    // เมธอดสำหรับดึงสถานะของเครื่องซักผ้า ณ index ที่กำหนด
    public MachineStatus getStatus(int index) {
        return machineStatuses[index];
    }

    // เมธอดสำหรับเปลี่ยนสถานะของเครื่องซักผ้า ณ index ที่กำหนด
    public void setStatus(int index, MachineStatus status) {
        machineStatuses[index] = status;
    }
}
