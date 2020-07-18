package net.pixelverse.gson;

import com.google.gson.JsonObject;
import junit.framework.TestCase;

import java.util.*;

public class SuperGsonTest extends TestCase {

    private SuperGson gson = new SuperGson();

    public void testDeserializeOutOfOrder() {
        String target = "Hello";
        JsonObject json = new JsonObject();
        json.addProperty("data", target);
        json.addProperty("type", target.getClass().getName());
        System.out.println(json);
        String out = gson.fromJson(json.toString());
        assertEquals(target, out);
    }

    public void testMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", new ArrayList<String>(Arrays.asList("hello")));
        doSerializationTest(map);
    }

    private <T> void doSerializationTest(T object) {
        String json = gson.toJson(object);
        System.out.println(json);
        T out = gson.fromJson(json);
        assertEquals(object, out);
    }
}
