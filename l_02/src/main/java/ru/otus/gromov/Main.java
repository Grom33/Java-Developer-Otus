package ru.otus.gromov;

import ru.otus.gromov.util.ObjFactory;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"RedundantStringConstructorCall", "InfiniteLoopStatement"})
public class Main {

    public static void main(String... args) throws IllegalAccessException {
        System.out.println("pid: " + ManagementFactory.getRuntimeMXBean().getName());
        ObjFactory objFactory = new ObjFactory();
        for (int i = 0; i <= 18; i++) {
            mesuringSizeOfObject(objFactory.getSomeObject());
        }
    }

    private static void mesuringSizeOfObject(Object object) throws IllegalAccessException {
        System.out.println(String.format("Size of %s is %s byte", object.getClass().getSimpleName(), calculateSizOfObject(object)));
    }

    private static long calculateSizOfObject(Object obj) throws IllegalAccessException {
        long result = 0L;
        List<Field> fields = getFields(obj);
        result += Agent.getObjectSize(obj);
        for (Field f : fields) {
            f.setAccessible(true);
            result += calculateSizOfObject(f.get(obj));
        }
        return result;
    }

    private static <T> List getFields(T t) {
        List<Field> fields = new ArrayList<>();
        Class clazz = t.getClass();
        while (clazz != Object.class) {
            fields.addAll(Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .filter(field -> !field.getType().isPrimitive())
                    .collect(Collectors.toList()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
}
