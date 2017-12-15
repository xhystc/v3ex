package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.query.UserQueryCondition;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserDao
{
	User getUserById(Long id);
	List<User> selectUsers(UserQueryCondition condition);
	User getUserByName(String name);
	int insertUser(User user);
	int deleteUserById(Long id);
	int updateUser(User user);
}
