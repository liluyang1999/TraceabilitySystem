package com.example.server_framework.web.rbac.service;

import com.example.server_framework.web.rbac.entity.Department;
import com.example.server_framework.web.rbac.mapper.DeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service: Department
 */
@Service
public class DeptService {

    @Autowired
    private DeptMapper deptMapper;

    public List<Department> getAllDepts() {
        return deptMapper.selectDeptsAll();
    }

    public Department getDeptById(Long id) {
        return deptMapper.selectDeptById(id);
    }

    public boolean addDept(Department dept) {
        return deptMapper.insertDept(dept) == 1;
    }

    public boolean updateDept(Department dept) {
        return deptMapper.updateDept(dept) == 1;
    }

    public boolean removeDept(Department dept) {
        return deptMapper.deleteDeptById(dept.getId()) == 1;
    }
}
