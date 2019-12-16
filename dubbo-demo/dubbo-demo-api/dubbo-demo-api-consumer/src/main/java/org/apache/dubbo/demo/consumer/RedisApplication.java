package org.apache.dubbo.demo.consumer;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.demo.DemoService;

/**
 * Description:
 *
 * @author: chixiao
 * @date: 2019-12-03
 * @time: 16:03
 */
public class RedisApplication {
    public static void main(String[] args) {
        ReferenceConfig<DemoService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("dubbo-demo-api-consumer"));
        reference.setRegistry(new RegistryConfig("redis://127.0.0.1:6379?password=12346"));
        reference.setInterface(DemoService.class);
        DemoService service = reference.get();
        String message = service.sayHello("dubbo");
        System.out.println(message);
    }
}
