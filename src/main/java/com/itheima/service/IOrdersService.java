package com.itheima.service;

import com.itheima.domain.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author csy
 * @since 2023-04-14
 */
public interface IOrdersService extends IService<Orders> {

    /**
     * 提交订单方法
     *
     * @param orders
     */
    void submit(Orders orders, Long userId);
}
