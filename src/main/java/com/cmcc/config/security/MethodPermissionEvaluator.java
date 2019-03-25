package com.cmcc.config.security;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * ClassName: MethodPermissionEvaluator 
 * @Description: TODO 基于URL拦截权限验证
 * @author zengzhibin
 * @date 2019年3月12日
 */
@Configuration
public class MethodPermissionEvaluator implements PermissionEvaluator {

	private Logger log =  LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		boolean accessable = false;
		String ps = permission.toString();
		for(GrantedAuthority authority : authentication.getAuthorities()){
			if(ps.equalsIgnoreCase(authority.getAuthority())){
				accessable = true;
				break;
			}
		}
		return accessable;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		// TODO Auto-generated method stub
		return false;
	}


}
