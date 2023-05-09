package com.itheima.dao;

import com.itheima.domain.DishFlavor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜品口味关系表 Mapper 接口
 * </p>
 *
 * @author csy
 * @since 2023-03-26
 */
@Mapper
public interface DishFlavorDao extends BaseMapper<DishFlavor> {

}
