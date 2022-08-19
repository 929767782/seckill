package com.llp.kill.rpc;

import com.llp.kill.remote.GoodsService;
import com.llp.kill.remote.OrderService;
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
    public GoodsService getGoodsService(){
        RpcClientManager clientManager = new RpcClientManager();
        GoodsService service = new ClientProxy(clientManager).getProxyService(GoodsService.class);
        return service;
    }

}
