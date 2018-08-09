package ru.otus.gromov;

public class Assert {
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";

    private Assert() {
    }

    public static void assertEquals(Object expected, Object actual) {
        if (expected == null) {
            System.out.print(RED + String.format("%-15s |", " Ignored!"));
            return;
        }
        if (expected.equals(actual)) {
            System.out.print(GREEN + String.format("%-15s |", " Passed"));
        } else {
            System.out.print(RED + String.format("%-15s |", " Failed"));
        }

    }
}
