package com.itheima.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.domain.Dish;
import com.itheima.domain.Setmeal;
import com.itheima.dto.SetMealDto;
import com.itheima.service.ISetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author csy
 * @since 2023-03-23
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private ISetmealService service;

    /**
     * 添加菜品的套餐的方法
     *
     * @param setMealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetMealDto setMealDto) {
        service.saveWithDishes(setMealDto);

        return R.success("添加套餐成功！");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetMealDto>> page(Integer page, Integer pageSize, String name) {
        //调用service中的自定义方法，除了查询setmeal的信息外，同时要查询该套餐的分类
        Page<SetMealDto> pageInfo = service.pageWithCategoryName(page, pageSize, name);

        return R.success(pageInfo);
    }

    /**
     * 批量起售，停售方法
     *
     * @param statusVal
     * @param ids
     * @return
     */
    @PostMapping("/status/{statusVal}")
    public R<String> changeStatus(@PathVariable Integer statusVal, Long[] ids) {
        //遍历ids,将之存入对象中去，同时存入status，并存到一个list集合中
        List<Setmeal> list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(ids[i]);
            setmeal.setStatus(statusVal);
            list.add(setmeal);
        }

        //调用service批量更新
        service.updateBatchById(list);
        return R.success("启售/停售成功！");
    }

    /**
     * 删除，批量删除方法
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        service.removeWithFlavors(ids);
        return R.success("删除成功！");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        //创建查询器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //查询条件
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //查询
        List<Setmeal> setmeals = service.list(queryWrapper);
        //返回
        return R.success(setmeals);
    }
}

