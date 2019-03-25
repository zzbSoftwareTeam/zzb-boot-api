package com.cmcc.config.security.filter;

import com.cmcc.common.bean.Result;
import com.cmcc.common.bean.ResultCode;
import com.cmcc.common.utils.JwtTokenUtil;
import com.cmcc.common.utils.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * ClassName: JWTAuthenticationFilter 
 * @Description: TODO 用户认证过滤器
 * @author zengzhibin
 * @date 2019年3月12日
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private Logger log =  LoggerFactory.getLogger(this.getClass());
	
    private AuthenticationManager authenticationManager;
    

    /**
     * 自定义鉴权方法与参数名
     */
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/auth/login");
        super.setUsernameParameter("userAccount");
        super.setPasswordParameter("userPass");
    }

    /**
     * 自定义鉴权方法
     * 参数传递
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
    	if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}
		String userAccount = request.getParameter("userAccount");
		String userPass = request.getParameter("userPass");
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userAccount, userPass, new ArrayList<>())
        );
    }

    /**
     * 验证成功，就生成token并返回
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
    	
    	JwtTokenUtil jwtTokenUtil = SpringUtil.getBean(JwtTokenUtil.class);
    	
		User user = (User) authResult.getPrincipal();
        System.out.println("user:" + user.toString());
        //user.getAuthorities();
        String token = jwtTokenUtil.createToken(user.getUsername());
        //返回自定义JSON
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        // 按照jwt的规定，最后请求的时候应该是 `Bearer token`
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", jwtTokenUtil.TOKEN_PREFIX+" "+ token);
        Result res = Result.success(map);
        response.getWriter().write(new ObjectMapper().writeValueAsString(res));
    }

    /**
     * 401认证失败
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    	//返回自定义JSON
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Result res = Result.failure(ResultCode.USER_LOGIN_ERROR);
        response.getWriter().write(new ObjectMapper().writeValueAsString(res));
    }
}
