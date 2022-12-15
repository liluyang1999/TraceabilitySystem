package com.example.server_framework.web.rbac.mapper;

import com.example.server_framework.web.rbac.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * DAO: Department Mapper
 */
@Mapper
public interface DeptMapper {

    List<Department> selectDeptsAll();

    Department selectDeptById(@Param("id") Long id);

    int insertDept(Department dept);

    int updateDept(Department dept);

    int deleteDeptById(@Param("id") Long id);
}
