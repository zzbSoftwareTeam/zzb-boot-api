package com.cmcc.config.security;

import com.cmcc.common.bean.Result;
import com.cmcc.common.bean.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * ClassName: JWTAuthenticationEntryPoint 
 * @Description: TODO 自定义认证失败异常
 * @author zengzhibin
 * @date 2019年3月12日
 */
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private Logger log =  LoggerFactory.getLogger(this.getClass());
	
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
    	//返回自定义401
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Result res = Result.failure(ResultCode.USER_AUTH_ERROR);
        response.getWriter().write(new ObjectMapper().writeValueAsString(res));
    }
}