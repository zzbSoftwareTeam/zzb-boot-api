package com.cmcc.system.serivce.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.common.utils.IdGenerateUtil;
import com.cmcc.common.utils.SpringUtil;
import com.cmcc.system.dao.SysOrgDao;
import com.cmcc.system.dao.SysUserDao;
import com.cmcc.system.entity.SysOrg;
import com.cmcc.system.entity.SysUser;
import com.cmcc.system.serivce.SysOrgService;

/**
 * 
 * ClassName: SysOrgServiceImpl 
 * @Description: TODO 机构部门业务实现类
 * @author zengzhibin
 * @date 2019年3月20日
 */
@Service
public class SysOrgServiceImpl implements SysOrgService{
	
	@Autowired
	private SysOrgDao sysOrgDao;
	
	@Autowired
	private SysUserDao sysUserDao;

	@Override
	public synchronized Integer addOrg(SysOrg sysOrg) {
		sysOrg.setOrgId(IdGenerateUtil.uuid3());
		SysUser user = (SysUser) SpringUtil.getBean("baseUser");
		sysOrg.setCreateUser(user.getUserId());
		sysOrg.setLesseeId(user.getLesseeId());
		sysOrg.setCreateTime(new Date());
		
		if(StringUtils.isBlank(sysOrg.getParentId())){//如果没选上级就默认为0
			sysOrg.setParentId("0");
		}
		Integer sort = sysOrgDao.selectMaxSort();//同步线程，保证值的正确性
		if(sort!=null){
			sysOrg.setSort(sort+1);
		}else{
			sysOrg.setSort(0);
		}
		return sysOrgDao.insertSelective(sysOrg);
	}

	@Override
	public Integer saveOrg(SysOrg sysOrg){
		SysUser user = (SysUser) SpringUtil.getBean("baseUser");
		sysOrg.setUpdateUser(user.getUserId());
		sysOrg.setUpdateTime(new Date());
		return sysOrgDao.updateByPrimaryKeySelective(sysOrg);
	}

	@Transactional
	@Override
	public Integer delOrg(String orgId) {
		return sysOrgDao.deleteByPrimaryKey(orgId);
	}

	@Override
	public List<SysOrg> getOrg(SysOrg sysOrg) {
		SysUser user = (SysUser) SpringUtil.getBean("baseUser");
		sysOrg.setLesseeId(user.getLesseeId());
		return sysOrgDao.selectOrgs(sysOrg);
	}

	@Override
	public List<SysOrg> getUserOrg(SysOrg sysOrg) {
		SysUser user = (SysUser) SpringUtil.getBean("baseUser");
		sysOrg.setLesseeId(user.getLesseeId());
		sysOrg.setMyUserId(user.getUserId());
		return sysOrgDao.selectUserOrgs(sysOrg);
	}

	
	@Override
	public Map<String, Object> getOrgUser(String orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> um = sysOrgDao.selectOrgUserNum(orgId);
		List<Map<String, Object>> pm = sysOrgDao.selectOrgPost(orgId);
		map.put("orgId", orgId);
		if(um!=null){
			map.put("nums", um.get("nums"));
		}
		if(pm!=null){
			map.put("user", pm);
		}
		return map;
	}

	@Override
	public SysOrg getOrgByName(String orgName) {
		SysOrg sysOrg = new SysOrg();
		SysUser user = (SysUser) SpringUtil.getBean("baseUser");
		sysOrg.setLesseeId(user.getLesseeId());
		sysOrg.setOrgName(orgName);
		List<SysOrg> list = sysOrgDao.selectOrgs(sysOrg);
		if(list.size()==1){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public Integer saveOrgByUser(String orgId, String lesseeId) {
		sysUserDao.updateUserByOrgId(orgId,lesseeId);
		return 1;
	}



}
