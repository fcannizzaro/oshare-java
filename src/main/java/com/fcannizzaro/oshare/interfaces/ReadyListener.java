package com.fcannizzaro.oshare.interfaces;

/**
 * Created by Francesco Cannizzaro (fcannizzaro)
 */
public interface ReadyListener {

    // on data available
    void onReady();

    // on socket connected
    void onConnected();

}
