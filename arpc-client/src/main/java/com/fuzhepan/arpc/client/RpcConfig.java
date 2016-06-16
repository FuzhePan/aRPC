package com.fuzhepan.arpc.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by FuzhePan on 2016/6/16.
 */
public class RpcConfig {
    private static ConcurrentHashMap<String,List<HostPortPair>> serviceConfigMap = new ConcurrentHashMap<String, List<HostPortPair>>();

    /**
     * ���һ������
     * @param serviceName ������
     * @param host ������������ip
     * @param port �˿�
     */
    public synchronized static void addService(String serviceName,String host,int port){
        if(serviceName == null || serviceName.isEmpty())
            throw new IllegalArgumentException("interfaceName is null or empty");
        if(host == null || host.isEmpty())
            throw new IllegalArgumentException("host is null or empty");
        if(port<=0 || port>65535)
            throw new IllegalArgumentException("Invalid port");

        if(!serviceConfigMap.containsKey(serviceName))
            serviceConfigMap.put(serviceName,new ArrayList<HostPortPair>());

        List<HostPortPair> hostList = serviceConfigMap.get(serviceName);
        hostList.add(new HostPortPair(host,port));
    }

    /**
     * ���ݷ�������ȡ�����б�
     * @param serviceName
     * @return
     */
    public static List<HostPortPair> getServiceList(String serviceName){
        if(!serviceConfigMap.containsKey(serviceName))
            return null;

        return serviceConfigMap.get(serviceName);
    }
}
