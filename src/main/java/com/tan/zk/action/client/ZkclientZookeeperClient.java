package com.tan.zk.action.client;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.tan.zk.action.pubsub.URL;

public class ZkclientZookeeperClient extends AbstractZookeeperClient<IZkChildListener>{


    private final ZkClientWrapper client;

    private volatile KeeperState state = KeeperState.SyncConnected;

    public ZkclientZookeeperClient(URL url) {
        super(url);
        client = new ZkClientWrapper(url.getBackupAddress(), 30000);
        client.addListener(new IZkStateListener() {
            @Override
            public void handleStateChanged(KeeperState state) throws Exception {
                ZkclientZookeeperClient.this.state = state;
                if (state == KeeperState.Disconnected) {
                    stateChanged(StateListener.DISCONNECTED);
                } else if (state == KeeperState.SyncConnected) {
                    stateChanged(StateListener.CONNECTED);
                }
            }

            @Override
            public void handleNewSession() throws Exception {
                stateChanged(StateListener.RECONNECTED);
            }

            @Override
            public void handleSessionEstablishmentError(Throwable error) throws Exception {
                
            }
            
            
        });
        client.start();
    }


    @Override
    public void createPersistent(String path) {
        try {
            client.createPersistent(path);
        } catch (ZkNodeExistsException e) {
        }
    }

    @Override
    public void createEphemeral(String path) {
        try {
            client.createEphemeral(path);
        } catch (ZkNodeExistsException e) {
        }
    }

    @Override
    public void delete(String path) {
        try {
            client.delete(path);
        } catch (ZkNoNodeException e) {
        }
    }

    @Override
    public List<String> getChildren(String path) {
        try {
            return client.getChildren(path);
        } catch (ZkNoNodeException e) {
            return null;
        }
    }

    @Override
    public boolean checkExists(String path) {
        try {
            return client.exists(path);
        } catch (Throwable t) {
        }
        return false;
    }

    @Override
    public boolean isConnected() {
        return state == KeeperState.SyncConnected;
    }

    @Override
    public void doClose() {
        client.close();
    }

    @Override
    public IZkChildListener createTargetChildListener(String path, final ChildListener listener) {
        return new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds)
                    throws Exception {
                listener.childChanged(parentPath, currentChilds);
            }
        };
    }

    @Override
    public List<String> addTargetChildListener(String path, final IZkChildListener listener) {
        return client.subscribeChildChanges(path, listener);
    }

    @Override
    public void removeTargetChildListener(String path, IZkChildListener listener) {
        client.unsubscribeChildChanges(path, listener);
    }


}
