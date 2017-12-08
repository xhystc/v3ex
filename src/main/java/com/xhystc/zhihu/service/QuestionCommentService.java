package com.xhystc.zhihu.service;

import com.xhystc.zhihu.model.Comment;
import com.xhystc.zhihu.model.Question;
import com.xhystc.zhihu.model.User;
import com.xhystc.zhihu.model.vo.form.QuestionCommentForm;
import com.xhystc.zhihu.model.vo.form.QuestionForm;
import com.xhystc.zhihu.model.vo.json.Problem;
import com.xhystc.zhihu.model.vo.page.CommentPage;
import com.xhystc.zhihu.model.vo.query.CommentQueryCondition;
import com.xhystc.zhihu.model.vo.query.QuestionQueryCondition;

import java.util.List;
import java.util.Set;

public interface QuestionCommentService
{
	List<Question> getQuestionItems(int page,int pageSize);
	Set<Problem> publishQuestion(User user, QuestionForm form);
	List<Comment> getCommentItems(CommentQueryCondition condition);
	CommentPage commentPage(Long questionId,int page,int pageSize);
	int totalQuestions(QuestionQueryCondition condition);
	Question getQuestionById(Long id);
	Set<Problem> publishQuestionComment(User user, QuestionCommentForm form);
}
