import com.fuzhepan.arpc.client.ProxyFactory;

/**
 * Created by FuzhePan on 2016/6/15.
 */
public class Consumer {

    public static void  main(String[] args){

        String url="rpc://example/HelloWorld";
        HelloWorld service = ProxyFactory.create(url,HelloWorld.class);
        String result = service.sayHello();

        System.out.println(result);

    }
}
