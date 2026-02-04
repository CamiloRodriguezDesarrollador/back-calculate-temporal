package com.microcode.client.service.mysql;

import com.microcode.client.entity.mysql.Question;

import java.util.List;

public interface QuestionServicesI {

    List<Question> getAllQuestions();
    void updateQuestionCalification();

}
