package com.itheima.dao;

import com.itheima.domain.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 购物车 Mapper 接口
 * </p>
 *
 * @author csy
 * @since 2023-04-10
 */
@Mapper
public interface ShoppingCartDao extends BaseMapper<ShoppingCart> {

}
