package com.tan.zk.action.client;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public  interface ListenableFuture<V> extends Future<V> {
    void addListener(Runnable listener, Executor executor);

    void addListener(Runnable listener);
}
