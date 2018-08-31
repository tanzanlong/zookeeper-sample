package com.baibei.wine.paycenter.zk.api;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ZkSidAndPwdInitExample implements Watcher{

    static  CountDownLatch connectedS=new CountDownLatch(1);
      
      public static void main(String[] args) {
        try {
          ZooKeeper zk=new ZooKeeper("localhost:2182,localhost:2183,localhost:2184",5000,new ZkInitExample());
            
          ZooKeeper zk1=new ZooKeeper("localhost:2182,localhost:2183,localhost:2184",5000,new ZkSidAndPwdInitExample(),1L,"".getBytes());
          
          ZooKeeper zk2=new ZooKeeper("localhost:2182,localhost:2183,localhost:2184",5000,new ZkSidAndPwdInitExample(),zk.getSessionId(),zk.getSessionPasswd());
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
