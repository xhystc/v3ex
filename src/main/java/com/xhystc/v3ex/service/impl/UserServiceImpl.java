package com.xhystc.v3ex.service.impl;

import com.xhystc.v3ex.commons.PasswordHelper;
import com.xhystc.v3ex.dao.UserDao;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.form.UserRegistForm;
import com.xhystc.v3ex.model.vo.Problem;
import com.xhystc.v3ex.service.UserService;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
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
	public User getUserByEmail(String email)
	{
		return userDao.getUserByEmail(email);
	}

	@Override
	public User getUserById(Long id)
	{
		return userDao.getUserById(id);
	}

	@Override
	public List<User> getUserByUsersnamePrefix(String prefix)
	{
		return null;
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


	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public Set<Problem> userRegist(UserRegistForm form)
	{
		Set<Problem> ret = new HashSet<>(2);
		Map<String,Object> codition = new HashMap<>(2);
		codition.put("username",form.getUsername());
		codition.put("email",form.getEmail());
		List<User> users = userDao.selectUsers(codition);
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

}









