/*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.fuzhepan.arpc.client;

import com.fuzhepan.arpc.common.RpcContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class ProxyHandler implements InvocationHandler, Serializable {

    private String serviceName;
    private String interfaceName;

    private static AtomicInteger requestCount = new AtomicInteger(0);

    private Log log = LogFactory.getLog(ProxyHandler.class);

    public ProxyHandler(String serviceName,String interfaceName){
        this.serviceName = serviceName;
        this.interfaceName = interfaceName;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.debug("invoke was called!");

        if(method.getName().equals("toString")){
            return "toString method was called";
        }

        RpcContext rpcContext = new RpcContext(interfaceName,method.getName(),method.getParameterTypes(),args);

        //get service info and load balance
        List<HostPortPair> serviceList =  RpcConfig.getServiceList(serviceName);
        if(serviceList== null || serviceList.size() ==0)
            throw new ClassNotFoundException("not find service : "+serviceName);
        int index = requestCount.get() % serviceList.size();
        if(requestCount.get()>100)
            requestCount.set(0);
        else
            requestCount.getAndIncrement();
        HostPortPair hostPort = serviceList.get(index);

        Socket socket = new Socket(hostPort.host,hostPort.port);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(rpcContext);
        objectOutputStream.flush();

        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        Object response = objectInputStream.readObject();

        objectInputStream.close();
        objectOutputStream.close();
        socket.close();

        Class methodReturnType = method.getReturnType();
        return methodReturnType.cast(response);
    }
}
