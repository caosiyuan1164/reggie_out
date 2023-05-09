package com.itheima.dao;

import com.itheima.domain.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author csy
 * @since 2023-04-14
 */
@Mapper
public interface OrdersDao extends BaseMapper<Orders> {

}
