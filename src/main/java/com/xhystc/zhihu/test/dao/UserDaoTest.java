package com.xhystc.zhihu.test.dao;

import com.xhystc.zhihu.dao.UserDao;
import com.xhystc.zhihu.model.User;
import com.xhystc.zhihu.model.vo.query.UserQueryCondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-mybatis.xml"})
public class UserDaoTest
{

	@Autowired
	UserDao userDao;

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void test(){
		List<User> user = userDao.selectUsers(new UserQueryCondition("test",null));
		user.get(0).setEmail("guodongzhanhun@qq.com");
		System.out.println(user.get(0).getEmail());
		userDao.deleteUserById((long) 23);
		userDao.updateUser(user.get(0));

	}

}











