package cn.tojintao.distributed;

import cn.tojintao.constant.ServerConstants;
import cn.tojintao.model.entity.ImNode;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
@Data
@Slf4j
public class WorkerRouter {

    private ImNode node = null;


    private static WorkerRouter singleInstance = null;
    private NamingService naming;



    private ConcurrentHashMap<String, PeerSender> workerMap =
            new ConcurrentHashMap<>();


    public synchronized static WorkerRouter getInst() {
        if (null == singleInstance) {
            singleInstance = new WorkerRouter();
        }
        return singleInstance;
    }

    private WorkerRouter() {

    }

    private boolean inited=false;

    /**
     * 初始化节点管理
     */
    public void init() {

        if(inited)
        {
            return;
        }
        inited=true;

        try {
            if (null == naming) {
                try {
                    this.naming = NamingFactory.createNamingService(ServerConstants.nacosServer);
                } catch (NacosException e) {
                    throw new RuntimeException(e);
                }

            }
            try {
                naming.subscribe(ServerConstants.nettyName, event -> {
                    if (event instanceof NamingEvent) {
                        List<Instance> instances = ((NamingEvent) event).getInstances();
                        for (Map.Entry<String, PeerSender> entry : workerMap.entrySet()) {
                            entry.getValue().setOnline(false);
                        }

                        for (Instance instance : instances) {
                            String addr = instance.getIp()+":"+instance.getPort();
                            if (workerMap.containsKey(addr)) {
                                workerMap.get(instance.getInstanceId()).setOnline(true);
                            } else {
                                ImNode rmNode = new ImNode(instance.getIp(), instance.getPort());
                                PeerSender peerSender = new PeerSender(rmNode);
                                peerSender.setOnline(true);
                                FutureTaskScheduler.add(() ->
            {
                                peerSender.doConnect();
                            });
                                workerMap.put(addr, peerSender);
                            }
                        }
                        for (Map.Entry<String, PeerSender> entry : workerMap.entrySet()) {
                            if (entry.getValue().getConnectFlag() == false) {
                                entry.getValue().stopConnecting();
                                workerMap.remove(entry.getKey());
                            }
                        }
                    }
                });
            } catch (NacosException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

            //订阅节点的增加和删除事件

    public PeerSender route(String addr) {
        PeerSender peerSender = workerMap.get(addr);
        if (null != peerSender) {
            return peerSender;
        }
        return null;
    }


    public void sendNotification(String json) {
        workerMap.keySet().stream().forEach(
                key ->
                {
                    if (!key.equals(getLocalNode().getId())) {
                        PeerSender peerSender = workerMap.get(key);
                        peerSender.writeAndFlush(json);
                    }
                }
        );

    }


    public ImNode getLocalNode() {
        return ImWorker.getInst().getLocalNodeInfo();
    }
}
