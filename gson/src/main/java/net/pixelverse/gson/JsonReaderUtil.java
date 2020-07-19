package net.pixelverse.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JsonReaderUtil {
    private static Map<String, Field> fields = new HashMap<String, Field>();
    private static Map<String, Field> treeFields = new HashMap<String, Field>();

    private static Field getField(String name) throws NoSuchFieldException {
        if (!fields.containsKey(name)) {
            Field field = JsonReader.class.getDeclaredField(name);
            field.setAccessible(true);
            fields.put(name, field);
        }
        return fields.get(name);
    }

    private static Field getTreeField(String name) throws NoSuchFieldException {
        if (!treeFields.containsKey(name)) {
            Field field = JsonTreeReader.class.getDeclaredField(name);
            field.setAccessible(true);
            treeFields.put(name, field);
        }
        return treeFields.get(name);
    }

    public static Map<String, Object> getReaderInformation(JsonReader reader) throws IllegalAccessException, NoSuchFieldException {
        Map<String, Object> data = reader instanceof JsonTreeReader ? getTreeReaderInformation((JsonTreeReader) reader) : new HashMap<String, Object>();
        data.put("buffer", Arrays.copyOf((char[]) getField("buffer").get(reader), 1024));
        data.put("pos", getField("pos").get(reader));
        data.put("limit", getField("limit").get(reader));
        data.put("lineNumber", getField("lineNumber").get(reader));
        data.put("lineStart", getField("lineStart").get(reader));
        data.put("peeked", getField("peeked").get(reader));
        data.put("peekedLong", getField("peekedLong").get(reader));
        data.put("peekedNumberLength", getField("peekedNumberLength").get(reader));
        data.put("peekedString", getField("peekedString").get(reader));
        data.put("baseStack", Arrays.copyOf((int[]) getField("stack").get(reader), 32));
        data.put("baseStackSize", getField("stackSize").get(reader));
        data.put("basePathNames", Arrays.copyOf((String[]) getField("pathNames").get(reader), 32));
        data.put("basePathIndices", Arrays.copyOf((int[]) getField("pathIndices").get(reader), 32));
        Gson gson = new Gson();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            String json = gson.toJson(entry.getValue());
            Object newValue = gson.fromJson(json, entry.getValue().getClass());
            entry.setValue(newValue);
        }
        return data;
    }

    public static Map<String, Object> getTreeReaderInformation(JsonTreeReader reader) throws IllegalAccessException, NoSuchFieldException {
        Map<String, Object> data = new HashMap<String, Object>();
        SuperGson serializer = new SuperGson();

        data.put("stack", Arrays.copyOf((Object[]) getTreeField("stack").get(reader), 32));
        data.put("stackSize", getTreeField("stackSize").get(reader));
        data.put("pathNames", Arrays.copyOf((String[]) getTreeField("pathNames").get(reader), 32));
        data.put("pathIndices", Arrays.copyOf((int[]) getTreeField("pathIndices").get(reader), 32));

        return data;
    }

    public static void restoreReaderInformation(JsonReader reader, Map<String, Object> information) throws IllegalAccessException, NoSuchFieldException {
        if (reader instanceof JsonTreeReader) {
            restoreTreeReaderInformation((JsonTreeReader) reader, information);
        }
        getField("pos").set(reader, information.get("pos"));
        getField("limit").set(reader, information.get("limit"));
        getField("lineNumber").set(reader, information.get("lineNumber"));
        getField("lineStart").set(reader, information.get("lineStart"));
        getField("peeked").set(reader, information.get("peeked"));
        getField("peekedLong").set(reader, information.get("peekedLong"));
        getField("peekedNumberLength").set(reader, information.get("peekedNumberLength"));
        getField("peekedString").set(reader, information.get("peekedString"));
        getField("stack").set(reader, information.get("baseStack"));
        getField("stackSize").set(reader, information.get("baseStackSize"));
        getField("pathNames").set(reader, information.get("basePathNames"));
        getField("pathIndices").set(reader, information.get("basePathIndices"));
    }

    public static void restoreTreeReaderInformation(JsonTreeReader reader, Map<String, Object> information) throws IllegalAccessException, NoSuchFieldException {
        getTreeField("stack").set(reader, information.get("stack"));
        getTreeField("stackSize").set(reader, information.get("stackSize"));
        getTreeField("pathNames").set(reader, information.get("pathNames"));
        getTreeField("pathIndices").set(reader, information.get("pathIndices"));
    }
}
