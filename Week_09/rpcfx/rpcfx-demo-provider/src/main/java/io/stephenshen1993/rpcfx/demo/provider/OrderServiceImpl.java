package io.stephenshen1993.rpcfx.demo.provider;

import io.stephenshen1993.rpcfx.demo.api.Order;
import io.stephenshen1993.rpcfx.demo.api.OrderService;

public class OrderServiceImpl implements OrderService {

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "Cuijing" + System.currentTimeMillis(), 9.9f);
    }
}
