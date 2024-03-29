/**
 * Testcases for the Pay Station system.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
package paystation.domain;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class PayStationImplTest {

    PayStation ps;

    @Before
    public void setup() {
        ps = new PayStationImpl();
    }

    /**
     * Entering 5 cents should make the display report 2 minutes parking time.
     */
    @Test
    public void shouldDisplay2MinFor5Cents()
            throws IllegalCoinException {
        ps.addPay(5);
        assertEquals("Should display 2 min for 5 cents",
                2, ps.readDisplay());
    }

    /**
     * Entering 25 cents should make the display report 10 minutes parking time.
     */
    @Test
    public void shouldDisplay10MinFor25Cents() throws IllegalCoinException {
        ps.addPay(25);
        assertEquals("Should display 10 min for 25 cents",
                10, ps.readDisplay());
    }

    /**
     * Verify that illegal coin values are rejected.
     */
    @Test(expected = IllegalCoinException.class)
    public void shouldRejectIllegalCoin() throws IllegalCoinException {
        ps.addPay(17);
    }

    /**
     * Entering 10 and 25 cents should be valid and return 14 minutes parking
     */
    @Test
    public void shouldDisplay14MinFor10And25Cents()
            throws IllegalCoinException {
        ps.addPay(10);
        ps.addPay(25);
        assertEquals("Should display 14 min for 10+25 cents",
                14, ps.readDisplay());
    }

    /**
     * Buy should return a valid receipt of the proper amount of parking time
     */
    @Test
    public void shouldReturnCorrectReceiptWhenBuy()
            throws IllegalCoinException {
        ps.addPay(5);
        ps.addPay(10);
        ps.addPay(25);
        Receipt receipt;
        receipt = ps.buy();
        assertNotNull("Receipt reference cannot be null",
                receipt);
        assertEquals("Receipt value must be 16 min.",
                16, receipt.value());
    }

    /**
     * Buy for 100 cents and verify the receipt
     */
    @Test
    public void shouldReturnReceiptWhenBuy100c()
            throws IllegalCoinException {
        ps.addPay(10);
        ps.addPay(10);
        ps.addPay(10);
        ps.addPay(10);
        ps.addPay(10);
        ps.addPay(25);
        ps.addPay(25);

        Receipt receipt;
        receipt = ps.buy();
        assertEquals(40, receipt.value());
    }

    /**
     * Verify that the pay station is cleared after a buy scenario
     */
    @Test
    public void shouldClearAfterBuy()
            throws IllegalCoinException {
        ps.addPay(25);
        ps.buy(); // I do not care about the result
        // verify that the display reads 0
        assertEquals("Display should have been cleared",
                0, ps.readDisplay());
        // verify that a following buy scenario behaves properly
        ps.addPay(10);
        ps.addPay(25);
        assertEquals("Next add payment should display correct time",
                14, ps.readDisplay());
        Receipt r = ps.buy();
        assertEquals("Next buy should return valid receipt",
                14, r.value());
        assertEquals("Again, display should be cleared",
                0, ps.readDisplay());
    }

    /**
     * Verify that cancel clears the pay station
     * @throws paystation.domain.IllegalCoinException
     */
    @Test
    public void shouldClearAfterCancel()
            throws IllegalCoinException {
        ps.addPay(10);
        ps.cancel();
        assertEquals("Cancel should clear display",
                0, ps.readDisplay());
        ps.addPay(25);
        assertEquals("Insert after cancel should work",
                10, ps.readDisplay());
    }
    
    /**
     * Call to empty returns the total amount entered.
     */
    @Test
    public void emptyReturnsAmountEntered()
            throws IllegalCoinException {
        ps.addPay(5);
        ps.addPay(5);
        ps.addPay(10);
        ps.addPay(25);
        ps.buy();
        assertEquals("Return 45 and then empty",
                45, ps.empty());
    }

    /**
     *  Verify cancel entry does not add to the amount returned by empty.
     * @throws IllegalCoinException
     */
    @Test
    public void cancelDoesNotAddToEmpty()
            throws IllegalCoinException {
        ps.addPay(5);
        ps.addPay(5);
        ps.addPay(5);
        ps.addPay(10);
        ps.addPay(10);
        ps.addPay(25);
        ps.buy();
        ps.addPay(5);
        ps.addPay(10);
        ps.addPay(25);
        ps.cancel();
        assertEquals("Return 60 and then empty",
                     60, ps.empty());
    }
    
    /*
    * Call to empty resets the total to zero.
    */
    @Test
    public void emptyResetsZero()
            throws IllegalCoinException {
        ps.addPay(25);
        ps.buy();
        assertEquals("Return 25 and then empty",
                25, ps.empty());
        ps.addPay(25);
        ps.buy();
        ps.empty();
        assertEquals("Return 0",
                0, ps.empty());
    }
    
    /*
    * Call to cancel returns a map containing one coin entered.
    */
    @Test
    public void cancelReturnsOneCoinMap()
            throws IllegalCoinException {
        ps.addPay(5);
        Map<Integer,Integer> test = new HashMap<>();
        test = ps.cancel();
        assertTrue(test.containsKey(5));
    }
    
    /*
     * Call to cancel returns a map containing a mixture of coins entered.
     */
    @Test
    public void cancelReturnsMultipleCoinMap()
    throws IllegalCoinException {
        ps.addPay(5);
        ps.addPay(10);
        ps.addPay(25);
        Map<Integer,Integer> test = new HashMap<>();
        test = ps.cancel();
        assertTrue(test.containsKey(5));
        assertTrue(test.containsKey(10));
        assertTrue(test.containsKey(25));
    }
    
     /*
     * Call to cancel returns a map that does not contain a key for a coin not entered.
     */
    @Test
    public void cancelMapKey()
    throws IllegalCoinException {
        ps.addPay(10);
        ps.addPay(25);
        Map<Integer,Integer> test = new HashMap<>();
        test = ps.cancel();
        assertFalse(test.containsKey(15));
    }
    
     /*
    * Call to cancel clears the map.
    */
    @Test
    public void cancelClearsMap()
            throws IllegalCoinException {
        ps.addPay(5);
        ps.addPay(10);
        ps.addPay(25);
        ps.cancel();
        Map total = ps.cancel();
        Map clearMap = new HashMap<>();
        assertEquals(total, clearMap);
    }
    
    /*
    * Call to buy clears the map.
    */
    @Test
    public void buyClearsMap()
            throws IllegalCoinException {
        ps.addPay(5);
        ps.addPay(10);
        ps.addPay(25);
        ps.buy();
        Map total = ps.cancel();
        Map clearMap = new HashMap<>();
        assertEquals(total, clearMap);
    }

}
