package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.CustomException;
import com.itheima.common.R;
import com.itheima.domain.Category;
import com.itheima.domain.Setmeal;
import com.itheima.dao.SetmealDao;
import com.itheima.domain.SetmealDish;
import com.itheima.dto.SetMealDto;
import com.itheima.service.ICategoryService;
import com.itheima.service.ISetmealDishService;
import com.itheima.service.ISetmealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 *
 * @author csy
 * @since 2023-03-23
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealDao, Setmeal> implements ISetmealService {

    @Autowired
    private ISetmealDishService setmealDishService;

    @Autowired
    private ICategoryService categoryService;

    @Override
    @Transactional
    public void saveWithDishes(SetMealDto setMealDto) {
        //首先将本表的对应的信息存入进去
        this.save(setMealDto);

        //获得本套餐对应的菜品信息
        List<SetmealDish> setmealDishes = setMealDto.getSetmealDishes();

        //遍历该list，将setmeal的id设置进去，因为传过来没有这个属性，而上面存到setmeal表的时候已经将id赋值给setmealDto对象
        for (int i = 0; i < setmealDishes.size(); i++) {
            setmealDishes.get(i).setSetmealId(setMealDto.getId().toString());
        }

        //调用setmealDishService来存储setmealDishes
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public Page<SetMealDto> pageWithCategoryName(Integer page, Integer pageSize, String name) {
        //首先创建page对象
        Page<Setmeal> pageInfo = new Page<Setmeal>(page,pageSize);
        //设置排序顺序
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<Setmeal>();
        //根据名称模糊查询
        queryWrapper.like(name != null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        //调用service查询
        this.page(pageInfo,queryWrapper);

        //创建SetMealDto的page对象
        Page<SetMealDto> setMealDtoPage = new Page<>();
        //将pageInfo中的除了records的属性复制过来
        BeanUtils.copyProperties(pageInfo,setMealDtoPage,"records");

        //创建SetMealDto的list
        List<SetMealDto> setMealDtoRecords = new ArrayList<>();
        //获取pageInfo中的records
        List<Setmeal> setmealRecords = pageInfo.getRecords();
        //遍历setmealRecords，将每个对象复制到setMealDtoRecords，并根据categoryId查询categoryName
        for (int i = 0; i < setmealRecords.size(); i++) {
            Setmeal setmeal = setmealRecords.get(i);
            //创建一个新的SetMealDto对象，将setmeal中属性复制过去
            SetMealDto setMealDto = new SetMealDto();
            BeanUtils.copyProperties(setmeal,setMealDto);
            //获取categoryId
            Long categoryId = setmeal.getCategoryId();
            //查询categoryName
            Category category = categoryService.getById(categoryId);
            if (category != null){
                //取出name
                String categoryName = category.getName();
                //赋值给setMealDto
                setMealDto.setCategoryName(categoryName);
            }
            //将setMealDto添加到setMealDtoRecords
            setMealDtoRecords.add(setMealDto);
        }
        //将setMealDtoRecords赋值给setMealDtoPage
        setMealDtoPage.setRecords(setMealDtoRecords);

        return setMealDtoPage;
    }

    @Override
    @Transactional
    public void removeWithFlavors(List<Long> ids) {
        //可以使用这样一个sql语句
        //select count(*) from setmeal where id in ids and status = 1 来判断是否有在启售状态的
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        //查询
        int count = this.count(queryWrapper);
        if (count > 0){
            //存在启售状态的套餐
            //抛出异常
            throw new CustomException("所选套餐中有在售套餐，删除失败！");
        }

        //无在启售中的商品的话那么删除对应的套餐和对应关系
        this.removeByIds(ids);

        //同时去删除对应关系
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper1);


        //遍历该list集合
        /*for (int i = 0; i < list.size(); i++) {
            Setmeal setmeal = list.get(i);
            //逻辑删除本表中的数据
            this.removeById(setmeal.getId());
            //去删除setmeal中的对应的数据，也是逻辑删除
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());

            //调用service删除
            setmealDishService.remove(queryWrapper);

        }*/
    }
}
