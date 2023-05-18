package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.common.BaseContext;
import com.itheima.common.CustomException;
import com.itheima.domain.AddressBook;
import com.itheima.domain.Orders;
import com.itheima.dao.OrdersDao;
import com.itheima.domain.ShoppingCart;
import com.itheima.domain.User;
import com.itheima.service.IAddressBookService;
import com.itheima.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.service.IShoppingCartService;
import com.itheima.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author csy
 * @since 2023-04-14
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements IOrdersService {
    @Autowired
    private IUserService userService;
    @Autowired
    private IAddressBookService addressBookService;
    @Autowired
    private IShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public void submit(Orders orders, Long userId) {
        //根据用户id查询用户姓名
        User user = userService.getById(userId);
        //设置order的username和userId和phone
        orders.setUserId(userId);
        orders.setUserName(user.getName());
        //根据已有的addressBookId查询地址表
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null) {
            throw new CustomException("地址信息有误，不能下单");
        }
        //设置地址和收货人还有phone
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        //查询shoppingCart表，设置份数和金额
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //设置查询条件-----userId
        shoppingCartLambdaQueryWrapper.eq(userId != null, ShoppingCart::getUserId, userId);
        //查询份数和金额
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        //判断，如果购物车为空，那么不应该下单
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空，下单失败");
        }
        //初始化份数和金额
        Integer number = 0;
        //BigDecimal amount = new BigDecimal(0);
        //原子性操作
        AtomicInteger amount = new AtomicInteger(0);
        //遍历，求得总的number和amount
        for (int i = 0; i < shoppingCarts.size(); i++) {
            ShoppingCart shoppingCart = shoppingCarts.get(i);
            number += shoppingCart.getNumber();
            //amount = amount.add(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())));
            amount.getAndAdd(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())).intValue());
        }
        //将amount设置到order中去
        orders.setNumber(number.toString());
        orders.setAmount(new BigDecimal(amount.intValue()));

        //最后设置orderTime
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());

        //保存
        this.save(orders);

    }
}
