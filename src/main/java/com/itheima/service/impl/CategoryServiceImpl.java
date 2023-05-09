package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.common.CustomException;
import com.itheima.common.R;
import com.itheima.domain.Category;
import com.itheima.dao.CategoryDao;
import com.itheima.domain.Dish;
import com.itheima.domain.Setmeal;
import com.itheima.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.service.IDishService;
import com.itheima.service.ISetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜品及套餐分类 服务实现类
 * </p>
 *
 * @author csy
 * @since 2023-03-22
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements ICategoryService {
    @Autowired
    private ISetmealService setmealService;
    @Autowired
    private IDishService dishService;

    @Override
    public void remove(Long ids) {
        //条件查询构造器
        LambdaQueryWrapper<Setmeal> queryWrapperSetMeal = new LambdaQueryWrapper<Setmeal>();
        LambdaQueryWrapper<Dish> queryWrapperDish = new LambdaQueryWrapper<Dish>();

        queryWrapperSetMeal.eq(Setmeal::getCategoryId,ids);
        queryWrapperDish.eq(Dish::getCategoryId,ids);

        List<Setmeal> setmeals = setmealService.list(queryWrapperSetMeal);
        List<Dish> dishes = dishService.list(queryWrapperDish);

        //判断
        if ((setmeals.size() == 0 || setmeals == null) && (dishes.size() == 0 || dishes == null)){
            super.removeById(ids);
        }else {
            throw new CustomException("删除失败！当前分类仍有套餐或者菜品");
        }
    }
}
