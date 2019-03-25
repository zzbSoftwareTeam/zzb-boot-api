package com.cmcc.system.controller;

import com.cmcc.common.bean.Result;
import com.cmcc.common.bean.ResultCode;
import com.cmcc.system.entity.SysMenu;
import com.cmcc.system.entity.SysUser;
import com.cmcc.system.serivce.SysUserService;
import com.github.pagehelper.Page;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 
 * ClassName: SysUserController
 * 
 * @Description 用户控制类
 * @author zengzhibin
 * @date 2019年3月22日
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	/**
	 * 用户业务接口
	 */
	@Autowired
	private SysUserService sysUserService;

	/**
	 * 
	 * @Description 单个添加用户人员
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	@PutMapping("/register")
	@ApiOperation(value = "用户注册，不用带Authorization鉴权")
	public Result register(@ApiParam(name = "user", value = "用户常规信息", required = true) SysUser user) {
		try {
			Integer it = sysUserService.register(user);
			if (it > 0) {
				return Result.success();
			} else {
				return Result.failure(ResultCode.DATA_IS_WRONG);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}

	/**
	 * 
	 * @Description 单个添加用户人员
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	@PutMapping("/add")
	@ApiOperation(value = "添加单个用户")
	public Result addOne(@ApiParam(name = "user", value = "用户常规信息", required = true) SysUser user) {
		try {
			Integer it = sysUserService.add(user);
			if (it > 0) {
				return Result.success();
			} else {
				return Result.failure(ResultCode.DATA_IS_WRONG);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	
	/**
	 * 
	 * @Description 根据用户ID更新
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	@PostMapping("/{userId}")
	@ApiOperation(value = "更新用户信息")
	public Result updateOne(@ApiParam(name = "userId", value = "用户id", required = true) @PathVariable String userId,
			@ApiParam(name = "user", value = "用户常规字段修改信息", required = true) SysUser user) {
		try {
			user.setUserId(userId);
			Integer it = sysUserService.update(user);
			if (it > 0) {
				return Result.success();
			} else {
				return Result.failure(ResultCode.DATA_IS_WRONG);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}

	/**
	 * 
	 * @Description 根据用户ID逻辑删除用户
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	@DeleteMapping("/{userId}")
	@ApiOperation(value = "根据id删除用户")
	public Result removeOne(
			@ApiParam(name = "userId", value = "用户id", required = true) 
			@PathVariable String userId) {
		try {
			Integer it = sysUserService.delete(userId);
			if (it > 0) {
				return Result.success();
			} else {
				return Result.failure(ResultCode.DATA_IS_WRONG);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}

	/**
	 * 
	 * @Description 根据ID查询用户
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	@GetMapping("/{userId}")
	@ApiOperation(value = "根据id获取用户信息")
	public Result getOne(
			@ApiParam(name = "userId", value = "用户id", required = true) 
			@PathVariable String userId) {
		try {
			SysUser user = sysUserService.getOne(userId);
			return Result.success(user);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}

	/**
	 * 
	 * @Description 分页查询用户列表
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	@GetMapping("/page")
	@ApiOperation(value = "获取用户信息", notes = "带分页")
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
			SysUser SysUser){
		try {
			Page<SysMenu> page = sysUserService.getPage(pageNum, pageSize, orderBy, SysUser);
			return Result.failure(ResultCode.SUCCESS, page.toPageInfo());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	/**
	 * 
	 * @Description 多条件查询 用户信息
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	@GetMapping
	@ApiOperation(value = "带条件查询用户")
	public Result getUsers(SysUser user) {
		try {
			List<SysUser> users = sysUserService.getUsers(user);
			return Result.success(users);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	/**
	 * 
	 * @Description 重置密码
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	@PostMapping("/resetPass/{userId}")
	@ApiOperation(value = "根据id重置密码")
	public Result resetPass(
			@ApiParam(name = "userId", value = "用户id", required = true) 
			@PathVariable String userId) {
		try {
			Integer it = sysUserService.resetPass(userId);
			if (it > 0) {
				return Result.success();
			} else {
				return Result.failure(ResultCode.DATA_IS_WRONG);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	/**
	 * 
	 * @Description  批量离职
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月22日
	 */
	@PostMapping("/dimission")
	@ApiOperation(value = "批量离职")
	@ApiImplicitParam(name = "userIds", value = "用户ID数组（id1,id2,...）", required = true, paramType = "query",dataType = "String")
	public Result userDimission(String[] userIds) {
		try {
			Integer it = sysUserService.userDimission(userIds);
			if (it > 0) {
				return Result.success();
			} else {
				return Result.failure(ResultCode.DATA_IS_WRONG);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	
	@PostMapping("/userSort")
	@ApiOperation(value = "用户排序(开发中)")
	public Result userSort(
			@ApiParam(name="oldUserId",value="要排序的用户ID",required=true)
			String oldUserId,
			@ApiParam(name="nowUserId",value="要替换的用户ID",required=true)
			String nowUserId) {
		try {
			return Result.success();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}
	
}
