package ru.otus.gromov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ATMDepartmentImpl implements ATMDepartment {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Map<Integer, Atm> atmPool;


    public ATMDepartmentImpl() {
        this.atmPool = new HashMap<>();
    }

    @Override
    public int getBalance() {
        log.info("Get balance from all ATM ");
        return atmPool.values().stream()
                .mapToInt(Atm::getBalance)
                .sum();
    }

    @Override
    public void reloadAllAtm() {
        log.info("Reload all ATM ");
        atmPool.values().forEach(Atm::reload);
    }

    @Override
    public void reloadAtm(int id) {
        log.info("Reload ATM #{}", id);
        atmPool.get(id).reload();
    }

    @Override
    public void addATM(Atm atm) {
        log.info("Add ATM #{} to department pool ", atm.getId());
        atmPool.put(atm.getId(), atm);
    }
}
