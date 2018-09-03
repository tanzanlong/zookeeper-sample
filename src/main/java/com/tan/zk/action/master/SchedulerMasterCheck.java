package com.tan.zk.action.master;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SchedulerMasterCheck implements IZkDataListener{

    
    
    private final String LOCK_NODE = "/customerservicelock";
    
    //ZK客户端
    private ZkClient zk = null;
    
    //临时serverID
    private String serverId = UUID.randomUUID().toString().toLowerCase();
    
    public void init(){
        zk = new ZkClient("", 3000, 5000, new SerializableSerializer());
        log.info("Temp serverId = {}", serverId);
        zk.subscribeDataChanges(LOCK_NODE, this);
    }
    
    //抢master
    private void createLock(){
        try{
            zk.createEphemeral(LOCK_NODE, serverId);
            log.info("serverId {} take master SUCCESS", serverId);
        }catch(ZkNodeExistsException e){//节点已存在
            log.info("serverId {} take master FAILURE", serverId);
            if(zk.readData(LOCK_NODE) == null){//在读取过程中，发现master节点已经被释放
                createLock();
            }
        }
    }
    
    public boolean lock(){
        try{
            String data = zk.readData(LOCK_NODE);
            if(serverId.equalsIgnoreCase(data)){
                return true;
            }
            createLock();
        }catch(Exception e){
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public void handleDataChange(String arg0, Object arg1) throws Exception {
    }

    @Override
    public void handleDataDeleted(String arg0) throws Exception {
        log.info("Old Master is down, Take master...");
        createLock();
    }
}
