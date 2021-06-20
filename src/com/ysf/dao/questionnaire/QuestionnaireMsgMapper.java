package com.ysf.dao.questionnaire;

import java.util.List;

import com.ysf.entity.questionnaire.QuestionnaireMsg;

public interface QuestionnaireMsgMapper {
    int deleteByPrimaryKey(String msgId);

    int insert(QuestionnaireMsg record);

    int insertSelective(QuestionnaireMsg record);

    QuestionnaireMsg selectByPrimaryKey(String msgId);

    int updateByPrimaryKeySelective(QuestionnaireMsg record);

    int updateByPrimaryKey(QuestionnaireMsg record);

    List<QuestionnaireMsg> selectByMainId(String mainId);

}
