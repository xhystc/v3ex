package com.xhystc.v3ex.test.dao;

import com.xhystc.v3ex.commons.QuestionRankUtils;
import com.xhystc.v3ex.dao.QuestionDao;
import com.xhystc.v3ex.dao.solr.SolrDao;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.vo.SolrHighLightInform;
import com.xhystc.v3ex.service.CommentService;
import com.xhystc.v3ex.service.VoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext.xml","classpath:conf/applicationContext-service.xml","classpath:conf/applicationContext-mybatis.xml", "classpath:conf/applicationContext-solr.xml"})
public class SolrDaoTest
{
	@Autowired
	SolrDao solrDao;
	@Autowired
	QuestionDao questionDao;
	@Autowired
	CommentService commentService;
	@Autowired
	VoteService voteService;

	@Test
	public void addQuestion() throws Exception
	{
		Question question = new Question();
		question.setId(9L);
		question.setTitle("奇怪了 vultr 上今天 google ping 不通了 baidu 却可以");
		question.setContent("前段时间想搞下谷歌的 cdn，然后查资料看到了 https://www.v2ex.com/t/272406 这个帖子下 一楼的回复， @sumhat 他的站用的就是 google cloud cdn 速度非常好。这两天我自己尝试配置了一下，总是不能成功将流量引到 https 上。 我的源站在香港，我在谷歌台湾地区买了一台 GCE 做 Nginx 反向代理回我的源站，比如这台 GCE 的 NGINX 的 ip 是 1.1.1.1 Nginx 配置是 123.xyz 和 www.123.xyz 都转跳到 https://www.123.xyz 。直接用域名解析到这台 GCE 的 Nginx 上是没问题的，浏览器输入 123.xyz 或者 www.123xyz.com 都能正确转到 https 上。然后我用谷歌的负载均衡器建立了 cdn 后，只能输入 https://www.123.xyz 才能访问我的网站，输入 123.xyz 或者 www.123xyz.com 都是变成 http://123.xyz 或者 http://www.123.xyz 。都无法转跳到 https 上。 请问是否能有设置过的哥们指点一下。\n" +
				"后端服务\n" +
				"\n" +
				"instance-2\n" +
				"端点协议：HTTPS 已命名的端口： https 超时：30 秒 运行状况检查： https-kk 会话粘性：无 Cloud CDN：已停用\n" +
				"\n" +
				"高级配置 实例组\t地区\t运行状况良好\t自动调节\t平衡模式\t容量 instance-group-1\tasia-east1-b\t1 / 1\t关闭\tCPU 利用率上限：80%\t100% 主机和路径规则\n" +
				"\n" +
				"主机\t路径\t后端 所有不匹配的项（默认）\t所有不匹配的项（默认）\tinstance-2 前端\n" +
				"\n" +
				"协议\tIP:端口\t证书 HTTP\t135.10.167.69:80\t— HTTPS\t135.10.167.69:443\tjs-ca1,");
		solrDao.addQuestion(question,9.32D);
	}


	@Test
	public void updateQuestion(){
		solrDao.updateQuestion(2L,"score","100");
	}

	@Test
	public void deleteQueston(){
		solrDao.deleteQuestion(4L);
	}

	@Test
	public void store(){
		QuestionRankUtils.storeSolrQuestions(questionDao,solrDao,commentService,voteService);
	}
}




