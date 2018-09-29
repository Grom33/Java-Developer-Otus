package ru.otus.gromov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.gromov.exception.InvalidAmountRequestException;
import ru.otus.gromov.exception.NotEnoughBillException;

import java.util.HashMap;
import java.util.Map;

public class AtmImpl implements Atm {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final int id;
    private Map<Bill, Cassette> cassettes;
    private final Map<Bill, Cassette> initialState;

    public AtmImpl(int id, Map<Bill, Cassette> initialState) {
        this.cassettes = new HashMap<>();
        this.id = id;

        this.initialState = new HashMap<>();
        initialState.forEach((k, v) -> this.initialState.put(k, new CassetteImpl(v)));
        this.cassettes = new HashMap<>(initialState);
    }

    @Override
    public void deposit(Map<Bill, Integer> moneyBundle) {
        log.info("Deposit money, incoming bundle {}", moneyBundle);
        moneyBundle.forEach((key, value) -> cassettes.get(key).deposit(value));
    }

    @Override
    public Map<Bill, Integer> withdraw(int moneyRequest) {
        log.info("Withdraw money, request amount {}", moneyRequest);
        requestAmountValidation(moneyRequest);
        divideToBills(moneyRequest)
                .forEach((key, value) -> cassettes.get(key).withdraw(value));
        return divideToBills(moneyRequest);
    }

    @Override
    public int getBalance() {
        log.info("Get balance");
        return cassettes.entrySet().stream()
                .mapToInt(i -> i.getValue().getBalance())
                .sum();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void reload() {
        log.info("Reloading ATM# {}", id);
        this.cassettes = new HashMap<>();
        this.initialState.forEach((k, v) -> this.cassettes.put(k, new CassetteImpl(v)));
    }

    private void requestAmountValidation(int moneyRequest) {
        if ((moneyRequest % Bill.getTheSmallestDenomination()) > 0)
            throw new InvalidAmountRequestException(moneyRequest);
        if (moneyRequest > getBalance())
            throw new NotEnoughBillException(moneyRequest);
    }

    private Map<Bill, Integer> divideToBills(int moneyRequest) {
        log.info("Money request division");
        int request = moneyRequest;
        Map<Bill, Integer> dividedBundle = new HashMap<>();
        for (Bill bill : Bill.values()) {
            int cassetteBalance = cassettes.get(bill).getBalance();
            if (request / bill.getDenomination() == 0 || cassetteBalance == 0) continue;
            if (request <= cassetteBalance) {
                dividedBundle.put(bill, request / bill.getDenomination());
                request %= bill.getDenomination();
            } else {
                dividedBundle.put(bill, cassettes.get(bill).getBillsCount());
                request -= cassetteBalance;
            }
            if (request == 0) break;
        }
        log.info("Division result {}", dividedBundle);
        return dividedBundle;
    }
}
