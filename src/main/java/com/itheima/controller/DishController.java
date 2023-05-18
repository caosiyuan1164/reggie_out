package com.itheima.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.domain.Dish;
import com.itheima.domain.DishFlavor;
import com.itheima.dto.DishDto;
import com.itheima.service.IDishFlavorService;
import com.itheima.service.IDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author csy
 * @since 2023-03-23
 */
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private IDishService service;

    @Autowired
    private IDishFlavorService flavorService;

    /**
     * 添加菜品
     *
     * @param dto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dto) {
        System.out.println("dto = " + dto.toString());
        service.saveWithFlavors(dto);
        return R.success("添加成功！");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name) {
        //调用service中的方法查询出dishDtoPage
        Page<DishDto> dishDtoPage = service.pageWithCategoryName(page, pageSize, name);

        System.out.println(dishDtoPage);

        return R.success(dishDtoPage);

    }

    /**
     * 根据id查询菜品及其口味
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = service.getByIdWithFlavors(id);

        return R.success(dishDto);
    }

    /**
     * 修改菜品功能
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        service.updateWithFlavors(dishDto);

        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        //根据categoryId查询该类别的菜品
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();

        //条件查询器
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //根据姓名查询
        queryWrapper.like(dish.getName() != null, Dish::getName, dish.getName());
        //查询未被禁售的菜品
        queryWrapper.eq(Dish::getStatus, 1);

        //设置排序
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        //调用service进行查询
        List<Dish> dishes = service.list(queryWrapper);
        //创建DishDto的List
        List<DishDto> dishDtos = new ArrayList<>();
        for (int i = 0; i < dishes.size(); i++) {
            //创建DishDto的对象
            DishDto dishDto = new DishDto();
            //拷贝属性
            BeanUtils.copyProperties(dishes.get(i), dishDto);
            //查询口味
            LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //查询条件
            flavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
            //查询
            List<DishFlavor> flavors = flavorService.list(flavorLambdaQueryWrapper);
            //设置dishDto的flavors属性
            dishDto.setFlavors(flavors);
            //添加到list集合
            dishDtos.add(dishDto);
        }

        //返回
        return R.success(dishDtos);
    }
}

