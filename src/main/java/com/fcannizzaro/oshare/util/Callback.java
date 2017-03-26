package com.fcannizzaro.oshare.util;

import com.fcannizzaro.oshare.model.FakeCallback;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Francesco Cannizzaro (fcannizzaro)
 */
public class Callback {

    private static ArrayList<FakeCallback> callbacks = new ArrayList<>();

    public static synchronized String add(FakeCallback callback, Method method) {
        callback.setMethod(method);
        callbacks.add(callback);
        return "$cb." + String.valueOf(callbacks.size() - 1);
    }

    static void execute(String id, Object... args) {
        int idx = Integer.parseInt(id.split("\\.")[1]);
        if (callbacks.size() > idx) {
            try {
                FakeCallback callback = callbacks.get(idx);
                callback.getMethod().invoke(callback.getInstance(), args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
