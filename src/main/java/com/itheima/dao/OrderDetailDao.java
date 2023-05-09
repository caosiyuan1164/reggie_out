package com.itheima.dao;

import com.itheima.domain.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单明细表 Mapper 接口
 * </p>
 *
 * @author csy
 * @since 2023-04-14
 */
@Mapper
public interface OrderDetailDao extends BaseMapper<OrderDetail> {

}
