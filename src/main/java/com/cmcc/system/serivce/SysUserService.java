package com.cmcc.system.serivce;

import com.cmcc.system.entity.SysMenu;
import com.cmcc.system.entity.SysUser;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * 
 * ClassName: SysUserService 
 * @Description 用户业务接口
 * @author zengzhibin
 * @date 2019年3月22日
 */
public interface SysUserService {

	/**
	 * 
	 * @Description 添加用户
	 * @return Integer  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	Integer add(SysUser user);

	/**
	 * 
	 * @Description 更新用户
	 * @return Integer  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	Integer update(SysUser user);

	/**
	 * 
	 * @Description 逻辑删除用户
	 * @return Integer  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	Integer delete(String userId);

	/**
	 * 
	 * @Description 根据用户ID获取用户完整信息
	 * @return SysUser  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	SysUser getOne(String userId);

	/**
	 * 
	 * @Description 分页查询用户
	 * @return Page<SysMenu>  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	Page<SysMenu> getPage(Integer pageNum, Integer pageSize, String orderBy, SysUser sysUser);

	/**
	 * 
	 * @Description 多条件查询用户信息
	 * @return List<SysUser>  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	List<SysUser> getUsers(SysUser user);

	/**
	 * 
	 * @Description 根据userID重置密码
	 * @return Integer  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	Integer resetPass(String userId);

	/**
	 * 
	 * @Description 批量用户离职
	 * @return Integer  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	Integer userDimission(String[] userIds);

	Integer register(SysUser user);


}
