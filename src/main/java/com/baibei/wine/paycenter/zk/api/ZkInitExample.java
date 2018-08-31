package com.baibei.wine.paycenter.zk.api;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ZkInitExample implements Watcher {
  static  CountDownLatch connectedS=new CountDownLatch(1);
    
    public static void main(String[] args) {
      try {
        ZooKeeper zk=new ZooKeeper("localhost:2182,localhost:2183,localhost:2184",5000,new ZkInitExample());
        System.out.println(zk.getState());
        try {
            connectedS.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("session established");
      } catch (IOException e) {
        e.printStackTrace();
    }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("receive WatchedEvent:"+event);
        if (KeeperState.SyncConnected == event.getState()) {
            connectedS.countDown();
        }
    }
    
    
}
