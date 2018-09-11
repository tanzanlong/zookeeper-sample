package com.tan.zk.action.api;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

public class ZkClientWatcher {
    public static void main(String[] args) throws InterruptedException {

        ZkClient zkClient = new ZkClient("192.168.12.89:2181",5000);
        System.out.println("ZK 成功建立连接！");

        String path = "/zk-test";
        // 注册子节点变更监听（此时path节点并不存在，但可以进行监听注册）
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("路径" + parentPath +"下面的子节点变更。子节点为：" + currentChilds );
            }
        });

        // 递归创建子节点（此时父节点并不存在）
        zkClient.createPersistent("/zk-test/a1",true);
        Thread.sleep(5000);
        System.out.println(zkClient.getChildren(path));
    }
}
