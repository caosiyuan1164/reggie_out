package com.itheima.service.impl;

import com.itheima.domain.OrderDetail;
import com.itheima.dao.OrderDetailDao;
import com.itheima.service.IOrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author csy
 * @since 2023-04-14
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailDao, OrderDetail> implements IOrderDetailService {

}
