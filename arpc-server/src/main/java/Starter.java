import com.fuzhepan.arpc.common.RpcContext;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务启动器
 */
public class Starter {

    /**
     * 启动服务
     * @param port
     */
    public void start(int port) throws IOException {
        if (port <= 0 || port > 65535)
            throw new IllegalArgumentException("Invalid port " + port);

        ServerSocket server = new ServerSocket(port);
        while(true){
            final Socket socket = server.accept();
            new Thread(new Runnable(){
                public void run() {
                    try {
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        RpcContext context = (RpcContext)input.readObject();

                        Class serviceClass = ServiceMap.getService(context.interfaceName);
                        Method method = serviceClass.getMethod(context.methodName, context.parameterTypes);
                        Object result = method.invoke(serviceClass.newInstance(), context.parameterValues);

                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                        output.writeObject(result);

                        output.close();
                        input.close();
                        socket.close();

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
