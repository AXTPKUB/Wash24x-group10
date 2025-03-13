package wash24x;

public class MachineManager {
    private static final int NUM_MACHINES = 12;
    private static MachineManager instance;
    private final MachineStatus[] machineStatuses;

    private MachineManager() {
        machineStatuses = new MachineStatus[NUM_MACHINES];
        for (int i = 0; i < NUM_MACHINES; i++) {
            machineStatuses[i] = MachineStatus.AVAILABLE;
        }
    }

    public static MachineManager getInstance() {
        if (instance == null) {
            instance = new MachineManager();
        }
        return instance;
    }

    public MachineStatus getStatus(int index) {
        return machineStatuses[index];
    }

    public void setStatus(int index, MachineStatus status) {
        machineStatuses[index] = status;
    }
}
