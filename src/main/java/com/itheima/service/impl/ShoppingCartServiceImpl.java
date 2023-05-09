package com.itheima.service.impl;

import com.itheima.domain.ShoppingCart;
import com.itheima.dao.ShoppingCartDao;
import com.itheima.service.IShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author csy
 * @since 2023-04-10
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements IShoppingCartService {

}
