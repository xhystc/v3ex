package com.xhystc.v3ex.test.dao;

import com.xhystc.v3ex.dao.UserDao;
import com.xhystc.v3ex.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext.xml", "classpath:conf/applicationContext-mybatis.xml"})
public class UserDaoTest
{

	@Autowired
	UserDao userDao;

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void test(){
		Map<String,Object> condition = new HashMap<>(3);
		condition.put("username","test");

		List<User> user = userDao.selectUsers(condition);
		user.get(0).setEmail("guodongzhanhun@qq.com");
		System.out.println(user.get(0).getEmail());
		userDao.deleteUserById((long) 23);
		userDao.updateUser(user.get(0));

	}



}











