package com.tan.zk.action.api;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchClient implements Runnable{
   // private static final Log LOG = LogFactory.getLog(WatchClient.class);
    public static final Logger LOG = LoggerFactory.getLogger(WatchClient.class);
    public static final int CLIENT_PORT = 2181;
    public static final String PATH = "/app1";// 所要监控的结点
    private static ZooKeeper zk;
    private static List<String> nodeList;// 所要监控的结点的子结点列表
 
    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure("F:\\test\\conf\\log4j.properties");
        WatchClient client = new WatchClient();
        Thread th = new Thread(client);
        th.start();
    }
 
    public WatchClient() throws IOException {
 
        zk = new ZooKeeper("192.168.255.133:" + CLIENT_PORT, 21810,
                new Watcher() {
                    public void process(WatchedEvent event) {
                    }
                });
    }
 
    /**
     * 设置watch的线程
     */
    @Override
    public void run() {
        
        Watcher wc = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 结点数据改变之前的结点列表
                List<String> nodeListBefore = nodeList;
                // 主结点的数据发生改变时
                if (event.getType() == EventType.NodeDataChanged) {
                    LOG.info("Node data changed:" + event.getPath());
                }
                if (event.getType() == EventType.NodeDeleted){
                    LOG.info("Node deleted:" + event.getPath());
                }
                if(event.getType()== EventType.NodeCreated){
                    LOG.info("Node created:"+event.getPath());
                }
 
                // 获取更新后的nodelist
                try {
                    nodeList = zk.getChildren(event.getPath(), false);
                } catch (KeeperException e) {
                    System.out.println(event.getPath()+" has no child, deleted.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<String> nodeListNow = nodeList;
                // 增加结点
                if (nodeListBefore.size() < nodeListNow.size()) {
                    for (String str : nodeListNow) {
                        if (!nodeListBefore.contains(str)) {
                            LOG.info("Node created:" + event.getPath() + "/" + str);
                        }
                    }
                }
            }
        };
 
        /**
         * 持续监控PATH下的结点
         */
        while (true) {
            try {
                zk.exists(PATH, wc);//所要监控的主结点
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                nodeList = zk.getChildren(PATH, wc);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
            // 对PATH下的每个结点都设置一个watcher
 
            for (String nodeName : nodeList) {
                try {
                    zk.exists(PATH + "/" + nodeName, wc);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            try {
                Thread.sleep(3000);// sleep一会，减少CUP占用率
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
