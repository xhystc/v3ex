package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface UserDao
{
	User getUserById(Long id);
	List<User> selectUsers(Map<String,Object> condition);
	User getUserByName(String name);
	List<User> getUsersByUsernamePrefix(String prefix);
	User getUserByEmail(String email);
	int insertUser(User user);
	int deleteUserById(Long id);
	int updateUser(User user);
}
