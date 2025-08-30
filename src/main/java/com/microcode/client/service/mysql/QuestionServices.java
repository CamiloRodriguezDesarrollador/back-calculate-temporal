package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IActionDao;
import com.microcode.client.dao.mysql.IQuestionDao;
import com.microcode.client.dao.mysql.IQuestionDetailDao;
import com.microcode.client.entity.general.Chat;
import com.microcode.client.entity.general.ContentResponse;
import com.microcode.client.entity.general.Option;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.mysql.PrincipalData;
import com.microcode.client.entity.mysql.Question;
import com.microcode.client.service.oracle.ActionsOracleServices;
import com.microcode.client.service.oracle.OptionsManageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    public void updateQuestionCalification(){
        List<Question> questions = this.getAllQuestions();
        System.out.println(questions);
        OptionsManageService.updateQuestionsCalification(questions);
    }
}
