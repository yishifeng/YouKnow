package com.ysf.dao.questionnaire;

import java.util.Map;

import com.ysf.entity.questionnaire.QuestionnaireMainUser;

public interface QuestionnaireMainUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(QuestionnaireMainUser record);

    int insertSelective(QuestionnaireMainUser record);

    QuestionnaireMainUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(QuestionnaireMainUser record);

    int updateByPrimaryKey(QuestionnaireMainUser record);

    QuestionnaireMainUser selectByMainIDUserId(Map<String,String> parm);
}
