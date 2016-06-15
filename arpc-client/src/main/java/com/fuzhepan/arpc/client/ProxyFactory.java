package com.fuzhepan.arpc.client;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

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

public class ProxyFactory {

    private static ConcurrentHashMap<String,Object> proxyCache = new ConcurrentHashMap();

    public static <T> T create(String url,Class classType){
        String cacheKey = url.toLowerCase();
        if(proxyCache.containsKey(cacheKey))
            return (T) proxyCache.get(cacheKey);

        Object proxy = createProxy(url,classType);
        if(proxy!=null)
            proxyCache.put(url,proxy);

        return (T) proxy;

    }

    private static Object createProxy(String url,Class classType){
        url = url.replace("rpc://", "");
        String[] splits = url.split("/");
        String serviceName = splits[1];

        ProxyHandler proxyHandler = new ProxyHandler(serviceName);
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{classType}, proxyHandler);
    }
}
