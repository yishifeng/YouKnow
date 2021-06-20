package com.ysf.dao.role;

import java.util.List;
import java.util.Map;

import com.ysf.entity.login.Userlogin;
import com.ysf.entity.login.UserloginKey;

public interface UserloginMapper {
    int deleteByPrimaryKey(UserloginKey key);

    int insert(Userlogin record);

    int insertSelective(Userlogin record);

    Userlogin selectByPrimaryKey(UserloginKey key);

    int updateByPrimaryKeySelective(Userlogin record);

    int updateByPrimaryKey(Userlogin record);

    Userlogin selectByUsername(String username);

    Userlogin selectByPrimaryKeyUsername(UserloginKey key);

    List<Userlogin> selectPage(Map<String,Object> parm);

    Map<String,Long> selectCount(Map<String,Object> parmMap);
}
