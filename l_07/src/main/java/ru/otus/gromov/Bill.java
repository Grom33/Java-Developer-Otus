package ru.otus.gromov;

public enum Bill {
    FIVE_THOUSAND(5000),
    THOUSAND(1000),
    FIVE_HUNDRED(500),
    HUNDRED(100);

    int denomination;

    Bill(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }

    public static int getTheSmallestDenomination() {
        return HUNDRED.getDenomination();
    }
}
