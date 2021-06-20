package com.ysf.service.role.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ysf.dao.role.Sys_permissionMapper;
import com.ysf.entity.role.Sys_permission;
import com.ysf.service.role.Sys_permissionService;

@Service
public class Sys_permissionServiceImpl implements Sys_permissionService {

    @Resource
    Sys_permissionMapper PerService;


    @Override
    public int deleteByPrimaryKey(Integer permissionId) {
        // TODO Auto-generated method stub
        return PerService.deleteByPrimaryKey(permissionId);
    }

    @Override
    public int insert(Sys_permission record) {
        // TODO Auto-generated method stub
        return PerService.insert(record);
    }

    @Override
    public int insertSelective(Sys_permission record) {
        // TODO Auto-generated method stub
        return PerService.insertSelective(record);
    }

    @Override
    public Sys_permission selectByPrimaryKey(Integer permissionId) {
        // TODO Auto-generated method stub
        return PerService.selectByPrimaryKey(permissionId);
    }

    @Override
    public int updateByPrimaryKeySelective(Sys_permission record) {
        // TODO Auto-generated method stub
        return PerService.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Sys_permission record) {
        // TODO Auto-generated method stub
        return PerService.updateByPrimaryKey(record);
    }

}

