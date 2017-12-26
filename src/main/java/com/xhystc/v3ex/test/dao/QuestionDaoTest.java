package com.xhystc.v3ex.test.dao;

import com.xhystc.v3ex.dao.QuestionDao;
import com.xhystc.v3ex.dao.UserDao;
import com.xhystc.v3ex.model.EntityType;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.query.QuestionQueryCondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext.xml", "classpath:conf/applicationContext-mybatis.xml"})
public class QuestionDaoTest
{
	@Autowired
	QuestionDao questionDao;
	@Autowired
	UserDao userDao;


	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void getQuestionById() throws Exception
	{
		System.out.println(EntityType.valueOf("question"));
		Question question = questionDao.getQuestionById(47L);
		System.out.println(question.getCommentCount());
	}

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void insertQuestion() throws Exception
	{
		Question question =new Question();
		User user = userDao.getUserById((long) 4);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse("2017-10-30 16:31:51");

		for(int i = 0;i<200000;i++){
			question.setContent("作者：徐子陵\n" +
					"链接：https://www.v3ex.com/question/27063463/answer/265785676\n" +
					"来源：知乎\n" +
					"著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。\n" +
					"\n" +
					"大学同学，同宿舍，高白胖，口才好！不挑食，味口非常好！学校附近的兰州拉面一次可以吃两大碗，汤都不带剩的。有一次吃完回到宿舍，又扫光了老五带回的几乎没有油水的蛋炒饼！每次宿舍出去聚餐，都被服务员嗤笑：下一盘菜上来之前，上一盘菜必须已扫光!到最后，他还要把盘底的汤水蘸干净！猪也没吃得这么干净！常喊着要减肥，也经常行动。每次都是只喝水及饮料，最长一次节食10几天，但开禁之后各种爆吃，大学四年没见他有瘦下来过！穿着随意！衣服常这一个孔，那一个洞的。最牛逼的是，大学报到时发了两套床单及被套，大一过后，反过来再用到大二结束，另外一套大三大四用，就这样大学四年没有洗过一次！喜欢躺在床上看小说，除了上厕所，可48小时不用下床。就在这种艰苦的环境下，人还是白胖白胖的，没有任何皮肤的问题，皮抗太高！大三那年，宿舍集体到大连旅游，去之前和我们说，会有人接待。到那边时，住*行招待所，全程有人开车陪我们吃喝玩。走之前行长请我们吃饭，每人送了一堆的干货海鲜！而后才知这小子母上是*行高层，家族大部分或经商或央企高层。毕业四年时，碰了次面，在北京已有5套房。还是那么能吃,就是更白胖了！转眼已毕业多年，6人散落各地！宿舍熄灯后，一人泡面，五个人抢吃的场景仿佛就在昨天！祝各兄弟一切安好！想念你们！");
			question.setTitle("你认识的有钱人是怎样生活的？");
			question.setUser(user);
			question.setCreateDate(new Date());
			questionDao.insertQuestion(question);
		}
	}

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void deleteQuestionById() throws Exception
	{
		questionDao.deleteQuestionById((long) 5);
	}

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void updateQuestion() throws Exception
	{
		Question question = questionDao.getQuestionById((long) 1);

		questionDao.updateQuestion(question);
	}


	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void selectQuestion(){
			QuestionQueryCondition condition = new QuestionQueryCondition();
			condition.setRows(20);
			condition.setOffset(0);
			condition.setTagId(1L);
			List<Question> questions = questionDao.selectQuestions(condition);

			for(Question question : questions){
				System.out.println(question.getTitle()+":"+question.getCommentCount());
			}
			System.out.println(questionDao.total(1L));
	}


	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void total(){
		System.out.println(questionDao.total(null));
	}
}