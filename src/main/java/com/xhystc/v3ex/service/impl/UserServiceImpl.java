package com.xhystc.v3ex.service.impl;

import com.xhystc.v3ex.commons.PasswordHelper;
import com.xhystc.v3ex.dao.UserDao;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.form.UserRegistForm;
import com.xhystc.v3ex.model.vo.json.Problem;
import com.xhystc.v3ex.model.vo.query.UserQueryCondition;
import com.xhystc.v3ex.service.UserService;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Transactional(rollbackFor = RuntimeException.class)
public class UserServiceImpl implements UserService
{

	private UserDao userDao;
	private PasswordHelper helper;

	@Autowired
	public UserServiceImpl(UserDao userDao, PasswordHelper helper)
	{
		this.userDao = userDao;
		this.helper = helper;
	}

	@Override
	public User getUserByName(String username)
	{
		if(!StringUtils.hasText(username)){
			return null;
		}
		return  userDao.getUserByName(username);
	}

	@Override
	public Set<String> getUserRoles(String username)
	{
		Set<String> set = new HashSet<>();
		set.add("user");
		if("root".equals(username) || "test".equals(username)){
			set.add("root");
		}
		return set;
	}


	@Override
	public Set<Problem> userRegist(UserRegistForm form)
	{
		Set<Problem> ret = new HashSet<>(2);
		List<User> users = userDao.selectUsers(new UserQueryCondition(form.getUsername(),form.getEmail()));
		if(users!=null && users.size()>0){
			User u = users.get(0);
			if(u.getName().equals(form.getUsername())){
				ret.add(new Problem("用户名",form.getUsername(),"用户名已存在"));
			}
			if(u.getEmail().equals(form.getEmail())){
				ret.add(new Problem("邮箱",form.getEmail(),"邮箱已注册"));
			}
		}
		if(ret.size()==0){
			User user = new User();
			user.setEmail(form.getEmail());
			user.setName(form.getUsername());
			user.setIconUrl("static/icon/default"+(Math.abs(UUID.randomUUID().toString().hashCode()%6)+1)+".png");
			user.setRegistDate(new Date());
			user.setPassword(form.getPassword());
			helper.generatePassword(user);

			try
			{
				int i = userDao.insertUser(user);
				if(i<=0) {
					ret.add(new Problem("错误","未知错误","请与管理员联系"));
					return ret;
				}
			}catch (Exception sqle){
				ret.add(new Problem("错误","未知错误","请与管理员联系"));
				return ret;
			}
		}

		return ret;
	}

	@Override
	public boolean usernameExist(String username)
	{
		if( userDao.selectUsers(new UserQueryCondition(username,null))==null){
			return false;
		}
		return true;
	}

	@Override
	public boolean emailExist(String email)
	{
		if(userDao.selectUsers(new UserQueryCondition(null,email))==null){
			return false;
		}
		return true;
	}
}









