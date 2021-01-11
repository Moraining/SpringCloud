package com.jiay.eurekaconsumer;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class MainController {
    
    DiscoveryClient client;
    @Autowired
    public void setClient (DiscoveryClient client) {
        this.client = client;
    }
    EurekaClient client2;
    @Autowired
    public void setClient2 (@Qualifier("eurekaClient") EurekaClient client2) {
        this.client2 = client2;
    }
    
    LoadBalancerClient lb;
    @Autowired
    public void setLb (LoadBalancerClient lb) {
        this.lb = lb;
    }
    
    @GetMapping("/getHi")
    public String getHi() {
        return "hi";
    }
    
    @GetMapping("/client")
    public String client() {
        List<String> services = client.getServices();
        for (String str : services) {
            System.out.println(str);
        }
        return "hi";
    }
    
    @GetMapping("/client2")
    public Object client2() {
        return client.getInstances("provider");
    }
    
    @GetMapping("/client3")
    public Object client3() {
        List<ServiceInstance> instances = client.getInstances("provider");
        for (ServiceInstance instance : instances) {
            System.out.println(ToStringBuilder.reflectionToString(instance));
        }
        return "xojo";
    }
    
    @GetMapping("/client4")
    public Object client4() {
        List<InstanceInfo> instances = client2.getInstancesByVipAddress("provider", false);
        for (InstanceInfo instance : instances) {
            System.out.println(instance);
        }
        
        
        if (instances.size() > 0) {
            InstanceInfo instanceInfo = instances.get(0);
            if (instanceInfo.getStatus() == InstanceInfo.InstanceStatus.UP) {
                String url = "http://" + instanceInfo.getHostName() + ":" + instanceInfo.getPort() + "/getHi";
                
                
                RestTemplate template = new RestTemplate();
                String respStr = template.getForObject(url, String.class);
                System.out.println("respStr---" + respStr);
            }
        }
        return "xojo";
    }
    
    @GetMapping("/client5")
    public Object client5() {
        
        ServiceInstance instance = lb.choose("provider");
    
        System.out.println(ToStringBuilder.reflectionToString(instance));
        
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/getHi";
        
        RestTemplate template = new RestTemplate();
        String respStr = template.getForObject(url, String.class);
        System.out.println("respStr---" + respStr);
        
        return "xojo";
    }
}
