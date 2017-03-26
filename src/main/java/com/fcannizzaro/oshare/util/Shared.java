package com.fcannizzaro.oshare.util;

import com.fcannizzaro.oshare.annotations.Share;
import com.fcannizzaro.oshare.model.FakeMap;
import com.fcannizzaro.oshare.model.Item;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Francesco Cannizzaro (fcannizzaro)
 */
public class Shared {

    private static FakeMap<String, Object> data = new FakeMap<>();

    public static JSONObject mock() {
        return mock(data, new JSONObject());
    }

    private static JSONObject mock(FakeMap<String, Object> data, JSONObject object) {

        for (String key : data.keySet()) {
            try {

                Object value = data.get(key);

                if (value instanceof FakeMap) {
                    object.put(key, mock((FakeMap) value, new JSONObject()));
                    continue;
                }

                if (value instanceof Item) {

                    Item item = (Item) value;

                    if (item.getValue() instanceof Method) {
                        object.put(key, "$function");
                    } else {
                        Field field = (Field) item.getValue();
                        object.put(key, field.get(item.getInstance()));
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return object;
    }

    private static Object find(String key) {

        String[] dots = key.split("\\.");
        Map<String, Object> temp = data;

        for (String dot : dots) {

            Object v = temp.get(dot);

            if (v instanceof Map) {
                temp = (Map<String, Object>) v;
                continue;
            }

            return v;

        }

        return null;

    }

    public static void invoke(String key, Object[] args) {

        if (key.contains("$cb.")) {
            Callback.execute(key, args);
            return;
        }

        Object value = find(key);

        if (value != null) {
            try {
                Item item = (Item) value;
                Method method = (Method) item.getValue();
                method.invoke(item.getInstance(), args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void put(String key, Object value, Object instance) {

        int i;
        String[] dots = key.split("\\.");
        FakeMap<String, Object> temp = data;

        int limits = dots.length - 1;

        if (value instanceof Map) {
            limits++;
        }

        for (i = 0; i < limits; i++) {
            if (temp.get(dots[i]) == null) {
                FakeMap<String, Object> nested = new FakeMap<>();
                temp.put(dots[i], nested);
                temp = nested;
            } else {
                temp = (FakeMap) temp.get(dots[i]);
            }
        }

        temp.put(dots[i], new Item(instance, value));
    }

    private static void check(Share annotation, Member member, Object instance) {

        if (annotation != null) {

            String key = annotation.value();

            if (key.equals("null")) {
                key = member.getName();
            }

            put(key, member, instance);

        }

    }

    public static void register(Object instance) {

        for (Method method : instance.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            check(method.getAnnotation(Share.class), method, instance);
        }

        for (Field field : instance.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            check(field.getAnnotation(Share.class), field, instance);
        }

    }

}
