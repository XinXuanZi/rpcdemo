package com.example.rpc1.server;

import com.example.rpc1.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author XuanZi
 * @date 2021/7/2518:06
 */
@Slf4j
@Component
public class ServiceRegistry {
    @Value("${registry.address}")
    private String registryAddress;

    private CountDownLatch latch = new CountDownLatch(1);

    public void register(String data) {

        if (data != null) {
            ZooKeeper zooKeeper = connectService();
            if (zooKeeper != null) {
                log.error("服务注册====》》》" + data);
                createNode(zooKeeper, data);
            }
        }
    }

    private ZooKeeper connectService(){
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper(registryAddress, 5000, watchedEvent -> {
                if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            });
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return zooKeeper;
    }

    private void createNode(ZooKeeper zooKeeper, String data) {

        try {
            byte[] bytes = data.getBytes();
            zooKeeper.create(Constant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
