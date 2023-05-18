package com.itheima.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.domain.Category;
import com.itheima.domain.Dish;
import com.itheima.domain.Employee;
import com.itheima.domain.Setmeal;
import com.itheima.service.ICategoryService;
import com.itheima.service.IDishService;
import com.itheima.service.ISetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜品及套餐分类 前端控制器
 * </p>
 *
 * @author csy
 * @since 2023-03-22
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private ICategoryService service;
    @Autowired
    private ISetmealService setmealService;
    @Autowired
    private IDishService dishService;

    @PostMapping
    public R<String> addNew(@RequestBody Category category) {
        //保存
        service.save(category);
        return R.success("添加成功！");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page<Category>> getPage(Integer page, Integer pageSize) {
        System.out.println("page = " + page);
        System.out.println("pageSize = " + pageSize);
        Page<Category> pageInfo = new Page<Category>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>();
        //按照更新时间降序查询
        queryWrapper.orderByDesc(Category::getUpdateTime);
        //不需要返回值
        service.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids) {
        service.remove(ids);

        return R.success("删除成功！");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        service.updateById(category);

        return R.success("修改成功！");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> res = service.list(queryWrapper);
        return R.success(res);
    }
}

