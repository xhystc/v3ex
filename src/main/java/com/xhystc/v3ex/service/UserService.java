package com.xhystc.v3ex.service;

import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.form.UserRegistForm;
import com.xhystc.v3ex.model.vo.Problem;

import java.util.List;
import java.util.Set;

public interface UserService
{
	User getUserByName(String username);
	User getUserByEmail(String email);
	User getUserById(Long id);
	List<User> getUserByUsersnamePrefix(String prefix);
	Set<String> getUserRoles(String username);
	Set<Problem> userRegist(UserRegistForm form);
}


