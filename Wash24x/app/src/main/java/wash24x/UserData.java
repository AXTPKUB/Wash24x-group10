package wash24x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserData {
    private static final Map<String, User> users = new HashMap<>(); // เก็บข้อมูลผู้ใช้ทั้งหมด
    private static final Map<String, String> userProfiles = new HashMap<>(); // เก็บอิโมจิโปรไฟล์ของผู้ใช้
    private static final Map<String, String> userPasswords = new HashMap<>(); // เก็บรหัสผ่านของผู้ใช้
    private static final Map<Integer, String> orderOwners = new HashMap<>(); // เก็บเจ้าของออร์เดอร์
    private static ObservableList<String> reportList = FXCollections.observableArrayList(); // รายงานปัญหา

    private static String currentUser = null; // ผู้ใช้ที่กำลังล็อกอิน
    private static String currentUserProfile = "👤"; // ค่าเริ่มต้นของโปรไฟล์
    private static int remainingTime = 0; // เวลาที่เหลือในคำสั่งล่าสุด
    private static String lastOrderService = null; // บริการล่าสุดที่ใช้

    static {
        // เพิ่มบัญชีผู้ใช้ตัวอย่าง
        users.put("admin", new User("admin", "admin123", "admin", 1000));
        users.put("user", new User("user", "qwer", "user", 500));
        users.put("staff1", new User("staff1", "staffpass", "staff", 800));

        userProfiles.put("user1", "😀");
        userPasswords.put("user1", "password123");
    }

    /*** เมธอดเกี่ยวกับผู้ใช้ ***/
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

    public static void setCurrentUsername(String username) {
        currentUser = username;
    }

    public static boolean userExists(String username) {
        return users.containsKey(username);
    }

    /*** เมธอดเกี่ยวกับบัญชีผู้ใช้ ***/
    public static String getUserProfile(String username) {
        return userProfiles.getOrDefault(username, "👤");
    }

    public static void updateProfile(String username, String newProfile) {
        userProfiles.put(username, newProfile);
    }

    public static boolean updateUsername(String oldUsername, String newUsername) {
        if (users.containsKey(newUsername)) {
            return false;
        }
        if (users.containsKey(oldUsername)) {
            User user = users.remove(oldUsername);
            users.put(newUsername, user);
            userProfiles.put(newUsername, userProfiles.remove(oldUsername));
            userPasswords.put(newUsername, userPasswords.remove(oldUsername));
            return true;
        }
        return false;
    }

    public static boolean verifyPassword(String username, String password) {
        return userPasswords.containsKey(username) && userPasswords.get(username).equals(password);
    }

    public static void updatePassword(String username, String newPassword) {
        if (userPasswords.containsKey(username)) {
            userPasswords.put(username, newPassword);
        }
    }

    /*** เมธอดเกี่ยวกับยอดเงิน ***/
    public static int getCurrentBalance() {
        User user = users.get(currentUser);
        return (user != null) ? user.getBalance() : 0;
    }

    public static void addBalance(int amount) {
        if (currentUser != null) {
            users.get(currentUser).addBalance(amount);
        }
    }

    public static boolean deductBalance(int amount) {
        User user = users.get(currentUser);
        if (user != null && user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            return true;
        }
        return false;
    }

    /*** เมธอดเกี่ยวกับออร์เดอร์ ***/
    public static void setOrderOwner(int machineIndex, String username) {
        orderOwners.put(machineIndex, username);
    }

    public static String getOrderOwner(int machineIndex) {
        return orderOwners.getOrDefault(machineIndex, null);
    }

    public static void removeOrderOwner(int machineIndex) {
        orderOwners.remove(machineIndex);
    }

    public static void setRemainingTime(int time) {
        remainingTime = time;
    }

    public static int getRemainingTime() {
        return remainingTime;
    }

    public static void setLastOrderService(String service) {
        lastOrderService = service;
    }

    public static String getLastOrderService() {
        return lastOrderService;
    }

    /*** เมธอดเกี่ยวกับรายงาน ***/
    public static void addReport(String report) {
        reportList.add(report);
    }

    public static ObservableList<String> getReportList() {
        return reportList;
    }

    public static boolean deductBalanceWithHistory(int price, String selectedService) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deductBalanceWithHistory'");
    }

    public static void clearLastOrder() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearLastOrder'");
    }

    public static List<String> getOrderHistory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrderHistory'");
    }

    public static String getUserRole(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserRole'");
    }

    public static String getBalance() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalance'");
    }

    public static String getLastCanceledOrder(int machineIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLastCanceledOrder'");
    }

    public static void setLastCanceledOrder(int machineIndex, String lastOrderService2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setLastCanceledOrder'");
    }

    public static String getCurrentUserProfile() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentUserProfile'");
    }

    public static void clearUsers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearUsers'");
    }

    public static BooleanSupplier isUserRegistered(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isUserRegistered'");
    }

    public static void clearOrderHistory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearOrderHistory'");
    }

    public static void setCurrentBalance(int i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCurrentBalance'");
    }
}

/*** คลาส User สำหรับเก็บข้อมูลผู้ใช้ ***/
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

    public String getUsername() {
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

    public void addBalance(int amount) {
        balance += amount;
    }

    public void addOrderHistory(String history) {
        orderHistory.add(history);
    }

    public ArrayList<String> getOrderHistory() {
        return orderHistory;
    }
}