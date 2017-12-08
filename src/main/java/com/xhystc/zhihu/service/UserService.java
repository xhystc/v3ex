package com.xhystc.zhihu.service;

import com.xhystc.zhihu.model.User;
import com.xhystc.zhihu.model.vo.form.UserRegistForm;
import com.xhystc.zhihu.model.vo.json.Problem;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService
{
	User getUserByName(String username);
	Set<String> getUserRoles(String username);
	Set<Problem> userRegist(UserRegistForm form);
	boolean usernameExist(String username);
	boolean emailExist(String email);
}
