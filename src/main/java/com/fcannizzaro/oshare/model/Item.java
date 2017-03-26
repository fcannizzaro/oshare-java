package com.fcannizzaro.oshare.model;

/**
 * Created by Francesco Cannizzaro (fcannizzaro)
 */
public class Item {

    private Object instance;
    private Object value;

    public Item(Object instance, Object value) {
        this.instance = instance;
        this.value = value;
    }

    public Object getInstance() {
        return instance;
    }

    public Object getValue() {
        return value;
    }

}
