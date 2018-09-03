package com.tan.zk.action.pubsub;

import com.tan.zk.action.client.ZookeeperClient;

public interface ZookeeperTransporter {
    ZookeeperClient connect(URL url);
}
