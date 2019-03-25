package com.cmcc.common.utils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.cmcc.system.dao.SysUserDao;
import com.cmcc.system.entity.SysUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

	private static ApplicationContext applicationContext;
	
	@Autowired
    private SysUserDao sysUserDao;
	
    @Value("${jwt.token.header}")
    public String TOKEN_HEADER;
    @Value("${jwt.token.prefix}")
    public String TOKEN_PREFIX;
    @Value("${jwt.secret}")
    public String SECRET;
    @Value("${jwt.iss}")
    public String ISS;
    // 过期时间是3600秒，既是1个小时
    @Value("${jwt.expiration}")
    public String EXPIRATION;

    
    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            if (JwtTokenUtil.applicationContext == null) {
            	JwtTokenUtil.applicationContext = applicationContext;
            }
    }

    // 创建token
    public String createToken(String username) {
    	long time = Long.parseLong(EXPIRATION);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + time * 1000))
                .compact();
    }

    // 从token中获取用户名
    public String getUserAccount(String token){
        return getTokenBody(token).getSubject();
    }

    public User getUser(String token){
    	String userAccount = getTokenBody(token).getSubject();
    	SysUser user = sysUserDao.selectByAccount(userAccount);
    	//手动注入user，让业务使用
    	ConfigurableApplicationContext context = (ConfigurableApplicationContext)applicationContext; 
    	DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)context.getBeanFactory(); 
    	BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(SysUser.class); 
    	beanDefinitionBuilder.addPropertyValue("userId", user.getUserId());
        beanDefinitionBuilder.addPropertyValue("userAccount", user.getUserAccount());
        beanDefinitionBuilder.addPropertyValue("userName", user.getUserName());
        beanDefinitionBuilder.addPropertyValue("userTel", user.getUserTel());
        beanDefinitionBuilder.addPropertyValue("orgId", user.getOrgId());
        beanDefinitionBuilder.addPropertyValue("lesseeId", user.getLesseeId());
    	beanFactory.registerBeanDefinition("baseUser", beanDefinitionBuilder.getBeanDefinition()); 
    	  
        if (user != null) {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            return new User(user.getUserAccount(), user.getUserPass(), grantedAuthorities);
        }
        return null;
    }
    
    public SysUser getSysUser(String token){
    	String userAccount = getTokenBody(token).getSubject();
    	SysUser user = sysUserDao.selectByAccount(userAccount);
        if (user != null) {
            return user;
        }
        return null;
    }
    
    public Boolean validateToken(String token, User user) {
        final String username = getUserAccount(token);
        return (username.equals(user.getUsername())&& !isExpiration(token));
    }
    
    // 是否已过期
    public boolean isExpiration(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private Claims getTokenBody(String token) throws ExpiredJwtException{
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
