package com.tan.zk.action.client;

import java.util.List;

public interface ChildListener {

    void childChanged(String path, List<String> children);

}
