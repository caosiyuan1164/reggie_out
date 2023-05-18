package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.domain.Category;
import com.itheima.domain.Dish;
import com.itheima.dao.DishDao;
import com.itheima.domain.DishFlavor;
import com.itheima.dto.DishDto;
import com.itheima.service.ICategoryService;
import com.itheima.service.IDishFlavorService;
import com.itheima.service.IDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author csy
 * @since 2023-03-23
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements IDishService {

    @Autowired
    private IDishFlavorService flavorService;

    @Autowired
    private ICategoryService categoryService;

    @Override
    @Transactional
    public void saveWithFlavors(DishDto dishDto) {
        //1.将菜品存入dish表
        this.save(dishDto);
        //2.获取菜品id
        Long id = dishDto.getId();
        //3.遍历dishDto中的flavor列表，将id设置进去
        for (int i = 0; i < dishDto.getFlavors().size(); i++) {
            dishDto.getFlavors().get(i).setDishId(id);
        }

        //4.保存菜品口味到dish_flavors表
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavorService.saveBatch(flavors);
    }

    @Override
    public Page<DishDto> pageWithCategoryName(Integer page, Integer pageSize, String name) {
        //创建page对象
        Page<Dish> pageInfo = new Page<Dish>(page, pageSize);
        //创建queryWrapper
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<Dish>();
        //条件查询，模糊查询
        queryWrapper.like(name != null, Dish::getName, name);
        //排序
        queryWrapper.orderByDesc(Dish::getCreateTime);

        //调用service查询
        this.page(pageInfo, queryWrapper);

        //对pageInfo进行处理，首先将pageInfo中的除了records外的其它属性复制出来
        Page<DishDto> dishDtoPage = new Page<DishDto>();
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        //获得pageInfo中的records
        List<Dish> dishes = pageInfo.getRecords();
        //遍历该list，将数据放到一个DishDto的list中去
        List<DishDto> dishDtos = new ArrayList<DishDto>();
        for (int i = 0; i < dishes.size(); i++) {
            Dish dish = dishes.get(i);
            Long categoryId = dish.getCategoryId();
            Category category = categoryService.getById(categoryId);
            DishDto dishDto = new DishDto();
            if (category != null) {
                String categoryName = category.getName();
                //新建一个DishDto对象，将dish中的属性复制过去
                BeanUtils.copyProperties(dish, dishDto);
                dishDto.setCategoryName(categoryName);
            }


            //添加到dishDtos中去
            dishDtos.add(dishDto);
        }

        //将dishDtos设置为最终page的records
        dishDtoPage.setRecords(dishDtos);

        //调用service查询
        return dishDtoPage;
    }

    @Override
    public DishDto getByIdWithFlavors(Long id) {
        //service根据id查询dish
        Dish dish = this.getById(id);
        //新创建一个DishDto
        DishDto dishDto = new DishDto();
        //将查询到的dish的属性复制给dto
        BeanUtils.copyProperties(dish, dishDto);
        //获取dish的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<DishFlavor>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> list = flavorService.list(queryWrapper);
        //设置到dishDto中去
        dishDto.setFlavors(list);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavors(DishDto dishDto) {
        //1.更新dish表
        this.updateById(dishDto);
        //2.清理之前的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<DishFlavor>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        flavorService.remove(queryWrapper);

        //3.将修改后的口味添加到dishFlavor表中
        List<DishFlavor> flavors = dishDto.getFlavors();
        //因为如果是将之前的口味删除了重新添加的话，那么新传过来的口味是没有dishId的属性的，所以这里要将这些flavor重新设置dishId
        for (int i = 0; i < flavors.size(); i++) {
            flavors.get(i).setDishId(dishDto.getId());
        }

        flavorService.saveBatch(flavors);
    }
}
