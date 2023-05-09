package com.itheima.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.domain.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.dto.DishDto;

/**
 * <p>
 * 菜品管理 服务类
 * </p>
 *
 * @author csy
 * @since 2023-03-23
 */
public interface IDishService extends IService<Dish> {
    public void saveWithFlavors(DishDto dishDto);

    public Page<DishDto> pageWithCategoryName(Integer page, Integer pageSize, String name);

    public DishDto getByIdWithFlavors(Long id);

    public void updateWithFlavors(DishDto dishDto);
}
