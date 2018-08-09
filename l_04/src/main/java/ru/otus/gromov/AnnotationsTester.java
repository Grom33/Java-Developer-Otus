package ru.otus.gromov;

import ru.otus.gromov.annotation.After;
import ru.otus.gromov.annotation.Before;
import ru.otus.gromov.annotation.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;


@SuppressWarnings("unchecked")
class AnnotationsTester {
    private static final String YELLOW = "\u001B[33m";
    private static final String HIGH_INTENSITY = "\u001B[1m";

    AnnotationsTester() {
    }

    <T> void runTest(Class<T> clazz) {
        Map<Annotation, List<Method>> annotationMap = (Map<Annotation, List<Method>>) ReflectionHelper.getAnnotationsMap(clazz);
        if (annotationMap == null) {
            System.out.println("Found no annotations in " + clazz.getSimpleName());
            return;
        }

        var before = new Object() {
            Object[] Methods = new Method[0];
        };
        var after = new Object() {
            Object[] Methods = new Method[0];
        };

        Object[] testMethods = new Method[0];

        for (Annotation anno : annotationMap.keySet()) {
            Object[] Methods = getMethodsArray(annotationMap, anno);
            if (anno instanceof Before) before.Methods = Methods;
            if (anno instanceof Test) testMethods = Methods;
            if (anno instanceof After) after.Methods = Methods;
        }

        if (testMethods.length < 1) {
            System.out.println("Found no @Test annotations in " + clazz.getSimpleName());
            return;
        }

        System.out.println(HIGH_INTENSITY + YELLOW + String.format("  %-45s", "_").replace(" ", "_"));
        System.out.println(String.format("|%-15s |", "  Test") + String.format("%-15s |", "  result") + String.format("%-10s |", "  duration"));
        System.out.println(String.format("|%-45s|", "_").replace(" ", "_"));

        Arrays.stream(testMethods)
                .forEach(test -> ExecuteTestAround(
                        before.Methods,
                        after.Methods,
                        (Method) test,
                        ReflectionHelper.instantiate(clazz))
                );
        System.out.println(String.format("|%-45s|", "_").replace(" ", "_"));
    }

    private <T> void ExecuteTestAround(Object[] beforeMethods, Object[] afterMethods, Method testMethod, T instance) {
        System.out.print(YELLOW + String.format("|%-15s |", testMethod.getName()));
        long startTime = System.currentTimeMillis();

        Arrays.stream(new Object[][]{beforeMethods, new Object[]{testMethod}, afterMethods})
                .forEach(methods -> callMethods(methods, instance));

        System.out.println(YELLOW + String.format("%-10s |", System.currentTimeMillis() - startTime + " ms"));
    }

    private <T> void callMethods(Object[] Methods, T instance) {
        Arrays.stream(Methods)
                .forEach(method -> ReflectionHelper.callMethod(instance, ((Method) method).getName()));
    }

    private Object[] getMethodsArray(Map<Annotation, List<Method>> annotationMap, Annotation annotation) {
        List methods = annotationMap.get(annotation);
        return (methods != null) ? methods.toArray() : new Object[0];
    }
}
