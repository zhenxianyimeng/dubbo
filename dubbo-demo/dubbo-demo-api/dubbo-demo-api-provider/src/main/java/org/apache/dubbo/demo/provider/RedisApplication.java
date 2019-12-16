package org.apache.dubbo.demo.provider;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.demo.DemoService;

import java.io.IOException;

/**
 * Description:
 *
 * @author: zhenxianyimeng
 * @date: 2019-12-03
 * @time: 15:43
 */
public class RedisApplication {
    public static void main(String[] args) throws IOException {
        ServiceConfig<DemoServiceImpl> service = new ServiceConfig<>();
//        service.setProtocol(new ProtocolConfig("redis"));
        service.setApplication(new ApplicationConfig("dubbo-demo-api-provider"));
        service.setRegistry(new RegistryConfig("redis://127.0.0.1:6379?password=12346"));
        service.setInterface(DemoService.class);
        service.setRef(new DemoServiceImpl());
        service.export();
        System.in.read();
    }
}
