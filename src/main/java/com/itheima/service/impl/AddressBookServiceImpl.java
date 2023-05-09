package com.itheima.service.impl;

import com.itheima.domain.AddressBook;
import com.itheima.dao.AddressBookDao;
import com.itheima.service.IAddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author csy
 * @since 2023-04-08
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookDao, AddressBook> implements IAddressBookService {

}
