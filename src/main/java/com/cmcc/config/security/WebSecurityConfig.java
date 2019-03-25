package com.cmcc.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.cmcc.common.service.SecurityUserService;
import com.cmcc.config.security.filter.JWTAuthenticationFilter;
import com.cmcc.config.security.filter.JWTAuthorizationFilter;

/**
 * 
 * ClassName: WebSecurityConfig 
 * @Description: TODO 基于spring security的权限验证 配置类
 * @author zengzhibin
 * @date 2019年3月12日
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    private SecurityUserService securityUserService;

	/**
	 * 
	 * @Description: TODO 跨域的简单处理
	 * @return CorsConfigurationSource  
	 * @author zengzhibin
	 * @date 2019年3月12日
	 */
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
	
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityUserService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	http.cors().and().csrf().disable()
    		.authorizeRequests()
	        // 对于获取token的rest api要允许匿名访问
	        .antMatchers("/auth/**","/sysUser/register","/file/**").permitAll()
	        // 除上面外的所有请求全部需要鉴权认证
	        .anyRequest().authenticated().and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager()))
            .addFilter(new JWTAuthorizationFilter(authenticationManager()))
            // 不需要session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .exceptionHandling().authenticationEntryPoint(new JWTAuthenticationEntryPoint())
            					.accessDeniedHandler(new JWTAccessDeniedHandler());
    }
    
    /**
     * swagger静态资源配置
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/swagger-resources/**",
                "/webjars/**",
                "/swagger-ui.html"
        );
    }
}
