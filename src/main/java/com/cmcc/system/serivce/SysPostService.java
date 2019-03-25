package com.cmcc.system.serivce;

import com.cmcc.system.entity.SysPost;
import com.github.pagehelper.Page;

/**
 * 
 * ClassName: SysPostService 
 * @Description: TODO 职位业务接口
 * @author zengzhibin
 * @date 2019年3月16日
 */
public interface SysPostService {

	/**
	 * 
	 * @Description: TODO 分页获取职位列表
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @param sysPost
	 * @return Page<SysPost>  
	 * @author zengzhibin
	 * @date 2019年3月16日
	 */
	Page<SysPost> getPage(Integer pageNum, Integer pageSize, String orderBy, SysPost sysPost);

	/**
	 * 
	 * @Description: TODO 根据ID获取职位信息
	 * @param postId
	 * @return SysPost  
	 * @author zengzhibin
	 * @date 2019年3月16日
	 */
	SysPost getById(String postId);

	/**
	 * 
	 * @Description: TODO 添加职位信息
	 * @param sysPost
	 * @return Integer  
	 * @author zengzhibin
	 * @date 2019年3月16日
	 */
	Integer add(SysPost sysPost);

	/**
	 * 
	 * @Description: TODO 更新职位信息
	 * @param sysPost
	 * @return 
	 * @return Integer  
	 * @author zengzhibin
	 * @date 2019年3月16日
	 */
	Integer update(SysPost sysPost);

	/**
	 * 
	 * @Description: TODO 删除职位信息，及下的用户关联
	 * @param id
	 * @return Integer  
	 * @author zengzhibin
	 * @date 2019年3月16日
	 */
	Integer delete(String id);


}
