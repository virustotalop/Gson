package com.google.gson;

import com.google.gson.common.TestTypes;
import junit.framework.TestCase;
import com.google.gson.internal.Primitives;

import java.util.*;

public class SuperGsonTest extends TestCase {

    private SuperGson gson = new SuperGson();

    public void testDeserializeOutOfOrder() {
        String target = "Hello";
        JsonObject json = new JsonObject();
        json.addProperty("data", target);
        json.addProperty("type", Primitives.toTypeName(target.getClass()));
        System.out.println(json);
        String out = gson.fromJson(json.toString());
        assertEquals(target, out);
    }

    public void testBasicInt() {
        doSerializationTest(4);
    }

    public void testObject() {
        TestTypes.ComplexClass complexClass = new TestTypes.ComplexClass();
        doSerializationTest(complexClass);
    }

    public void testMap() {
        Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        map.put("list", new ArrayList<String>(Collections.singletonList("hello")));
        doSerializationTest(map);
    }

    public void testList() {
        List<TestTypes.ComplexClass> objects = new ArrayList<TestTypes.ComplexClass>(Arrays.asList(new TestTypes.ComplexClass(), new TestTypes.ComplexClass()));
        doSerializationTest(objects);
    }

    private <T> void doSerializationTest(T object) {
        String json = gson.toJson(object);
        System.out.println(json);
        T out = gson.fromJson(json);
        assertEquals(object, out);
    }
}
