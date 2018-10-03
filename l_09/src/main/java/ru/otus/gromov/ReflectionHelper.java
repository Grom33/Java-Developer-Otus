package ru.otus.gromov;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;


public class ReflectionHelper {

    private ReflectionHelper() {
    }

    public static <T> List getFields(T t) {
        List<Field> fields = new ArrayList<>();
        Class clazz = t.getClass();
        while (clazz != Object.class) {
            fields.addAll(Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .filter(field -> !Modifier.isTransient(field.getModifiers()))
                    .collect(Collectors.toList()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static <T> T getFieldValue(Object object, String name) {
        Field field = null;
        try {
            field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null) {
                field.setAccessible(false);
            }
        }
        return null;
    }

}
