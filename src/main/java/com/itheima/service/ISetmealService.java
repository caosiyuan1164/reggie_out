package com.itheima.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.domain.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.dto.SetMealDto;

import java.util.List;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author csy
 * @since 2023-03-23
 */
public interface ISetmealService extends IService<Setmeal> {

    void saveWithDishes(SetMealDto setMealDto);

    Page<SetMealDto> pageWithCategoryName(Integer page, Integer pageSize, String name);

    void removeWithFlavors(List<Long> ids);
}
