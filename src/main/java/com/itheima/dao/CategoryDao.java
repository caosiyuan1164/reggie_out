package com.itheima.dao;

import com.itheima.domain.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜品及套餐分类 Mapper 接口
 * </p>
 *
 * @author csy
 * @since 2023-03-22
 */
@Mapper
public interface CategoryDao extends BaseMapper<Category> {

}
