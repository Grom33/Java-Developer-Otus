package ru.otus.gromov;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.lang.reflect.Method;

public class JsonVisitor {
    private void visitString(String obj, Object json, String fieldName) {
        if (json instanceof JsonArrayBuilder) {
            ((JsonArrayBuilder) json).add(obj);
        } else {
            ((JsonObjectBuilder) json).add(fieldName, obj);
        }
    }

    private void visitInteger(Integer obj, Object json, String fieldName) {
        if (json instanceof JsonArrayBuilder) {
            ((JsonArrayBuilder) json).add(obj);
        } else {
            ((JsonObjectBuilder) json).add(fieldName, obj);
        }
    }

    private void visitLong(Long obj, Object json, String fieldName) {
        if (json instanceof JsonArrayBuilder) {
            ((JsonArrayBuilder) json).add(obj);
        } else {
            ((JsonObjectBuilder) json).add(fieldName, obj);
        }
    }

    private void visitDouble(Double obj, Object json, String fieldName) {
        if (json instanceof JsonArrayBuilder) {
            ((JsonArrayBuilder) json).add(obj);
        } else {
            ((JsonObjectBuilder) json).add(fieldName, obj);
        }
    }

    private void visitBoolean(Boolean obj, Object json, String fieldName) {
        if (json instanceof JsonArrayBuilder) {
            ((JsonArrayBuilder) json).add(obj);
        } else {
            ((JsonObjectBuilder) json).add(fieldName, obj);
        }
    }

    private void visitDefault(Object obj, Object json, String fieldName) {
        if (json instanceof JsonArrayBuilder) {
            ((JsonArrayBuilder) json).add(JsonHelper.getJson(obj));
        } else {
            ((JsonObjectBuilder) json).add(fieldName, JsonHelper.getJson(obj));
        }
    }

    public Object visit(Object o, Object json, String fieldName) {

        String methodName = o.getClass().getName();
        methodName = "visit" +
                methodName.substring(methodName.lastIndexOf('.') + 1);
        try {
            Method m = getClass().getDeclaredMethod(methodName, o.getClass(), Object.class, String.class);
            try {
                m.invoke(this, new Object[]{o, json, fieldName});
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            visitDefault(o, json, fieldName);
        }
        return json;
    }
}