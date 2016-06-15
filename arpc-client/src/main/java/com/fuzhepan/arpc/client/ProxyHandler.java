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

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;


public class ProxyHandler implements InvocationHandler, Serializable {

    private String serviceName;

    private Log log = LogFactory.getLog(ProxyHandler.class);

    public ProxyHandler(String serviceName){
        this.serviceName = serviceName;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("invoke was called!");

        if(method.getName().equals("toString")){
            return "toString method was called";
        }

        RpcContext rpcContext = new RpcContext(serviceName,method.getName(),method.getParameterTypes(),args);
        Socket socket = new Socket("localhost",8081);

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
