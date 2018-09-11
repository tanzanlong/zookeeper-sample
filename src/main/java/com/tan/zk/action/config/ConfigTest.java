package com.tan.zk.action.config;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ConfigTest {
    @Test
    public void testZkConfig() throws JsonProcessingException, InterruptedException {
 
        ConfigManager cfgManager = new ConfigManager();
        ConfigClient clientApp = new ConfigClient();
 
        //模拟【配置管理中心】初始化时，从db加载配置初始参数
        cfgManager.loadConfigFromDB();
        //然后将配置同步到ZK
        cfgManager.syncFtpConfigToZk();
 
        //模拟客户端程序运行
        clientApp.run();
 
        //模拟配置修改
        cfgManager.updateFtpConfigToDB(23, "10.6.12.34", "newUser", "newPwd");
        cfgManager.syncFtpConfigToZk();
 
        //模拟客户端自动感知配置变化
        clientApp.run();
 
    }
 
}
