package com.itheima.dao;

import com.itheima.domain.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 员工信息 Mapper 接口
 * </p>
 *
 * @author csy
 * @since 2023-03-12
 */
@Mapper//dao要加这个，不然spring识别不出来这个bean
public interface EmployeeDao extends BaseMapper<Employee> {

}
