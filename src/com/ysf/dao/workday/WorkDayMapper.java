package com.ysf.dao.workday;

import java.util.List;

import com.ysf.entity.workday.WorkDay;

public interface WorkDayMapper {
    int deleteByPrimaryKey(String workId);

    int insert(WorkDay record);

    int insertSelective(WorkDay record);

    WorkDay selectByPrimaryKey(String workId);

    int updateByPrimaryKeySelective(WorkDay record);

    int updateByPrimaryKey(WorkDay record);

    List<WorkDay> selectAll();
}
