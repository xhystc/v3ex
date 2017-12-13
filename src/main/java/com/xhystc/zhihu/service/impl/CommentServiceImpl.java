package com.xhystc.zhihu.service.impl;

import com.xhystc.zhihu.commons.FormUtils;
import com.xhystc.zhihu.dao.CommentDao;
import com.xhystc.zhihu.dao.CommentInformDao;
import com.xhystc.zhihu.dao.QuestionTagDao;
import com.xhystc.zhihu.model.Comment;
import com.xhystc.zhihu.model.CommentInform;
import com.xhystc.zhihu.model.User;
import com.xhystc.zhihu.model.vo.page.CommentPage;
import com.xhystc.zhihu.model.vo.query.CommentQueryCondition;
import com.xhystc.zhihu.service.CommentService;
import com.xhystc.zhihu.service.QuestionService;
import com.xhystc.zhihu.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(rollbackFor = RuntimeException.class)
public class CommentServiceImpl implements CommentService
{
	private CommentDao commentDao;
	private CommentInformDao commentInformDao;
	private QuestionTagDao questionTagDao;
	private QuestionService questionService;


	@Autowired
	public CommentServiceImpl(CommentDao commentDao, CommentInformDao commentInformDao, QuestionTagDao questionTagDao,
	                         QuestionService questionService)
	{
		this.commentDao = commentDao;
		this.commentInformDao = commentInformDao;
		this.questionTagDao = questionTagDao;
		this.questionService = questionService;
	}

	@Override
	public void initCommentInform(String type, Long id)
	{
		CommentInform commentInform = new CommentInform();
		commentInform.setId(type+"_"+id.toString());
		commentInformDao.insertCommentInform(commentInform);
	}

	@Override
	public CommentInform doComment(Comment comment)
	{
		String commentInformId = comment.getParentType()+"_"+comment.getParentId().toString();
		commentInformDao.increaseCommentInform(commentInformId,
				comment.getUser().getId(),comment.getSendDate(),1);
		commentDao.insertComment(comment);
		return commentInformDao.selectCommentInform(commentInformId);
	}

	@Override
	public CommentPage getQuestionCommentPage(String type, Long id, int page, int pageSize)
	{
		CommentQueryCondition commentQueryCondition = new CommentQueryCondition();
		commentQueryCondition.setOffset((page-1)*pageSize);
		commentQueryCondition.setRows(pageSize);
		commentQueryCondition.setParentType(type);
		commentQueryCondition.setParentId(id);

		List<Comment> comments = commentDao.selectComments(commentQueryCondition);

		CommentPage commentPage = new CommentPage();
		commentPage.setComments(comments);
		commentPage.setTags(questionTagDao.getQuestionTags(id));
		commentPage.setCurrentPage(page);
		commentPage.setQuestion(questionService.getQuestion(id));

		CommentInform commentInform = getCommentInform("question",id);
		commentPage.setLastPage((commentInform.getCommentCount()/pageSize)+(commentInform.getCommentCount()%pageSize>0?1:0));
		commentPage.setPageButtons(FormUtils.pageButtons(commentPage.getCurrentPage(),commentPage.getLastPage()));

		User user = FormUtils.getCurrentUser();
		Long userId = user==null?-1:user.getId();

		return commentPage;
	}

	@Override
	public CommentInform getCommentInform(String type, Long id)
	{
		return commentInformDao.selectCommentInform(type+"_"+id.toString());
	}

	public QuestionService getQuestionService()
	{
		return questionService;
	}

	public void setQuestionService(QuestionService questionService)
	{
		this.questionService = questionService;
	}
}







