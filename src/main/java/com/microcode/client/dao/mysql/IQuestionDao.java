package com.microcode.client.dao.mysql;

import com.microcode.client.entity.mysql.Question;
import com.microcode.client.entity.mysql.QuestionDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IQuestionDao extends CrudRepository<Question, Long> {

    @Query(""" 
            SELECT p
            FROM Question p
            WHERE p.questionStatus = 'A'
            ORDER BY p.questionOrder ASC
            """)
    List<Question> findQuestionAll();
}
