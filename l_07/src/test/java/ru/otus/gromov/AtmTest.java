package ru.otus.gromov;

import org.junit.Before;
import org.junit.Test;
import ru.otus.gromov.exception.InvalidAmountRequestException;
import ru.otus.gromov.exception.NotEnoughBillException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AtmTest {
    private static Atm ATM;
    private static final int EXPECTED_BALANCE_OF_FULL_ATM = 6_600_000;
    private static final int EXPECTED_BALANCE_AFTER_WITHDRAW = 6_594_400;
    private static final int EXPECTED_BALANCE_AFTER_DEPOSIT = 6_610_500;
    private static Map<Bill, Integer> EXPECTED_DIVIDED_BUNDLE;
    private static final int REQUEST_MONEY_AMOUNT = 5600;
    private static Map<Bill, Integer> BUNDLE_TO_DEPOSIT;
    private static final int BAD_REQUEST_MONEY_AMOUNT = 116;
    private static final int EXCEED_REQUEST_MONEY_AMOUNT = 6_601_000;

    static {
        EXPECTED_DIVIDED_BUNDLE = new HashMap<>();
        EXPECTED_DIVIDED_BUNDLE.put(Bill.FIVE_THOUSAND, 1);
        EXPECTED_DIVIDED_BUNDLE.put(Bill.FIVE_HUNDRED, 1);
        EXPECTED_DIVIDED_BUNDLE.put(Bill.HUNDRED, 1);

        BUNDLE_TO_DEPOSIT = new HashMap<>();
        BUNDLE_TO_DEPOSIT.put(Bill.HUNDRED, 5);
        BUNDLE_TO_DEPOSIT.put(Bill.FIVE_THOUSAND, 2);
    }

    @Before
    public void setup() {
        ATM = new AtmImpl();
    }

    @Test
    public void deposit() {
        ATM.deposit(BUNDLE_TO_DEPOSIT);
        assertEquals("Deposit money to ATM", EXPECTED_BALANCE_AFTER_DEPOSIT, ATM.getBalance());
    }

    @Test
    public void withdraw() {
        ATM.withdraw(REQUEST_MONEY_AMOUNT);
        assertEquals("Withdraw money from ATM", EXPECTED_BALANCE_AFTER_WITHDRAW, ATM.getBalance());
    }

    @Test
    public void getBalance() {
        assertEquals("Get balance from ATM", EXPECTED_BALANCE_OF_FULL_ATM, ATM.getBalance());
    }

    @Test
    public void minimalDivisionToBillBundle() {
        assertEquals("Divide money bundle to minimal bills count", EXPECTED_DIVIDED_BUNDLE, ATM.withdraw(REQUEST_MONEY_AMOUNT));
    }

    @Test(expected = InvalidAmountRequestException.class)
    public void nonRepaymentRequest() {
        ATM.withdraw(BAD_REQUEST_MONEY_AMOUNT);
    }

    @Test(expected = NotEnoughBillException.class)
    public void exceedRequestMoneyAmount() {
        ATM.withdraw(EXCEED_REQUEST_MONEY_AMOUNT);
    }

}