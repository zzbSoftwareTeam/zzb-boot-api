package com.cmcc.system.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmcc.common.bean.Result;
import com.cmcc.common.bean.ResultCode;
import com.cmcc.system.entity.SysPost;
import com.cmcc.system.serivce.SysPostService;
import com.github.pagehelper.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * ClassName: SysPostController 
 * @Description: TODO 职位控制类
 * @author zengzhibin
 * @date 2019年3月16日
 */
@Api(value="职位接口")
@RestController
@RequestMapping("/sysPost")
public class SysPostController {
	
private Logger log =  LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private SysPostService sysPostService;

	/**
	 * 
	 * @Description: TODO 分页带条件查询职位
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @param sysRole
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月16日
	 */
	@ApiOperation(value="分页获取职位", notes="分页带条件查询职位")
	@GetMapping(value = "/page")
	public Result getPage(
			@ApiParam(name="pageNum",value="页码，从1开始，默认为1",required=true)
			@RequestParam(value="pageNum",defaultValue="1")
			Integer pageNum,
			@ApiParam(name="pageSize",value="每页大小，默认为10",required=true)
			@RequestParam(value="pageSize",defaultValue="10")
			Integer pageSize,
			@ApiParam(name="orderBy",value="排序字段 ，‘order desc’",required=false)
			@RequestParam(value="orderBy",required=false)
			String orderBy,
			SysPost sysPost){
		try {
			Page<SysPost> page = sysPostService.getPage(pageNum, pageSize, orderBy, sysPost);
			return Result.failure(ResultCode.SUCCESS, page.toPageInfo());
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	
	/**
	 * 
	 * @Description: TODO 根据ID获取职位
	 * @param roleId
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月16日
	 */
	@ApiOperation(value="根据ID获取职位", notes="根据ID获取职位")
    @GetMapping(value = "/{postId}")
	public Result getById(
			@ApiParam(name="postId",value="职位ID",required=true)
			@PathVariable String postId){
    	try {
    		SysPost sysPost = sysPostService.getById(postId);
    		if(sysPost!=null){
    			return Result.success(sysPost);
    		}else{
    			return Result.failure(ResultCode.RESULE_DATA_NONE);
    		}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	
	/**
	 * 
	 * @Description: TODO 添加职位 
	 * @param sysRole
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月16日
	 */
	@ApiOperation(value="添加职位", notes="根据实体添加职位")
	@PutMapping
	public Result add(SysPost sysPost){
    	try {
    		Integer it = sysPostService.add(sysPost);
    		if(it==1){
    			return Result.success();
    		}else{
    			return Result.failure(ResultCode.DATA_IS_WRONG);
    		}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	
	/**
	 * 
	 * @Description: TODO 更新职位
	 * @param id
	 * @param sysRole
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月16日
	 */
	@ApiOperation(value="更新职位", notes="根据实体ID更新职位")
	@PostMapping("/{id}")
	public Result update(
			@ApiParam(name="id",value="职位ID",required=true)
			@PathVariable String id,
			SysPost sysPost){
    	try {
    		sysPost.setPostId(id);
    		Integer it = sysPostService.update(sysPost);
    		if(it==1){
    			return Result.success();
    		}else{
    			return Result.failure(ResultCode.RESULE_DATA_NONE);
    		}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	
	/**
	 * 
	 * @Description: TODO 删除职位，与其下的角色关系
	 * @param id
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月16日
	 */
	@ApiOperation(value="删除职位，与其下的角色关系", notes="删除职位，与其下的角色关系")
	@DeleteMapping("/{id}")
	public Result del(
			@ApiParam(name="id",value="职位ID",required=true)
			@PathVariable String id){
    	try {
    		Integer it = sysPostService.delete(id);
    		if(it==1){
    			return Result.success();
    		}else{
    			return Result.failure(ResultCode.RESULE_DATA_NONE);
    		}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
}
