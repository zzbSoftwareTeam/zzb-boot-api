package com.cmcc.system.serivce.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.common.bean.Result;
import com.cmcc.common.bean.ResultCode;
import com.cmcc.common.utils.ExcelUtil;
import com.cmcc.common.utils.IdGenerateUtil;
import com.cmcc.common.utils.SpringUtil;
import com.cmcc.system.dao.SysOrgDao;
import com.cmcc.system.dao.SysPostDao;
import com.cmcc.system.dao.SysUserDao;
import com.cmcc.system.entity.SysOrg;
import com.cmcc.system.entity.SysPost;
import com.cmcc.system.entity.SysUser;
import com.cmcc.system.serivce.SysOrgService;
import com.cmcc.system.serivce.UserFileService;
import com.cmcc.system.vo.InUserVo;

/**
 * 
 * ClassName: UserFileServiceImpl 
 * @Description 用户机构导入导出业务实现类
 * @author zengzhibin
 * @date 2019年3月21日
 */
@Service
public class UserFileServiceImpl implements UserFileService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	//初始化密码值 
	@Value("${system.password}")
    public String PASSWORD;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private SysOrgDao sysOrgDao;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysPostDao sysPostDao;
	
	@Transactional
	@Override
	public Result importUser(MultipartFile file) {
		
		//验证文件格式
		String filename=file.getOriginalFilename();
		if (!filename.matches("^.+\\.(?i)(xls)$") && !filename.matches("^.+\\.(?i)(xlsx)$")) {
			return Result.failure(ResultCode.PARAM_FILE_FORMAT);
		}
		
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		try {
			//读取文件数据转成集合对象 
			map =  ExcelUtil.importExcel(file,InUserVo.class);
		} catch (IOException e) {
			log.error("解释导入文件异常："+e.getMessage());
			return Result.failure(ResultCode.PARAM_FILE_IOEX);
		}
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		List<Object> successList = map.get("success");
		List<Object> errorList = map.get("error");
		//操作人信息
		SysUser user = (SysUser) SpringUtil.getBean("baseUser");
		String lesseeId = user.getLesseeId();
		String userId = user.getUserId();
		//导入机构信息
		for (Object suData : successList) {
			Map<String,Object> sumap = (Map<String, Object>) suData;
			try {
				//获取文件中的机构数据
				String orgOne = sumap.get("orgOne")!=null?sumap.get("orgOne").toString():"";
				String orgTwo = sumap.get("orgTwo")!=null?sumap.get("orgTwo").toString():"";
				String orgThree = sumap.get("orgThree")!=null?sumap.get("orgThree").toString():"";
				String orgFour = sumap.get("orgFpur")!=null?sumap.get("orgFpur").toString():"";
				String orgFive = sumap.get("orgFive")!=null?sumap.get("orgFive").toString():"";
				//初始化机构ID
				String oneId=IdGenerateUtil.uuid3(),twoId=IdGenerateUtil.uuid3(),threeId=IdGenerateUtil.uuid3(),
						fourId=IdGenerateUtil.uuid3(),fiveId=IdGenerateUtil.uuid3();
				/**
				 * 1。判断文件中的机构是否存在。
				 * 2。一级机构没有二三四五级就是无效数据
				 * 3。一级一级机构去数据库查询和保存
				 * 4。返回最下级机构ID，放入map中，在保存用户的时候指定所属机构 
				 */
				if(orgOne!=""){
					SysOrg objOne = sysOrgService.getOrgByName(orgOne);
					if(objOne==null){
						objOne = new SysOrg();
						objOne.setCreateUser(userId);
						objOne.setCreateTime(new Date());
						objOne.setLesseeId(lesseeId);
						objOne.setOrgId(oneId);
						objOne.setOrgName(orgOne);
						objOne.setTopName(orgOne);
						objOne.setDescription(orgOne);
						objOne.setParentId("0");
						sysOrgDao.insertSelective(objOne);
					}else{
						oneId=objOne.getOrgId();
					}
					if(orgTwo!=""){
						SysOrg objTwo = sysOrgService.getOrgByName(orgTwo);
						if(objTwo==null){
							objTwo = new SysOrg();
							objTwo.setCreateUser(userId);
							objTwo.setCreateTime(new Date());
							objTwo.setLesseeId(lesseeId);
							objTwo.setOrgId(twoId);
							objTwo.setOrgName(orgTwo);
							objTwo.setTopName(orgTwo);
							objTwo.setDescription(orgTwo);
							objTwo.setParentId(oneId);
							sysOrgDao.insertSelective(objTwo);
						}else{
							twoId=objTwo.getOrgId();
						}
						if(orgThree!=""){
							SysOrg objThree = sysOrgService.getOrgByName(orgThree);
							if(objThree==null){
								objThree = new SysOrg();
								objThree.setCreateUser(userId);
								objThree.setCreateTime(new Date());
								objThree.setLesseeId(lesseeId);
								objThree.setOrgId(threeId);
								objThree.setOrgName(orgThree);
								objThree.setTopName(orgThree);
								objThree.setDescription(orgThree);
								objThree.setParentId(twoId);
								sysOrgDao.insertSelective(objThree);
							}else{
								threeId=objThree.getOrgId();
							}
							if(orgFour!=""){
								SysOrg objFour = sysOrgService.getOrgByName(orgFour);
								if(objFour==null){
									objFour = new SysOrg();
									objFour.setCreateUser(userId);
									objFour.setCreateTime(new Date());
									objFour.setLesseeId(lesseeId);
									objFour.setOrgId(fourId);
									objFour.setOrgName(orgFour);
									objFour.setTopName(orgFour);
									objFour.setDescription(orgFour);
									objFour.setParentId(threeId);
									sysOrgDao.insertSelective(objFour);
								}else{
									fourId=objFour.getOrgId();
								}	
								if(orgFive!=""){
									SysOrg objFive = sysOrgService.getOrgByName(orgFive);
									if(objFive==null){
										objFive = new SysOrg();
										objFive.setCreateUser(userId);
										objFive.setCreateTime(new Date());
										objFive.setLesseeId(lesseeId);
										objFive.setOrgId(fiveId);
										objFive.setOrgName(orgFive);
										objFive.setTopName(orgFive);
										objFive.setDescription(orgFive);
										objFive.setParentId(fourId);
										sysOrgDao.insertSelective(objFive);
									}else{
										sumap.put("findOrgId", objFive.getOrgId());
									}
								}else{
									sumap.put("findOrgId", fourId);
								}
							}else{
								sumap.put("findOrgId", threeId);
							}
						}else{
							sumap.put("findOrgId", twoId);
						}
					}else{
						sumap.put("findOrgId", oneId);
					}
				}else{
					sumap.put("findOrgId", "");
				}
			} catch (Exception e) {
				log.error("导入机构时异常，"+sumap.toString()+"; "+e.getMessage());
			}
			dataList.add(sumap);
		}
		
		//导入用户人员信息
		for (Map<String,Object> sumap : dataList) {
			try {
				String orgId = sumap.get("findOrgId").toString();
				if(StringUtils.equals(orgId, "")){
					errorList.add(sumap);
				}else{
					SysUser sysUser = new SysUser();
					sysUser.setOrgId(orgId);
					sysUser.setUserId(IdGenerateUtil.uuid3());
					sysUser.setUserName(sumap.get("userName")!=null?sumap.get("userName").toString():"");
					sysUser.setUserAccount(sumap.get("userAccount")!=null?sumap.get("userAccount").toString():"");
					sysUser.setUserTel(sumap.get("userTel")!=null?sumap.get("userTel").toString():"");
					sysUser.setSeTel(sumap.get("seTel")!=null?sumap.get("seTel").toString():"");
					sysUser.setTelephone(sumap.get("telephone")!=null?sumap.get("telephone").toString():"");
					sysUser.setEmail(sumap.get("email")!=null?sumap.get("email").toString():"");
					String status = sumap.get("status")!=null?sumap.get("status").toString():"";
					sysUser.setStatus(status=="在职"?"0":"1");
					//职位
					if(sumap.get("postId")!=null){
						String postName = sumap.get("postId").toString();
						SysPost post = sysPostDao.selectByPostName(postName);
						sysUser.setPostId(post.getPostName());
					}
					//默认值 
					sysUser.setUserPinyin("");
					sysUser.setUserPass(bCryptPasswordEncoder.encode(PASSWORD));
					sysUser.setDelFlag("0");
					sysUser.setUserBookId(lesseeId);
					sysUser.setCreateUser(user.getUserId());
					sysUser.setCreateTime(new Date());
					sysUser.setLesseeId(lesseeId);
					sysUserDao.insertSelective(sysUser);
					//用户附属表保存
					//String affiliateSql="";
					//sysUserDao.insertAffiliate(affiliateSql);
				}
			} catch (Exception e) {
				log.error("导入用户时数据转换保存异常，"+sumap.toString()+"; "+e.getMessage());
				errorList.add(sumap);
			}
		}
		return Result.success(errorList);
	}

	@Override
	public List<Map<String, Object>> getExportUser() {
		return sysUserDao.selectExportUser();
	}

}
