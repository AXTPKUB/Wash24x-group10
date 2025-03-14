package wash24x;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class UserDataTest {

    @BeforeEach
    public void setUp() {
        // Reset UserData before each test
        UserData.setCurrentUsername("TestUser");
        UserData.setCurrentBalance(500);
        UserData.clearOrderHistory();
    }
    
    @Test
    public void testDeductBalanceWithSufficientFunds() {
        boolean result = UserData.deductBalanceWithHistory(200, "Deluxe Wash");
        
        assertTrue(result, "Deduction should succeed with sufficient balance");
        assertEquals(300, UserData.getCurrentBalance(), "Balance should be reduced by 200");
        
        List<String> history = UserData.getOrderHistory();
        assertEquals(1, history.size(), "History should have one entry");
        assertTrue(history.get(0).contains("Deluxe Wash"), "History should contain service name");
        assertTrue(history.get(0).contains("200 THB"), "History should contain price");
    }
    
    @Test
    public void testDeductBalanceWithInsufficientFunds() {
        UserData.setCurrentBalance(100);
        boolean result = UserData.deductBalanceWithHistory(200, "Deluxe Wash");
        
        assertFalse(result, "Deduction should fail with insufficient balance");
        assertEquals(100, UserData.getCurrentBalance(), "Balance should remain unchanged");
        
        List<String> history = UserData.getOrderHistory();
        assertTrue(history.isEmpty(), "History should be empty");
    }
    
    @Test
    public void testOrderOwner() {
        // Set and get order owner
        UserData.setOrderOwner(0, "TestUser");
        assertEquals("TestUser", UserData.getOrderOwner(0), "Order owner should be set");
        
        // Remove order owner
        UserData.removeOrderOwner(0);
        assertNull(UserData.getOrderOwner(0), "Order owner should be removed");
    }
    
    @Test
    public void testLastOrderService() {
        UserData.setLastOrderService("Dry Clean");
        assertEquals("Dry Clean", UserData.getLastOrderService(), "Last order service should be set");
        
        UserData.clearLastOrder();
        assertNull(UserData.getLastOrderService(), "Last order service should be cleared");
    }
    
    @Test
    public void testRemainingTime() {
        UserData.setRemainingTime(120);
        assertEquals(120, UserData.getRemainingTime(), "Remaining time should be set");
    }
}
