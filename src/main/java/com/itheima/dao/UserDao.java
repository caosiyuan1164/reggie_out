package com.itheima.dao;

import com.itheima.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author csy
 * @since 2023-04-05
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

}
