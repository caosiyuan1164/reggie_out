package com.itheima.service.impl;

import com.itheima.domain.UserCode;
import com.itheima.dao.UserCodeDao;
import com.itheima.service.IUserCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author csy
 * @since 2023-04-06
 */
@Service
public class UserCodeServiceImpl extends ServiceImpl<UserCodeDao, UserCode> implements IUserCodeService {

}
