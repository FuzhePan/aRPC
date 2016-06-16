import com.fuzhepan.arpc.client.ProxyFactory;
import com.fuzhepan.arpc.client.RpcConfig;

/**
 * Created by FuzhePan on 2016/6/15.
 */
public class Consumer {

    public static void  main(String[] args){

        RpcConfig.addService("example","127.0.0.1",12701);
        RpcConfig.addService("example","127.0.0.1",12702);

        for(int i=0;i<5;i++){
            String url="rpc://example/HelloWorld";
            HelloWorld service = ProxyFactory.create(url,HelloWorld.class);
            String result = service.sayHello();

            System.out.println(result);
        }
    }
}
