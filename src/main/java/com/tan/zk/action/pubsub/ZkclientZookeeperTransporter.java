package com.tan.zk.action.pubsub;

import com.tan.zk.action.client.ZkclientZookeeperClient;
import com.tan.zk.action.client.ZookeeperClient;

public class ZkclientZookeeperTransporter implements ZookeeperTransporter{
    public ZookeeperClient connect(URL url) {
        return new ZkclientZookeeperClient(url);
    }
}
