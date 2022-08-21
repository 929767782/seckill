package com.llp.user.rpc;


import com.llp.rpc.client.manager.ClientProxy;
import com.llp.rpc.client.manager.RpcClientManager;
import com.llp.user.remote.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcFactory {
    @Bean
    public OrderService getOrderService(){
        RpcClientManager clientManager = new RpcClientManager();
        OrderService service = new ClientProxy(clientManager).getProxyService(OrderService.class);
        return service;
    }

}
