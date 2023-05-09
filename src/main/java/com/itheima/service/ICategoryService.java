package com.itheima.service;

import com.itheima.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜品及套餐分类 服务类
 * </p>
 *
 * @author csy
 * @since 2023-03-22
 */
public interface ICategoryService extends IService<Category> {
    void remove(Long ids);
}
