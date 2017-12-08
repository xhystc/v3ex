package com.xhystc.zhihu.test.dao;

import com.xhystc.zhihu.dao.QuestionDao;
import com.xhystc.zhihu.dao.UserDao;
import com.xhystc.zhihu.model.Question;
import com.xhystc.zhihu.model.vo.query.QuestionQueryCondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-mybatis.xml"})
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
		Question question = questionDao.getQuestionById((long) 1);
		System.out.println(question.getTitle()+":"+question.getUser().getEmail());
	}

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void insertQuestion() throws Exception
	{
		Question question =new Question();
		question.setContent("作者：徐子陵\n" +
				"链接：https://www.zhihu.com/question/27063463/answer/265785676\n" +
				"来源：知乎\n" +
				"著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。\n" +
				"\n" +
				"大学同学，同宿舍，高白胖，口才好！不挑食，味口非常好！学校附近的兰州拉面一次可以吃两大碗，汤都不带剩的。有一次吃完回到宿舍，又扫光了老五带回的几乎没有油水的蛋炒饼！每次宿舍出去聚餐，都被服务员嗤笑：下一盘菜上来之前，上一盘菜必须已扫光!到最后，他还要把盘底的汤水蘸干净！猪也没吃得这么干净！常喊着要减肥，也经常行动。每次都是只喝水及饮料，最长一次节食10几天，但开禁之后各种爆吃，大学四年没见他有瘦下来过！穿着随意！衣服常这一个孔，那一个洞的。最牛逼的是，大学报到时发了两套床单及被套，大一过后，反过来再用到大二结束，另外一套大三大四用，就这样大学四年没有洗过一次！喜欢躺在床上看小说，除了上厕所，可48小时不用下床。就在这种艰苦的环境下，人还是白胖白胖的，没有任何皮肤的问题，皮抗太高！大三那年，宿舍集体到大连旅游，去之前和我们说，会有人接待。到那边时，住*行招待所，全程有人开车陪我们吃喝玩。走之前行长请我们吃饭，每人送了一堆的干货海鲜！而后才知这小子母上是*行高层，家族大部分或经商或央企高层。毕业四年时，碰了次面，在北京已有5套房。还是那么能吃,就是更白胖了！转眼已毕业多年，6人散落各地！宿舍熄灯后，一人泡面，五个人抢吃的场景仿佛就在昨天！祝各兄弟一切安好！想念你们！");
		question.setTitle("你认识的有钱人是怎样生活的？");
		question.setUser(userDao.getUserById((long) 4));
		question.setCreateDate(new Date());
		questionDao.insertQuestion(question);
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
		question.setContent("我有个室友，上厕所的时候喜欢玩手机。有次大号的时候正在玩王者，当时选的中单诸葛亮各种秀，就在拿三杀的时候一个激动，不小心把手机掉到便池里面了，手机大，卡在洞口处。蹲在那纠结了一分钟捡还是不捡。刚好屏幕朝上，看见对面强行把自己家队友一波团灭。突然咬着牙下定决心，把手机捡起来继续嗨。队友骂他为什么挂机，他说手机刚刚掉到便池了，现在我捡起来继续玩了。突然发现开的是全部，对话被对面看到了，然后全场沉默了。。。可惜那把还是输了，但是没人举报我那室友，并且还收到九个赞。。。。。。\n" +
				"\n" +
				"作者：其土人老\n" +
				"链接：https://www.zhihu.com/question/68450231/answer/264965204\n" +
				"来源：知乎\n" +
				"著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。");
		question.setTitle("如何证明「你永远不知道你的王者荣耀队友正在干什么」这句话？");
		question.setUser(userDao.getUserById((long) 4));
		question.setCreateDate(new Date());
		questionDao.updateQuestion(question);
	}
	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void increaseComment() throws Exception{
		Question question = new Question();
		question.setId(6L);
		question.setAgree(3);
		questionDao.increaseAgree(1L,-1);
	}

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void selectQuestion(){
		QuestionQueryCondition condition = new QuestionQueryCondition();
		condition.setRows(3);
		condition.setOffset(0);
		condition.setOrder("asc");
		Set<Long> ruleOut = new HashSet<>();
		ruleOut.add(1L);
		condition.setRuleOut(ruleOut);
		List<Question> questions = questionDao.selectQuestions(condition);


		for(Question question : questions){
			System.out.println(question.getTitle()+":"+question.getUser().getName());
		}
	}

}