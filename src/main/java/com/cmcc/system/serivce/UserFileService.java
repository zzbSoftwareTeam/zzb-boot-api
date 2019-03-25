package com.cmcc.system.serivce;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cmcc.common.bean.Result;

/**
 * 
 * ClassName: UserFileService 
 * @Description 用户导入导出业务接口
 * @author zengzhibin
 * @date 2019年3月21日
 */
public interface UserFileService {

	/**
	 * 
	 * @Description 导入用户数据与机构数据
	 * @return Result  
	 * @author zengzhibin
	 * @date 2019年3月21日
	 */
	Result importUser(MultipartFile file);

	/**
	 * 
	 * @Description 获取要导出的用户数据
	 * @return List<Map<String,Object>>  
	 * @author zengzhibin
	 * @date 2019年3月23日
	 */
	List<Map<String, Object>> getExportUser();

}
