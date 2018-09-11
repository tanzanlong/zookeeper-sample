package com.tan.zk.action.pubsub;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tan.zk.action.client.ChildListener;
import com.tan.zk.action.client.StateListener;
import com.tan.zk.action.client.ZookeeperClient;

/**
 * ZookeeperRegistry
 *
 */
public class ZookeeperRegistry {

    private final static Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

    private final static int DEFAULT_ZOOKEEPER_PORT = 2181;


    private final String root;


    private final ConcurrentMap<String, ConcurrentMap<String, ChildListener>> zkListeners = new ConcurrentHashMap<String, ConcurrentMap<String, ChildListener>>();

    private final ZookeeperClient zkClient;

    public ZookeeperRegistry(URL url, ZookeeperTransporter zookeeperTransporter) {
        String group = "/tanpubT";
        this.root = group;
        zkClient = zookeeperTransporter.connect(url);
        zkClient.addStateListener(new StateListener() {
            
            public void stateChanged(int state) {
                if (state == RECONNECTED) {
                    try {
                        logger.info(" state == RECONNECTED");;
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        });
    }

    static String appendDefaultPort(String address) {
        if (address != null && address.length() > 0) {
            int i = address.indexOf(':');
            if (i < 0) {
                return address + ":" + DEFAULT_ZOOKEEPER_PORT;
            } else if (Integer.parseInt(address.substring(i + 1)) == 0) {
                return address.substring(0, i + 1) + DEFAULT_ZOOKEEPER_PORT;
            }
        }
        return address;
    }

    
    public boolean isAvailable() {
        return zkClient.isConnected();
    }

    
    public void destroy() {
        try {
            zkClient.close();
        } catch (Exception e) {
            logger.warn("Failed to close zookeeper client " + root + ", cause: " + e.getMessage(), e);
        }
    }

    
    protected void doRegister(String String,boolean ephemeral) throws Exception {
        try {
            zkClient.create(String, ephemeral);
        } catch (Throwable e) {
            throw new Exception("Failed to register " + String + " to zookeeper " + root + ", cause: " + e.getMessage(), e);
        }
    }

    
    protected void doUnregister(String String) throws Exception {
        try {
            zkClient.delete(String);
        } catch (Throwable e) {
            throw new Exception("Failed to unregister " + String + " to zookeeper " + root + ", cause: " + e.getMessage(), e);
        }
    }


    protected void doSubscribe(final String path) throws Exception {
        try {
            ChildListener zkListener = new ChildListener() {
                public void childChanged(String parentPath, List<String> currentChilds) {
                    logger.info(" childChanged:{}",currentChilds);
                }
            };
           // zkClient.create(root, false);
            
          //  Map<String,ChildListener> listeners=zkListeners.get(path);
            List<String> services = zkClient.addChildListener(root, zkListener);
            logger.info(" services:{}",services);
            //listeners.put("confitListner", zkListener);
        } catch (Throwable e) {
            throw new Exception("Failed to subscribe " + path + " to zookeeper " + root
                    + ", cause: " + e.getMessage(), e);
        }
    }

    
    protected void doUnsubscribe(String path) {
        ChildListener zkListener = null;
        zkClient.removeChildListener(path, zkListener);
    }


}
