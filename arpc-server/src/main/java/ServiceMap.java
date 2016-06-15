import java.util.concurrent.ConcurrentHashMap;

/**
 * ��������Ϣ��
 */
public class ServiceMap {

    private static ConcurrentHashMap<String,Class> serviceMap = new ConcurrentHashMap<String, Class>();

    public static void addService(String serviceName,Class serviceType){
        serviceMap.put(serviceName,serviceType);
    }

    public static Class getService(String serviceName){
        return serviceMap.get(serviceName);
    }
}
