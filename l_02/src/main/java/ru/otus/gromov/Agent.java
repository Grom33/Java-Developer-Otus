package ru.otus.gromov;

import java.lang.instrument.Instrumentation;

public class Agent {
    private static volatile Instrumentation globalInstrumentation;

    public static void premain(final String agentArgs, Instrumentation inst) {
        globalInstrumentation = inst;
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        globalInstrumentation = inst;
    }

    public static long getObjectSize(final Object object) {
        if (globalInstrumentation == null) {
            throw new IllegalStateException("Agent not initialized.");
        }
        return globalInstrumentation.getObjectSize(object);
    }
}
