package paystation.domain;

import java.util.*;

/**
 * Implementation of the pay station.
 *
 * Responsibilities:
 *
 * 1) Accept payment; 
 * 2) Calculate parking time based on payment; 
 * 3) Know earning, parking time bought; 
 * 4) Issue receipts; 
 * 5) Handle buy and cancel events.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
public class PayStationImpl implements PayStation {
    
    private int insertedSoFar;
    private int timeBought;
    private int totalMoney;
    private int numNickels = 0;
    private int numDimes = 0;
    private int numQuarters = 0;
    private Map<Integer, Integer> coinCount = new HashMap<>();

    @Override
    public void addPay(int coinValue)
            throws IllegalCoinException {
        switch (coinValue) {
            case 5: 
                coinCount.put(5, ++numNickels);
                break;
            case 10: 
                coinCount.put(10, ++numDimes);
                break;
            case 25: 
                coinCount.put(25, ++numQuarters);
                break;
            default:
                throw new IllegalCoinException("Invalid coin: " + coinValue);
        }
        insertedSoFar += coinValue;
        timeBought = insertedSoFar / 5 * 2;
    }

    @Override
    public int readDisplay() {
        return timeBought;
    }

    @Override
    public Receipt buy() {
        Receipt r = new ReceiptImpl(timeBought);
        totalMoney += insertedSoFar;
        reset();
        return r;
    }

    @Override
    public Map<Integer, Integer> cancel() {
        HashMap<Integer, Integer> tempMap = new HashMap<>();
        tempMap.putAll(coinCount);
        reset();
        return tempMap;
    }
    
    private void reset() {
        timeBought = insertedSoFar = 0;
        coinCount.clear();
    }
    
    @Override
    public int empty(){
        int temp = totalMoney;
        totalMoney = 0;
        return temp;
    }
}
