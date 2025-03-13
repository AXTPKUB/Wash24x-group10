package wash24x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class UserData {
    private static final Map<String, User> users = new HashMap<>();
    private static String currentUser = null;
    private static int remainingTime = 0;
    private static String lastOrderService = null;
    private static Map<Integer, String> orderOwners = new HashMap<>();
private static String tempUsername = null;
private static Map<String, String> userProfiles = new HashMap<>();
private static Map<String, String> userPasswords = new HashMap<>();


    static {
        // Add initial accounts with starting balances
        users.put("admin", new User("admin", "admin123", "admin", 1000));
        users.put("user", new User("user", "qwer", "user", 500));
        users.put("staff1", new User("staff1", "staffpass", "staff", 800));
    }

    // เพิ่มเมธอดสำหรับจัดการเจ้าของออร์เดอร์
public static void setOrderOwner(int machineIndex, String username) {
    orderOwners.put(machineIndex, username);
}

public static String getOrderOwner(int machineIndex) {
    return orderOwners.getOrDefault(machineIndex, null);
}

public static void removeOrderOwner(int machineIndex) {
    orderOwners.remove(machineIndex);
}

    // Register new user with initial balance
    public static boolean register(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(username, password, "user", 0));
        return true;
    }

    public static boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = username;
            return true;
        }
        return false;
    }

    public static String getCurrentUsername() {
        return currentUser;
    }

    public static String getUserRole(String username) {
        User user = users.get(username);
        return (user != null) ? user.getRole() : "guest";
    }

    // Get current balance
    public static int getCurrentBalance() {
        User user = users.get(currentUser);
        return (user != null) ? user.getBalance() : 0;
    }

    // Deduct balance
    public static boolean deductBalance(int amount) {
        User user = users.get(currentUser);
        if (user != null && user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            return true;
        }
        return false;
    }

    // Deduct balance with history recording
    public static boolean deductBalanceWithHistory(int amount, String serviceType) {
        User user = users.get(currentUser);
        if (user != null && user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            String history = String.format("%s[%s] %s: -%d THB (Remaining: %d THB)",
                    currentUser, java.time.LocalDateTime.now(), serviceType, amount, user.getBalance());
            user.addOrderHistory(history);
            return true;
        }
        return false;
    }

    // Get order history
    public static ArrayList<String> getOrderHistory() {
        User user = users.get(currentUser);
        return (user != null) ? user.getOrderHistory() : new ArrayList<>();
    }

    // Clear last order information
    public static void clearLastOrder() {
        lastOrderService = null;
        remainingTime = 0;
    }

    // Add balance
    public static void addBalance(int amount) {
        if (currentUser != null) {
            users.get(currentUser).addBalance(amount);
        }
    }

    // Set and get remaining time
    public static void setRemainingTime(int time) {
        remainingTime = time;
    }
    
    public static int getRemainingTime() {
        return remainingTime;
    }
    
    // Set and get last order service
    public static String getLastOrderService() {
        return lastOrderService;
    }

    public static void setLastOrderService(String service) {
        lastOrderService = service;
    }

    static {
        userProfiles.put("user1", "😀");
        userPasswords.put("user1", "password123");
    }

    public static String getUserProfile(String username) {
        return userProfiles.getOrDefault(username, "👤");
    }

    public static boolean verifyPassword(String username, String password) {
        return userPasswords.containsKey(username) && userPasswords.get(username).equals(password);
    }

    public static void updateProfile(String username, String newProfile) {
        userProfiles.put(username, newProfile);
    }

    public static void updateUsername(String oldUsername, String newUsername) {
        if (userProfiles.containsKey(oldUsername)) {
            String profile = userProfiles.remove(oldUsername);
            userProfiles.put(newUsername, profile);
            
            String password = userPasswords.remove(oldUsername);
            userPasswords.put(newUsername, password);
        }
    }
    public static void updatePassword(String username, String newPassword) {
        if (userPasswords.containsKey(username)) {
            userPasswords.put(username, newPassword);
        }
    }
    
}

// User class
class User {
    private final String username;
    private final String password;
    private final String role;
    private int balance;
    private final ArrayList<String> orderHistory = new ArrayList<>();
    

    public User(String username, String password, String role, int initialBalance) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.balance = initialBalance;
    }
    

    public String getuser() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void addOrderHistory(String history) {
        orderHistory.add(history);
    }

    public ArrayList<String> getOrderHistory() {
        return orderHistory;
    }

    public void addBalance(int amount) {
        balance += amount;
    }
}