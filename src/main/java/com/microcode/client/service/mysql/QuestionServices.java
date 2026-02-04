package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IQuestionDao;
import com.microcode.client.dao.mysql.IQuestionDetailDao;
import com.microcode.client.entity.mysql.Question;
import com.microcode.client.service.oracle.OptionsManageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionServices implements QuestionServicesI {

    private final IQuestionDao questionDao;
    private final IQuestionDetailDao detailDao;

    @Override
    public List<Question> getAllQuestions() {
        List<Question> questions = questionDao.findQuestionAll();

        for (Question q : questions) {
            q.setQuestionsDetail(detailDao.findQuestionById(q.getQuestionId()));
        }

        return questions;
    }

    @Override
    public void updateQuestionCalification(){
        List<Question> questions = this.getAllQuestions();
        System.out.println(questions);
        OptionsManageService.updateQuestionsCalification(questions);
    }
}
