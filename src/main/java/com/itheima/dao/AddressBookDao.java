package com.itheima.dao;

import com.itheima.domain.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 地址管理 Mapper 接口
 * </p>
 *
 * @author csy
 * @since 2023-04-08
 */
@Mapper
public interface AddressBookDao extends BaseMapper<AddressBook> {

}
