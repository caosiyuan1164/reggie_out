package com.itheima.dto;

import com.itheima.domain.OrderDetail;
import com.itheima.domain.Orders;

import java.util.List;

public class OrderDto extends Orders {
    private List<OrderDetail> orderDetails;

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "orderDetails=" + orderDetails +
                '}' + super.toString();
    }
}
