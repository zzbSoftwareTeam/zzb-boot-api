package com.cmcc.system.serivce;


import com.cmcc.system.entity.SysOrg;

import java.util.List;
import java.util.Map;

/**
 * 组织机构业务接口
 * @author Administrator
 *
 */
public interface SysOrgService {

	/**
	 * 增加组织机构
	 * @param sysOrg 组织机构实体对象
	 * @return
	 */
	Integer addOrg(SysOrg sysOrg);

	/**
	 * 更新组织机构
	 * @param sysOrg 组织机构实体对象
	 * @return
	 */
	Integer saveOrg(SysOrg sysOrg);

	/**
	 * 根据机构id删除部门
	 * @param orgId 机构id
	 * @return
	 */
	Integer delOrg(String orgId);

	/**
	 * 根据机构id清空机构内人员
	 * @param orgId
	 * @param lesseeId 
	 * @return
	 */
	Integer saveOrgByUser(String orgId, String lesseeId);

	/**
	 * 
	 * @Description: TODO 获取所有机构
	 * @param sysOrg
	 * @return List<SysOrg>  
	 * @author zengzhibin
	 * @date 2019年3月20日
	 */
	List<SysOrg> getOrg(SysOrg sysOrg);

	/**
	 * 
	 * @Description: TODO 查询当前登录人管理的组织机构
	 * @param sysOrg
	 * @return List<SysOrg>  
	 * @author zengzhibin
	 * @date 2019年3月20日
	 */
	List<SysOrg> getUserOrg(SysOrg sysOrg);

	/**
	 * 
	 * @Description 查询指定组织机构ID下的人员数与负责人
	 * @return List<Map<String,Object>>  
	 * @author zengzhibin
	 * @date 2019年3月20日
	 */
	Map<String, Object> getOrgUser(String orgId);

	/**
	 * 
	 * @Description 根据机构名称查询
	 * @return orgName  
	 * @author zengzhibin
	 * @date 2019年3月21日
	 */
	SysOrg getOrgByName(String orgName);
}
