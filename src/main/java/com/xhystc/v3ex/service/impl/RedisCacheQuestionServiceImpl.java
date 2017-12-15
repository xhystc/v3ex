package com.xhystc.v3ex.service.impl;

import com.xhystc.v3ex.commons.RedisUtils;
import com.xhystc.v3ex.dao.QuestionDao;
import com.xhystc.v3ex.dao.QuestionTagDao;
import com.xhystc.v3ex.dao.TagDao;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.Tag;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.form.QuestionForm;
import com.xhystc.v3ex.model.vo.query.QuestionQueryCondition;
import com.xhystc.v3ex.service.QuestionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.*;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Transactional(rollbackFor = RuntimeException.class)
@Component("redisCacheQuestionServiceImpl")
public class RedisCacheQuestionServiceImpl implements QuestionService,InitializingBean,Runnable
{

	static final private Logger logger = Logger.getLogger(RedisCacheQuestionServiceImpl.class);
	static final private String ACTIVE_PREFIX = "question-active";
	static final private String TAG_TOTAL_PREFIX = "question-total";
	static final private String MODIFY_PREFIX = "question-modify";
	static final private int FETCH_SIZE = 1000;

	private long timeout = 60*1000*3;

	private JedisPool jedisPool;
	private QuestionDao questionDao;
	private QuestionTagDao questionTagDao;
	private TagDao tagDao;


	@Autowired
	public RedisCacheQuestionServiceImpl(JedisPool jedisPool, QuestionDao questionDao,QuestionTagDao questionTagDao,TagDao tagDao)
	{
		this.jedisPool = jedisPool;
		this.questionDao = questionDao;
		this.jedisPool = jedisPool;
		this.questionTagDao = questionTagDao;
		this.tagDao = tagDao;
	}


	@Override
	public Question getQuestion(Long id)
	{
		Question question = questionDao.getQuestionById(id);
		try(Jedis redis = jedisPool.getResource())
		{
			double active = redis.zscore(activeKey(),question.getId().toString());
			Date activeTime = new Date();
			activeTime.setTime((long) active);
			question.setActiveTime(activeTime);
		}
		return question;
	}

	@Override
	public List<Question> getQuestions(Long tagId, int page, int pageSize)
	{
		Set<Tuple> questionTuples;
		try(Jedis redis = jedisPool.getResource())
		{
			int offset = (page-1)*pageSize;
			questionTuples = redis.zrevrangeWithScores(activeKey(tagId),offset,offset+pageSize-1);
		}
		if(questionTuples.size()==0)
		{
			return Collections.emptyList();
		}
		Map<Long,Integer> indexMap = new HashMap<>(questionTuples.size());
		Map<Long,Long> activeMap = new HashMap<>(questionTuples.size());
		Set<Long> ids = new HashSet<>(questionTuples.size());

		int i=0;
		for(Tuple tuple : questionTuples){
			Long questionId = Long.parseLong(tuple.getElement());
			indexMap.put(questionId,i++);
			activeMap.put(questionId, (long) tuple.getScore());
			ids.add(questionId);
		}

		QuestionQueryCondition condition = new QuestionQueryCondition();
		condition.setInclude(ids);
		condition.setNeedOrder(false);
		condition.setNeedLimit(false);
		List<Question> questions = questionDao.selectQuestions(condition);
		Question[] questionArray = new Question[questions.size()];

		for(Question question : questions){
			Date activeTime = new Date();
			activeTime.setTime(activeMap.get(question.getId()));
			question.setActiveTime(activeTime);
			questionArray[indexMap.get(question.getId())] = question;
		}

		return Arrays.asList(questionArray);
	}

	@Override
	public Long publishQuetion(User user, QuestionForm form)
	{
		Question question = new Question();
		question.setActiveTime(new Date());
		question.setContent(form.getContent());
		question.setCreateDate(question.getActiveTime());
		question.setTitle(form.getTitle());
		question.setUser(user);

		questionDao.insertQuestion(question);

		for(Long tag : form.getTags()){
			questionTagDao.insertQuestionTag(question.getId(),tag);
		}
		tagDao.increaseCount(Arrays.asList(form.getTags()));
		incraseRedisTotal(form.getTags());

		upQuestion(question.getId());
		return question.getId();
	}

	@Override
	public void upQuestion(Long questionId)
	{
		try(Jedis redis = jedisPool.getResource())
		{
			List<Long> tagIds =  questionTagDao.getQuestionTagIds(questionId);

			Pipeline pipeline = redis.pipelined();
			for(Long tagId : tagIds){
				pipeline.zadd(activeKey(tagId),System.currentTimeMillis(),questionId.toString());
			}
			pipeline.zadd(activeKey(),System.currentTimeMillis(),questionId.toString());
			pipeline.sadd(modifyKey(),questionId.toString());
			pipeline.sync();
		}
	}

	@Override
	public int total(Long tagId)
	{
		try(Jedis redis = jedisPool.getResource())
		{
			String s = redis.get(totalKey(tagId));
			return Integer.parseInt(s);
		}
	}
	public void storeQuestions(){
		logger.info("redis cached question service start");
		Long start = System.currentTimeMillis();

		List<Tag> tags = tagDao.selectAll();

		try(Jedis redis = jedisPool.getResource())
		{
			for(Tag tag :tags){
				storeTotal(redis,tag);
				for(int i=0;;i++){
					List<Question> questions = getDBQuestions(i*FETCH_SIZE,FETCH_SIZE,tag.getId());
					if (questions.size()==0)
					{
						break;
					}
					storeQuestions(redis,questions,tag.getId());
				}
			}
			storeTotal(redis);
			for(int i=0;;i++){
				List<Question> questions = getDBQuestions(i*FETCH_SIZE,FETCH_SIZE,null);
				storeQuestions(redis,questions,null);
				if (questions.size()<FETCH_SIZE)
				{
					break;
				}
			}
		}
		System.out.println("store over:"+((System.currentTimeMillis()-start)/1000)+"s");
	}

	@Override
	public void run()
	{
		logger.debug("question time out");
		try(Jedis redis = jedisPool.getResource())
		{
			Set<String> modifies = redis.smembers(modifyKey());
			Pipeline pipeline = redis.pipelined();
			Map<String,Response<Double>> activeTimeMap = new HashMap<>(modifies.size());
			for(String s : modifies){
				activeTimeMap.put(s,pipeline.zscore(activeKey(),s));
			}
			pipeline.sync();
			for(Map.Entry<String,Response<Double>> en : activeTimeMap.entrySet()){
				Question question = new Question();
				question.setId(Long.parseLong(en.getKey()));
				Date date = new Date();
				date.setTime(en.getValue().get().longValue());
				question.setActiveTime(date);
				questionDao.updateQuestion(question);
			}
			redis.del(modifyKey());
		}

	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		logger.debug("question service do init");
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1, r -> {
			Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setName("question-service-flush");
			t.setDaemon(true);
			return t;
		});
		service.scheduleWithFixedDelay(this,timeout,timeout, TimeUnit.MILLISECONDS);
	}


	private void incraseRedisTotal(Long[] ids){
		try(Jedis redis = jedisPool.getResource())
		{
			Pipeline pipeline = redis.pipelined();
			for(Long id : ids){
				pipeline.incr(totalKey(id));
			}
			pipeline.incr(totalKey());
			pipeline.sync();
		}
	}
	private List<Question> getDBQuestions(int offset,int rows,Long tagId){
		QuestionQueryCondition condition = new QuestionQueryCondition();
		condition.setOffset(offset);
		condition.setRows(rows);
		condition.setTagId(tagId);
		condition.setNeedOrder(false);
		return questionDao.selectQuestions(condition);
	}
	private void storeQuestions(Jedis redis,List<Question> questions,Long tagId){
		Pipeline pipeline = redis.pipelined();
		for (Question question : questions){
			pipeline.zadd(activeKey(tagId),question.getActiveTime().getTime(),question.getId().toString());
		}
		pipeline.sync();
	}
	private void storeTotal(Jedis redis,Tag tag){
		redis.set(totalKey(tag.getId()),tag.getQuestionCount()+"");
	}

	private void storeTotal(Jedis redis){
		int total = questionDao.total(null);
		redis.set(totalKey(),total+"");
	}

	private static String modifyKey(){
		return MODIFY_PREFIX;
	}
	private static String activeKey(){
		return ACTIVE_PREFIX;
	}
	private static String activeKey(Long tagId){
		return tagId==null?ACTIVE_PREFIX:ACTIVE_PREFIX+"-"+tagId;
	}

	private static String totalKey(){
		return TAG_TOTAL_PREFIX;
	}
	private static String totalKey(Long tagId){
		return tagId==null?TAG_TOTAL_PREFIX:TAG_TOTAL_PREFIX+"-"+tagId;
	}

	public long getTimeout()
	{
		return timeout;
	}

	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}
}






