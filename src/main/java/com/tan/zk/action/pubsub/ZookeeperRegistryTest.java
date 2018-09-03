package com.tan.zk.action.pubsub;

public class ZookeeperRegistryTest {

    public static void main(String[] args) throws Exception {
         URL url=new URL();
         url.setBackupAddress("192.168.12.89:2181");
        ZookeeperTransporter zookeeperTransporter=new ZkclientZookeeperTransporter();
        ZookeeperRegistry ZookeeperRegistry=new ZookeeperRegistry(url,zookeeperTransporter);
      //  ZookeeperRegistry.doRegister("/tanpubT/pay", false);
        ZookeeperRegistry.doSubscribe("/pay");
        ZookeeperRegistry.doRegister("/tanpubT/pay/0001", false);
    }
}
