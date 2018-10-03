package ru.otus.gromov;

import javax.json.*;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;


public class JsonHelper {
    static String serialize(Object obj) {
        return writeToString(getJson(obj).build());
    }

    @SuppressWarnings("unchecked")
    public static JsonObjectBuilder getJson(Object object) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonVisitor jsonVisitor = new JsonVisitor();
        List<Field> fields = ReflectionHelper.getFields(object);

        for (Field field : fields) {
            Object value = ReflectionHelper.getFieldValue(object, field.getName());
            if (value == null) {
                builder.addNull(field.getName());
                continue;
            }
            if (field.getType().isArray()) {
                JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
                for (Object obj : (Object[]) value) {
                    jsonVisitor.visit(obj, jsonArrayBuilder, null);
                }
                builder.add(field.getName(), jsonArrayBuilder);
            } else if (value instanceof Collection) {
                JsonArrayBuilder jsonCollectionBuilder = Json.createArrayBuilder();
                ((Collection) value)
                        .forEach((o) -> jsonVisitor.visit(o, jsonCollectionBuilder, null));
                builder.add(field.getName(), jsonCollectionBuilder);
            } else {
                jsonVisitor.visit(value, builder, field.getName());
            }
        }
        return builder;
    }

    private static String writeToString(JsonObject jsonst) {
        StringWriter stWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
            jsonWriter.writeObject(jsonst);
        }

        return stWriter.toString();
    }


}
