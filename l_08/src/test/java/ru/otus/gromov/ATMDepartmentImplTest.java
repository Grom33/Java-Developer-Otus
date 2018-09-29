package ru.otus.gromov;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ATMDepartmentImplTest {
    private ATMDepartment atmDepartment;
    private Atm ATM_1;
    private Atm ATM_2;
    private static final int DEPARTMENT_INITIAL_BALANCE = 11_650_000;

    @Before
    public void setUp() {
        atmDepartment = new ATMDepartmentImpl();

        Map<Bill, Cassette> ATM_CASSETTES_1 = new HashMap<>();
        ATM_CASSETTES_1.put(Bill.FIVE_HUNDRED, new CassetteImpl(Bill.FIVE_HUNDRED, 1000));
        ATM_CASSETTES_1.put(Bill.HUNDRED, new CassetteImpl(Bill.HUNDRED, 1000));
        ATM_CASSETTES_1.put(Bill.THOUSAND, new CassetteImpl(Bill.THOUSAND, 1000));
        ATM_CASSETTES_1.put(Bill.FIVE_THOUSAND, new CassetteImpl(Bill.FIVE_THOUSAND, 1000));

        Map<Bill, Cassette> ATM_CASSETTES_2 = new HashMap<>();
        ATM_CASSETTES_2.put(Bill.FIVE_HUNDRED, new CassetteImpl(Bill.FIVE_HUNDRED, 500));
        ATM_CASSETTES_2.put(Bill.HUNDRED, new CassetteImpl(Bill.HUNDRED, 500));
        ATM_CASSETTES_2.put(Bill.THOUSAND, new CassetteImpl(Bill.THOUSAND, 500));
        ATM_CASSETTES_2.put(Bill.FIVE_THOUSAND, new CassetteImpl(Bill.FIVE_THOUSAND, 500));

        Map<Bill, Cassette> ATM_CASSETTES_3 = new HashMap<>();
        ATM_CASSETTES_3.put(Bill.FIVE_HUNDRED, new CassetteImpl(Bill.FIVE_HUNDRED, 400));//200 000
        ATM_CASSETTES_3.put(Bill.HUNDRED, new CassetteImpl(Bill.HUNDRED, 500));//50 000
        ATM_CASSETTES_3.put(Bill.THOUSAND, new CassetteImpl(Bill.THOUSAND, 1000));// 1000 000
        ATM_CASSETTES_3.put(Bill.FIVE_THOUSAND, new CassetteImpl(Bill.FIVE_THOUSAND, 100));//500 000


        ATM_1 = new AtmImpl(1, ATM_CASSETTES_1);
        ATM_2 = new AtmImpl(2, ATM_CASSETTES_2);
        Atm ATM_3 = new AtmImpl(3, ATM_CASSETTES_3);

        atmDepartment.addATM(ATM_1);
        atmDepartment.addATM(ATM_2);
        atmDepartment.addATM(ATM_3);
    }

    @Test
    public void getBalance() {

        Assert.assertEquals("Get total balance department",
                DEPARTMENT_INITIAL_BALANCE,
                atmDepartment.getBalance());
    }

    @Test
    public void reloadAllAtm() {
        int requestMoney = 1_000_000;
        ATM_1.withdraw(requestMoney);
        Assert.assertEquals("Get total balance department after withdraw",
                (DEPARTMENT_INITIAL_BALANCE - requestMoney),
                atmDepartment.getBalance());
        atmDepartment.reloadAllAtm();
        Assert.assertEquals("Get total balance department after reloading",
                (DEPARTMENT_INITIAL_BALANCE),
                atmDepartment.getBalance());
    }

    @Test
    public void reloadAtm() {
        int requestMoney = 1_000_000;
        ATM_1.withdraw(requestMoney);
        ATM_2.withdraw(requestMoney);
        Assert.assertEquals("Get total balance department after withdraw",
                (DEPARTMENT_INITIAL_BALANCE - (requestMoney * 2)),
                atmDepartment.getBalance());
        atmDepartment.reloadAtm(ATM_1.getId());
        Assert.assertEquals("Get total balance department after reloading",
                (DEPARTMENT_INITIAL_BALANCE - requestMoney),
                atmDepartment.getBalance());
    }
}