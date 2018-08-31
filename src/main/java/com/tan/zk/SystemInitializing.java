package com.baibei.wine.paycenter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SystemInitializing implements InitializingBean {

    
    public void afterPropertiesSet() throws Exception {
        doInit();
    }

    private void doInit() {
        //初始化定时任务master争抢
        try {
        } catch (Exception e) {
            e.printStackTrace();
            log.info("error:{}", e.getMessage());
        }
    }

}
