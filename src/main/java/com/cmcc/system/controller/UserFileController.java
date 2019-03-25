package com.cmcc.system.controller;

import com.cmcc.common.bean.Result;
import com.cmcc.common.bean.ResultCode;
import com.cmcc.common.utils.ExcelUtil;
import com.cmcc.system.serivce.UserFileService;
import com.cmcc.system.vo.InUserVo;
import com.cmcc.system.vo.OutUserVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 
 * ClassName: UserFileController
 * 
 * @Description 用户导入导出控制类
 * @author zengzhibin
 * @date 2019年3月21日
 */
@Api(value="用户导入导出接口")
@RestController
@RequestMapping("/file")
public class UserFileController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private UserFileService userFileService;

	/**
	 * 
	 * @Description 导入用户与机构数据
	 * @return Result
	 * @author zengzhibin
	 * @date 2019年3月21日
	 */
	@ApiOperation(value = "导入用户与机构数据", notes = "导入用户与机构数据")
	@PostMapping("/importUser")
	public Result importUser(
			@ApiParam(name = "file", value = "文件模板", required = true) @RequestParam("file") MultipartFile file) {
		try {
			return userFileService.importUser(file);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
	}

	/**
	 * 
	 * @Description 下载用户机构导入的excl模板
	 * @return ResponseEntity<byte[]>  
	 * @author zengzhibin
	 * @date 2019年3月21日
	 */
	@ApiOperation(value = "下载用户与机构导入模板", notes = "下载用户与机构导入模板")
	@GetMapping(value="/exportUserModel")  
	public void exportModel(HttpServletResponse response) {
		HSSFWorkbook wb = null;
		OutputStream out = null;
		String fileName  = "用户导入模板.xls";
		try {
			String[] titles = {"一级部门", "二级部门","三级部门", "四级部门", "五级部门","工号", "姓名","手机", "短号码",
					"固定电话", "电子邮件", "是否允许登陆","职务"};
			wb = ExcelUtil.exportExcel(titles,InUserVo.class,null);
	        response.setContentType("application/force-download");
	        response.addHeader("Content-Disposition","attachment;fileName=" +new String(fileName.getBytes("UTF-8"),"iso-8859-1"));
	        out = response.getOutputStream();
			wb.write(out);
            out.flush();
		}catch (UnsupportedEncodingException e) {
			log.error("文件名转码异常");
		}catch (FileNotFoundException e) {
			log.error("文件流创建异常");
		}catch (IOException e) {
			log.error("文件流读写异常");
		}finally{
			try {
				out.close();
				wb.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}
	
	/**
	 * 
	 * @Description 导出用户与机构数据
	 * @return void  
	 * @author zengzhibin
	 * @date 2019年3月23日
	 */
	@ApiOperation(value = "导出用户与机构数据", notes = "导出用户与机构数据")
	@GetMapping(value="/exportUser")  
	public void exportUser(HttpServletResponse response) {
		HSSFWorkbook wb = null;
		OutputStream out = null;
		String fileName  = "用户机构.xls";
		try {
			List<Map<String,Object>> list = userFileService.getExportUser();
			
			String[] titles = {"部门","工号", "姓名","手机", "短号码",
					"固定电话", "电子邮件", "是否允许登陆","职务"};
			wb = ExcelUtil.exportExcel(titles,OutUserVo.class, list);
	        response.setContentType("application/force-download");
	        response.addHeader("Content-Disposition","attachment;fileName=" +new String(fileName.getBytes("UTF-8"),"iso-8859-1"));
	        out = response.getOutputStream();
			wb.write(out);
            out.flush();
		}catch (UnsupportedEncodingException e) {
			log.error("文件名转码异常");
		}catch (FileNotFoundException e) {
			log.error("文件流创建异常");
		}catch (IOException e) {
			log.error("文件流读写异常");
		}finally{
			try {
				out.close();
				wb.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}
}
