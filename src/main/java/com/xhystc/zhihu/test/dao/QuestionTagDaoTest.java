package com.xhystc.zhihu.test.dao;

import com.xhystc.zhihu.dao.QuestionTagDao;
import com.xhystc.zhihu.dao.TagDao;
import com.xhystc.zhihu.model.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext.xml", "classpath:conf/applicationContext-mybatis.xml"})
public class QuestionTagDaoTest
{
	@Autowired
	QuestionTagDao questionTagDao;
	@Autowired
	TagDao tagDao;

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void getQuestionTags()
	{
		List<Tag> tags = questionTagDao.getQuestionTags(38L);
		for(Tag t : tags){
			System.out.println(t.getName());
		}
		System.out.println(tagDao.selectTagsByName("程").get(0).getName());
	}

	@Test
	public void insertQuestionTags()
	{

		questionTagDao.insertQuestionTag(38L,4L);
		questionTagDao.insertQuestionTag(38L,3L);
		questionTagDao.insertQuestionTag(38L,5L);
	}
}