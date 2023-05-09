package com.itheima.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.common.BaseContext;
import com.itheima.common.R;
import com.itheima.domain.ShoppingCart;
import com.itheima.service.IShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author csy
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private IShoppingCartService service;

    /**
     * 添加菜品到购物车方法
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<String> add(@RequestBody ShoppingCart shoppingCart){
        //首先将当前用户的id取出来
        Long userId = BaseContext.getCurrentId();

        //首先要判断，当前是否已经在该用户下添加了该菜品，如果添加了，那么只是添加数量和钱数
        //查询器
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(shoppingCart.getDishId() != null,ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId() != null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        //查询
        ShoppingCart existOne = service.getOne(queryWrapper);

        //判断表中该用户下是否有这个菜品
        if (existOne == null){
            //该用户下没有添加该菜品，那么添加该菜品
            //将userId设置到shoppingCart中去
            shoppingCart.setUserId(userId);
            //设置创建时间
            shoppingCart.setCreateTime(LocalDateTime.now());

            //保存
            service.save(shoppingCart);

            return R.success("添加成功！");
        }
        //该用户下已经添加了该菜品，那么只需要添加他的数量
        existOne.setNumber(existOne.getNumber() + 1);
        //更新
        service.updateById(existOne);
        //返回
        return R.success("添加成功！");
    }

    /**
     * 显示购物车方法
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        //首先获取当前user的id
        Long userId = BaseContext.getCurrentId();
        //创建查询器
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //查询条件
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        //查询
        List<ShoppingCart> shoppingCarts = service.list(queryWrapper);
        //返回
        return R.success(shoppingCarts);
    }

    /**
     * 购物车中减少一份菜品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        //首先获取当前userId
        Long userId = BaseContext.getCurrentId();
        //查询器
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //查询条件
        queryWrapper.eq(shoppingCart.getDishId() != null,ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId() != null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        //查询
        ShoppingCart existOne = service.getOne(queryWrapper);
        if (existOne.getNumber() == 1){
            //如果该菜品或套餐只有1分，那么删除需要将该菜品从表中删除
            service.removeById(existOne.getId());
            return R.success("减少成功！");
        }
        //数量不止一份，将数量减一
        existOne.setNumber(existOne.getNumber() - 1);
        //更新
        service.updateById(existOne);
        return R.success("减少成功！");
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        //首先获取当前userId
        Long userId = BaseContext.getCurrentId();
        //清空当前用户购物车中所有内容
        //查询器
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        //清除所有
        service.remove(queryWrapper);
        //返回
        return R.success("清空成功！");
    }

}

