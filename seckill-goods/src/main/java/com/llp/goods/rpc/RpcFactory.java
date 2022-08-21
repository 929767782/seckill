package com.llp.goods.rpc;

import com.llp.goods.remote.OrderService;
import com.llp.goods.remote.UserService;
import com.llp.rpc.client.manager.ClientProxy;
import com.llp.rpc.client.manager.RpcClientManager;
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

    @Bean
    public UserService getUserService(){
        RpcClientManager clientManager = new RpcClientManager();
        UserService service = new ClientProxy(clientManager).getProxyService(UserService.class);
        return service;
    }

}
