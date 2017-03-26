package com.fcannizzaro.oshare.model;

import java.lang.reflect.Method;

/**
 * Created by Francesco Cannizzaro (fcannizzaro)
 */
public class FakeCallback {

    private Object instance;
    private Method method;

    public FakeCallback(Object instance) {
        this.instance = instance;
    }

    public Object getInstance() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

}
