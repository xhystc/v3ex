package com.xhystc.v3ex.shiro;

import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.service.UserService;


import org.apache.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.HashSet;
import java.util.Set;


public class GeneralRealm extends AuthorizingRealm
{
	@Autowired
	private UserService userService;

	private static Logger logger = Logger.getLogger(GeneralRealm.class);

	public GeneralRealm(CacheManager cacheManager, CredentialsMatcher matcher)
	{
		super(cacheManager, matcher);
	}
	public  GeneralRealm(){
		super();
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)
	{
		String username = (String) principalCollection.getPrimaryPrincipal();
		SimpleAuthorizationInfo ret = new SimpleAuthorizationInfo();
		Set<String> roles = new HashSet<>(userService.getUserRoles(username));

		ret.addRoles(roles);
		return ret;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException
	{
		User user = userService.getUserByName((String) authenticationToken.getPrincipal());
		if (user == null)
		{
			logger.debug("user not found");
			throw new UnknownAccountException();
		}
		if(user.getIsLocked()){
			logger.debug("user locked");
			throw new LockedAccountException();
		}

		SimpleAuthenticationInfo ret = new SimpleAuthenticationInfo(user.getName(),user.getPassword(), ByteSource.Util.bytes(user.getCredentialsSalt()),getName());
		return ret;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}
}





