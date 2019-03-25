package com.cmcc.config.security.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.cmcc.common.bean.Result;
import com.cmcc.common.bean.ResultCode;
import com.cmcc.common.utils.JwtTokenUtil;
import com.cmcc.common.utils.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * ClassName: JWTAuthorizationFilter 
 * @Description: TODO token JWT验证的过滤器
 * @author zengzhibin
 * @date 2019年3月12日
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private Logger log =  LoggerFactory.getLogger(this.getClass());
	
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 验证token
     */
    @Override
    protected void doFilterInternal(
    		HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {

    	JwtTokenUtil jwtTokenUtil = SpringUtil.getBean(JwtTokenUtil.class);
    	
        String tokenHeader = request.getHeader(jwtTokenUtil.TOKEN_HEADER);
        // 请求头中没有Authorization
        if (tokenHeader == null || !tokenHeader.startsWith(jwtTokenUtil.TOKEN_PREFIX)) {
        	chain.doFilter(request, response);
            return;
        	//返回自定义JSON
            /*response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            Result res = Result.failure(ResultCode.USER_AUTH_NULL);
            response.getWriter().write(new ObjectMapper().writeValueAsString(res));
            return;*/
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息
        String token = tokenHeader.replace(jwtTokenUtil.TOKEN_PREFIX+" ", "");
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	try {
	        	User user = jwtTokenUtil.getUser(token);
	            if (jwtTokenUtil.validateToken(token, user)) {
	                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authentication);
	                super.doFilterInternal(request, response, chain);
	            }
        	} catch (ExpiredJwtException e) {
        		//返回自定义JSON
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                Result res = Result.failure(ResultCode.USER_AUTH_OUTTIME);
                response.getWriter().write(new ObjectMapper().writeValueAsString(res));
        	} catch (SignatureException e) {
        		//返回自定义JSON
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                Result res = Result.failure(ResultCode.USER_AUTH_TOKEN_ERROR);
                response.getWriter().write(new ObjectMapper().writeValueAsString(res));
        	}
        }else{
        	//返回自定义JSON
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            Result res = Result.failure(ResultCode.USER_AUTH_TOKEN_ERROR);
            response.getWriter().write(new ObjectMapper().writeValueAsString(res));
        }
        
        
    }

}