package com.example.rpc1.client;

import com.example.rpc1.common.Constant;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * auth:XuanZi
 * time:2021/7/30 14:55
 */
@Component
public class DiscoverService {
    @Value("${registry.address}")
    private String registryAddress;


    private CountDownLatch latch = new CountDownLatch(1);

    private List<String> dataLists;

    public String getServer() {
        String data = null;
        int size = dataLists.size();

        if (size > 0) {
            if(size ==1) {
                return dataLists.get(0);
            }
            else {
                return dataLists.get(ThreadLocalRandom.current().nextInt(size));
            }
        }

        return null;
    }

    @PostConstruct
    private void post() {
        ZooKeeper connect = connect();
        if (connect != null) {
            whatNode(connect);
        }
    }


    public ZooKeeper connect() {
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper(registryAddress, 5000, watchedEvent -> {
                if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return zooKeeper;
    }

    private void whatNode(ZooKeeper zooKeeper) {
        try {

            List<String> strings = zooKeeper.getChildren(Constant.ZK_DATA_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                        whatNode(zooKeeper);
                    }
                }
            });


            List<String> dataList = new ArrayList<>();
            for (String node : strings) {
                byte[] bytes = zooKeeper.getData(Constant.ZK_REGISTRY_PATH + "/" + node, false, null);
                dataList.add(new String(bytes));
            }

            this.dataLists = dataList;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
