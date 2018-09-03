package com.tan.zk.action.client;

public interface StateListener {

    int DISCONNECTED = 0;

    int CONNECTED = 1;

    int RECONNECTED = 2;

    void stateChanged(int connected);


}
