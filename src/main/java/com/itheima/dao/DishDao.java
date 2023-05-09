package com.itheima.dao;

import com.itheima.domain.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜品管理 Mapper 接口
 * </p>
 *
 * @author csy
 * @since 2023-03-23
 */
@Mapper
public interface DishDao extends BaseMapper<Dish> {

}
