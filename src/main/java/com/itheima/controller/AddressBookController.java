package com.itheima.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.common.BaseContext;
import com.itheima.common.R;
import com.itheima.domain.AddressBook;
import com.itheima.domain.User;
import com.itheima.service.IAddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 * 地址管理 前端控制器
 * </p>
 *
 * @author csy
 * @since 2023-04-08
 */
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private IAddressBookService service;

    /**
     * 查询当前用户的所有地址
     * @param request
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(HttpServletRequest request){
        //根据request获得session
        HttpSession session = request.getSession();
        //从session中获取当前登录的user的id
        Long userId = (Long) session.getAttribute("user");
        //查询地址表
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,userId);
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> addressBooks = service.list(queryWrapper);

        return R.success(addressBooks);
    }

    /**
     * 保存地址
     * @param request
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody AddressBook addressBook){
        //设置userId
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("user");
        addressBook.setUserId(userId);
        //直接保存
        service.save(addressBook);
        //返回
        return R.success("地址保存成功！");
    }

    @PutMapping("/default")
    public R<String> setDefault(HttpServletRequest request, @RequestBody User user){
        //首先查询当前用户下的默认地址，如果有，那么将之改为非默认
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("user");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,userId).eq(AddressBook::getIsDefault,1);
        AddressBook preDefault = service.getOne(queryWrapper);
        if (preDefault != null){
            //设置为非默认
            preDefault.setIsDefault(0);
            //更新
            service.updateById(preDefault);
        }
        //将传过来的地址设置为默认
        //先查询
        AddressBook currentDefault = service.getById(user.getId());
        //设置
        currentDefault.setIsDefault(1);
        //更新
        service.updateById(currentDefault);

        //返回
        return R.success("设置默认成功");
    }

    /**
     * 回显地址信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<AddressBook> getAddress(@PathVariable Long id){
        System.out.println(id);

        //根据id查询表
        AddressBook address = service.getById(id);

        return R.success(address);
    }

    /**
     * 删除地址
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteAddress(@RequestParam List<Long> ids){
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AddressBook::getId,ids);

        service.remove(queryWrapper);

        return R.success("删除成功！");
    }

    /**
     * 修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<String> updateAddress(@RequestBody AddressBook addressBook){
        service.updateById(addressBook);

        return R.success("更新成功！");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefaultAddress(){
        //获取当前用户
        Long userId = BaseContext.getCurrentId();
        //根据当前用户id，以及default值查询默认地址
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(AddressBook::getUserId,userId);
        queryWrapper.eq(AddressBook::getIsDefault,1);
        //查询
        AddressBook defaultAddress = service.getOne(queryWrapper);

        //返回
        return R.success(defaultAddress);
    }
}

