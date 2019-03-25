package com.cmcc.system.serivce.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.common.utils.IdGenerateUtil;
import com.cmcc.common.utils.SpringUtil;
import com.cmcc.system.dao.SysUserDao;
import com.cmcc.system.entity.SysMenu;
import com.cmcc.system.entity.SysUser;
import com.cmcc.system.serivce.SysUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.util.Date;
import java.util.List;

/**
 * 
 * ClassName: SysUserServiceImpl 
 * @Description 用户业务实现类
 * @author zengzhibin
 * @date 2019年3月22日
 */
@Service
public class SysUserServiceImpl implements SysUserService{
	
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	//初始化密码值 
	@Value("${system.password}")
    public String PASSWORD;

	@Transactional
	@Override
	public Integer register(SysUser user) {
        String userId = IdGenerateUtil.uuid3();
        user.setUserId(userId);
        String password = bCryptPasswordEncoder.encode(PASSWORD);
        user.setUserPass(password);
        user.setCreateTime(new Date());
        user.setDelFlag("0");
        user.setUserPinyin("");
		return sysUserDao.insertSelective(user);
		//附属表开发中。。。
	}
	
	@Transactional
	@Override
	public Integer add(SysUser user) {
		SysUser baseUser = (SysUser)SpringUtil.getBean("baseUser");
		user.setLesseeId(baseUser.getLesseeId());
		user.setUserBookId(baseUser.getUserBookId());
		user.setCreateUser(baseUser.getUserId());
        String userId = IdGenerateUtil.uuid3();
        user.setUserId(userId);
        String password = bCryptPasswordEncoder.encode(PASSWORD);
        user.setUserPass(password);
        
        user.setCreateTime(new Date());
        user.setDelFlag("0");
        user.setUserPinyin("");
		return sysUserDao.insertSelective(user);
		//附属表开发中。。。
	}

	@Override
	public Integer update(SysUser user) {
		return sysUserDao.insertSelective(user);
		//附属表开发中。。。
	}


	@Override
	public Integer delete(String userId) {
		SysUser user = new SysUser();
		user.setUserId(userId);
		user.setDelFlag("1");
		return sysUserDao.updateByPrimaryKeySelective(user);
	}


	@Override
	public SysUser getOne(String userId) {
		return sysUserDao.selectByPrimaryKey(userId);
		//附属表开发中。。。
	}


	@Override
	public Page<SysMenu> getPage(Integer pageNum, Integer pageSize, String orderBy, SysUser sysUser) {
		//获取token中的用户数据
		SysUser user = (SysUser) SpringUtil.getBean("baseUser");
		sysUser.setLesseeId(user.getLesseeId());
		sysUser.setDelFlag("0");
		PageHelper.startPage(pageNum,pageSize,orderBy);
		return sysUserDao.selectPage(sysUser);
		//附属表开发中。。。
	}


	@Override
	public List<SysUser> getUsers(SysUser sysUser) {
		//获取token中的用户数据
		SysUser user = (SysUser) SpringUtil.getBean("baseUser");
		sysUser.setLesseeId(user.getLesseeId());
		sysUser.setDelFlag("0");
		return sysUserDao.selectAll(sysUser);
		//附属表开发中。。。
	}

	@Override
	public Integer resetPass(String userId) {
		SysUser user = new SysUser();
		user.setUserId(userId);
		String password = bCryptPasswordEncoder.encode(PASSWORD);
        user.setUserPass(password);
		return sysUserDao.updateByPrimaryKeySelective(user);
	}
	
	@Transactional
	@Override
	public Integer userDimission(String[] userIds) {
		for (String id : userIds) {
			SysUser user = new SysUser();
			user.setUserId(id);
			user.setStatus("1");
			sysUserDao.updateByPrimaryKeySelective(user);
		}
		return 1;
	}

}
