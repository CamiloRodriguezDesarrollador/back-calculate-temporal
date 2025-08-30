package com.microcode.client.dao.mysql;

import com.microcode.client.entity.mysql.QuestionDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IQuestionDetailDao extends CrudRepository<QuestionDetail, Long> {

    @Query(""" 
            SELECT p
            FROM QuestionDetail p
            WHERE p.questionId = :questionId
            AND p.questionDetailStatus = 'A'
            ORDER BY p.questionDetailOrder ASC
            """)
    List<QuestionDetail> findQuestionById(Integer questionId);
}
