package com.cmcc.system.serivce.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.common.utils.IdGenerateUtil;
import com.cmcc.common.utils.SpringUtil;
import com.cmcc.system.dao.SysPostDao;
import com.cmcc.system.dao.SysUserPostDao;
import com.cmcc.system.entity.SysPost;
import com.cmcc.system.entity.SysUser;
import com.cmcc.system.serivce.SysPostService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class SysPostServiceImpl implements SysPostService {

	private Logger log =  LoggerFactory.getLogger(this.getClass());
	
	@Autowired 
	private SysPostDao sysPostDao;
	@Autowired 
	private SysUserPostDao sysUserPostDao;
	
	@Override
	public Page<SysPost> getPage(Integer pageNum, Integer pageSize, String orderBy, SysPost sysPost) {
		//获取token中的用户数据
		SysUser user = (SysUser) SpringUtil.getBean("baseUser");
		sysPost.setLesseeId(user.getLesseeId());
		PageHelper.startPage(pageNum,pageSize,orderBy);
		return sysPostDao.selectPage(sysPost);
	}

	@Override
	public SysPost getById(String postId) {
		return sysPostDao.selectByPrimaryKey(postId);
	}

	@Override
	public Integer add(SysPost sysPost) {
		//获取token中的用户数据
		SysUser user = (SysUser) SpringUtil.getBean("baseUser");
		sysPost.setPostId(IdGenerateUtil.uuid3());
		sysPost.setCreateUser(user.getUserId());
		sysPost.setCreateTime(new Date());
		sysPost.setLesseeId(user.getLesseeId());
		if(StringUtils.isBlank(sysPost.getParentId())){
			sysPost.setParentId("0");
		}
		return sysPostDao.insertSelective(sysPost);
	}

	@Override
	public Integer update(SysPost sysPost) {
		//获取token中的用户数据
		SysUser user = (SysUser) SpringUtil.getBean("baseUser");
		sysPost.setUpdateUser(user.getUserId());
		sysPost.setUpdateTime(new Date());
		return sysPostDao.updateByPrimaryKeySelective(sysPost);
	}

	@Override
	public Integer delete(String id) {
		sysPostDao.deleteByPrimaryKey(id);
		sysUserPostDao.deleteByPostId(id);
		return 1;
	}

}
