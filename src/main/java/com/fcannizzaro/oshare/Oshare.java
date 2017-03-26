package com.fcannizzaro.oshare;

import com.fcannizzaro.oshare.interfaces.ReadyListener;
import com.fcannizzaro.oshare.model.FakeCallback;
import com.fcannizzaro.oshare.util.Callback;
import com.fcannizzaro.oshare.util.Shared;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Francesco Cannizzaro (fcannizzaro)
 */
public class Oshare {

    private static Oshare instance;
    private Socket socket;

    private Oshare() {
    }

    public static FakeCallback Cb(Object instance) {
        return new FakeCallback(instance);
    }

    public static void init(String url, Class clazz) {
        init(url, clazz, null);
    }

    public static void init(String url, Class clazz, ReadyListener listener) {

        if (instance != null) {
            return;
        }

        instance = new Oshare();

        try {

            instance.socket = IO.socket(url);

            instance.socket.on(Socket.EVENT_CONNECT, args -> {

                if (listener != null) {
                    listener.onConnected();
                }

                instance.socket.emit("share", Shared.mock());

            });

            instance.socket.on("share", args -> {

                iterate((JSONObject) args[0], clazz.getCanonicalName());

                if (listener != null) {
                    listener.onReady();
                }


            });

            instance.socket.on("invoke", args -> {
                try {

                    JSONObject object = (JSONObject) args[0];
                    JSONArray array = object.getJSONArray("args");

                    Object[] objects = new Object[array.length()];

                    for (int i = 0; i < array.length(); i++) {
                        objects[i] = array.get(i);
                    }

                    Shared.invoke(object.getString("method"), objects);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

            instance.socket.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void iterate(JSONObject object, String path) {

        Iterator<String> it = object.keys();

        try {

            Class<?> nested = Class.forName(path);

            while (it.hasNext()) {
                try {

                    String key = it.next();
                    Object value = object.get(key);

                    if (value instanceof JSONObject) {
                        iterate((JSONObject) value, path + "$" + key);
                        continue;
                    }

                    if (value.equals("$function")) {
                        continue;
                    }

                    Field field = nested.getDeclaredField(key);
                    field.set(null, value);

                } catch (Exception e) {

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void invoke(String key, Object... args) {

        ArrayList<Method> methods = new ArrayList<>();
        int counter = 0;

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof FakeCallback) {

                FakeCallback callback = (FakeCallback) args[i];
                Class clazz = callback.getInstance().getClass();
                if (methods.size() == 0) {
                    for (Method method : clazz.getDeclaredMethods()) {
                        String lowerKey = key.toLowerCase().replace(".", "");
                        if (method.getName().toLowerCase().contains(lowerKey)) {
                            method.setAccessible(true);
                            methods.add(method);
                        }
                    }
                }
                args[i] = Callback.add(callback, methods.get(counter++));
            }
        }

        try {
            JSONObject packet = new JSONObject();
            packet.put("method", key);
            packet.put("args", args);
            instance.socket.emit("invoke", packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
