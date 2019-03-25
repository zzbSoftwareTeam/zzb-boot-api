package com.cmcc.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.cmcc.common.bean.Result;
import com.cmcc.common.bean.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 
 * ClassName: JWTAccessDeniedHandler 
 * @Description: TODO 自定义资源权限403
 * @author zengzhibin
 * @date 2019年3月12日
 */
public class JWTAccessDeniedHandler implements AccessDeniedHandler  {

	private Logger log =  LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		//返回自定义403
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Result res = Result.failure(ResultCode.PERMISSION_NO_ACCESS);
        response.getWriter().write(new ObjectMapper().writeValueAsString(res));
		
	}


    
}
