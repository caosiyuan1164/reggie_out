package com.itheima.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.BaseContext;
import com.itheima.common.R;
import com.itheima.domain.OrderDetail;
import com.itheima.domain.Orders;
import com.itheima.domain.ShoppingCart;
import com.itheima.dto.OrderDto;
import com.itheima.service.IOrderDetailService;
import com.itheima.service.IOrdersService;
import com.itheima.service.IShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author csy
 * @since 2023-04-14
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private IOrdersService service;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IShoppingCartService shoppingCartService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        //获得当前用户id
        Long userId = BaseContext.getCurrentId();
        System.out.println(orders);
        //将当前订单提交到订单表
        service.submit(orders,userId);//保存后orders对象被封装完好

        //同时保存到订单细节表
        //首先根据userId将shoppingcart中数据查出来
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        //创建OrderDetails的List,用于之后保存
        List<OrderDetail> orderDetails = new ArrayList<>();
        //遍历shoppingCarts
        for (int i = 0; i < shoppingCarts.size(); i++) {
            ShoppingCart shoppingCart = shoppingCarts.get(i);
            //创建orderDetails对象
            OrderDetail orderDetail = new OrderDetail();
            //赋值
            orderDetail.setAmount(shoppingCart.getAmount());
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            orderDetail.setDishId(shoppingCart.getDishId());
            orderDetail.setImage(shoppingCart.getImage());
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetail.setSetmealId(shoppingCart.getSetmealId());
            //设置的名称是菜品的名称
            orderDetail.setName(shoppingCart.getName());
            orderDetail.setOrderId(orders.getId());
            //将该orderDetail添加到orderDetails中
            orderDetails.add(orderDetail);
        }
        orderDetailService.saveBatch(orderDetails);
        //清空当前用户购物车
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);

        return R.success("支付成功！");
    }

    @GetMapping("/userPage")
    public R<Page<OrderDto>> userPage(Integer page, Integer pageSize){
        //当前用户
        Long userId = BaseContext.getCurrentId();
        //创建page对象
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        //分页查询
        //设置条件查询
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,userId);
        //查询
        service.page(pageInfo, queryWrapper);

        //要转换成OrderDto的page对象
        Page<OrderDto> orderDtoPage = new Page<>();
        //复制属性
        BeanUtils.copyProperties(pageInfo,orderDtoPage,"records");
        //获取pageInfo的records
        List<Orders> orders = pageInfo.getRecords();
        //创建OrderDto的List
        List<OrderDto> orderDtos = new ArrayList<>();
        //遍历orders
        for (int i = 0; i < orders.size(); i++) {
            //获取每个order
            Orders order = orders.get(i);
            //拷贝属性
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order,orderDto);
            //获取order的id，根据这个id去查detail
            Long orderId = order.getId();
            //查询detail
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId,orderId);
            //查询
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailLambdaQueryWrapper);
            //设置到orderDto中去
            orderDto.setOrderDetails(orderDetails);
            //将orderDto添加到orderDtos
            orderDtos.add(orderDto);

        }

        //将orderDtos作为orderDtoPage的records
        orderDtoPage.setRecords(orderDtos);

        return R.success(orderDtoPage);
    }
}

