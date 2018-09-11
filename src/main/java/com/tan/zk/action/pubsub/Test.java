package com.tan.zk.action.pubsub;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class Test {
 public static void main(String[] args) {
     ZkClient zk = new ZkClient("192.168.12.89:2181", 3000, 5000, new SerializableSerializer());
   //  zk.writeData("/tanpubT/pay/0001", "123");
     
    // zk.writeData("/tanpubT/pay", "123");
     
     zk.createEphemeral("/tanpubT/pay2");
}
}
