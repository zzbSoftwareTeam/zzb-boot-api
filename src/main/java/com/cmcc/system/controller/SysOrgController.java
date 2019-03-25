package com.cmcc.system.controller;


import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cmcc.common.bean.Result;
import com.cmcc.common.bean.ResultCode;
import com.cmcc.common.utils.SpringUtil;
import com.cmcc.system.entity.SysOrg;
import com.cmcc.system.entity.SysUser;
import com.cmcc.system.serivce.SysOrgService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * ClassName: SysOrgController 
 * @Description 部门机构控制 类
 * @author zengzhibin
 * @date 2019年3月20日
 */
@Api(value="部门机构接口")
@RestController
@RequestMapping("/sysOrg")
public class SysOrgController {
	
	private Logger log =  LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SysOrgService sysOrgService; 
	
	/**	
	 * 
	 * @Description  增加组织机构
	 * @param sysOrg
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月20日
	 */
	@ApiOperation(value="增加组织机构", notes="根据实体添加组织机构")
	@PutMapping
	public Result addOrg(SysOrg sysOrg){
		try {
    		Integer it = sysOrgService.addOrg(sysOrg);
    		if(it > 0){
    			return Result.success();
    		}else{
    			return Result.failure(ResultCode.DATA_IS_WRONG);
    		}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	
	/**
	 * 
	 * @Description  更新组织机构
	 * @param orgId
	 * @param sysOrg
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月20日
	 */
	@ApiOperation(value="更新组织机构", notes="根据实体更新组织机构")
	@PostMapping(value = "/{orgId}")
	public Result saveOrg(
			@ApiParam(name="orgId",value="机构ID",required=true)
			@PathVariable String orgId,
			SysOrg sysOrg){
		try {
			sysOrg.setOrgId(orgId);
    		Integer it = sysOrgService.saveOrg(sysOrg);
    		if(it > 0){
    			return Result.success();
    		}else{
    			return Result.failure(ResultCode.DATA_IS_WRONG);
    		}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	
	/**
	 * 
	 * @Description  清空组织机构内人员
	 * @param orgId
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月20日
	 */
	@ApiOperation(value="清空组织机构内人员", notes="根据机构id更新组织机构内人员")
	@PostMapping(value = "/{orgId}/user")
	public Result saveOrgByUser(
			@ApiParam(name="orgId",value="机构ID",required=true)
			@PathVariable String orgId){
		SysUser user = (SysUser) SpringUtil.getBean("baseUser");
		String lesseeId = user.getLesseeId();
		try {
    		Integer it = sysOrgService.saveOrgByUser(orgId,lesseeId);
    		if(it > 0){
    			return Result.success();
    		}else{
    			return Result.failure(ResultCode.DATA_IS_WRONG);
    		}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	
	/**
	 * 
	 * @Description  删除组织机构
	 * @param orgId
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月20日
	 */
	@ApiOperation(value="删除组织机构", notes="根据机构id删除部门")
	@DeleteMapping(value = "/{orgId}")
	public Result delOrg(
			@ApiParam(name="orgId",value="机构ID",required=true)
			@PathVariable String orgId){
		try {
			Integer it = sysOrgService.delOrg(orgId);
    		if(it > 0){
    			return Result.success();
    		}else{
    			return Result.failure(ResultCode.DATA_IS_WRONG);
    		}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	
	/**
	 * 
	 * @Description  查询所有组织机构
	 * @param sysOrg
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月20日
	 */
	@ApiOperation(value="查询所有组织机构", notes="查询所有组织机构")
	@GetMapping(value = "/getOrg")
	public Result getOrg(SysOrg sysOrg){
		try {
			List<SysOrg> list = sysOrgService.getOrg(sysOrg);
    		return Result.success(list);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	
	/**
	 * 
	 * @Description  查询当前登录人管理的组织机构
	 * @param sysOrg
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月20日
	 */
	@ApiOperation(value="查询当前登录人管理的组织机构", notes="查询当前登录人管理的组织机构")
	@GetMapping(value = "/getUserOrg")
	public Result getUserOrg(SysOrg sysOrg){
		try {
			List<SysOrg> list = sysOrgService.getUserOrg(sysOrg);
    		return Result.success(list);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	
	/**
	 * 
	 * @Description  查询所有组织机构及人员数与负责人
	 * @param sysOrg
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月20日
	 */
	@ApiOperation(value="查询指定组织机构ID下的人员数与负责人", notes="查询指定组织机构ID下的人员数与负责人")
	@GetMapping(value = "/{orgId}/orgUser")
	public Result getOrgUser(
			@ApiParam(name="orgId",value="机构ID",required=true)
			@PathVariable String orgId){
		try {
			Map<String,Object> list = sysOrgService.getOrgUser(orgId);
    		return Result.success(list);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
}
