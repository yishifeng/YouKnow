package com.ysf.dao.role;

import java.util.List;

import com.ysf.entity.role.Sys_permissionTree;

public interface Sys_permissionTreeMapper {

    List<Sys_permissionTree> selectAll();

    int deleteByPrimaryKey(Integer id);

}
