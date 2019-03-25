package com.cmcc.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * ClassName: ExcelUtil
 * 
 * @Description excel导出导出工具类
 * @author zengzhibin
 * @date 2019年3月21日
 */
public class ExcelUtil {

	private static Logger log = LoggerFactory.getLogger(ExcelUtil.class);

	/**
	 * 
	 * @Description 导入excel，传入文件与实体，返回解释与的实体集合
	 * @return Map<String,List<Object>> 解释成功与失败的实体集合
	 * @author zengzhibin
	 * @date 2019年3月21日
	 */
	@SuppressWarnings("resource")
	public static Map<String, List<Object>> importExcel(MultipartFile file, Class<?> clazz) throws IOException {
		// 判断Excel文件的版本
		String filename = file.getOriginalFilename();
		boolean isExcel2003 = true;
		if (filename.matches("^.+\\.(?i)(xlsx)$")) {
			isExcel2003 = false;
		}
		// 成功读取与失败读取数据的集合
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		List<Object> successList = new ArrayList<Object>();
		List<Object> errorList = new ArrayList<Object>();
		// 装载流
		InputStream is = file.getInputStream();
		// 创建book
		Workbook hw = null;
		if (isExcel2003) {
			hw = new HSSFWorkbook(is);
		} else {
			hw = new XSSFWorkbook(is);
		}
		// 获取第一个sheet页
		Sheet sheet = hw.getSheetAt(0);
		// 遍历行 从下标第一行开始（去除标题）
		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			try {
				Row row = sheet.getRow(i);
				if (row != null) {
					successList.add(dataObj(clazz, row));
				}
			} catch (Exception e) {
				errorList.add(i);
				log.error("导入第" + i + "题目时，出现异常，" + e.getMessage());
			}
		}
		map.put("success", successList);
		map.put("error", errorList);
		return map;
	}

	public static Map<String, List<Object>> importExcel(MultipartFile file,String[] titles) throws IOException {
		// 判断Excel文件的版本
		String filename = file.getOriginalFilename();
		boolean isExcel2003 = true;
		if (filename.matches("^.+\\.(?i)(xlsx)$")) {
			isExcel2003 = false;
		}
		// 成功读取与失败读取数据的集合
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		List<Object> successList = new ArrayList<Object>();
		List<Object> errorList = new ArrayList<Object>();
		// 装载流
		InputStream is = file.getInputStream();
		// 创建book
		Workbook hw = null;
		if (isExcel2003) {
			hw = new HSSFWorkbook(is);
		} else {
			hw = new XSSFWorkbook(is);
		}
		// 获取第一个sheet页
		Sheet sheet = hw.getSheetAt(0);
		// 遍历行 从下标第一行开始（去除标题）
		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			try {
				Row row = sheet.getRow(i);
				if (row != null) {
					Map<String, Object> mapCall = new HashMap<String, Object>();
					for (int j = 0; j < row.getLastCellNum(); j++) {
						Cell cell = row.getCell(j);
						mapCall.put(titles[j], getVal(cell));
					}
					successList.add(mapCall);
				}
			} catch (Exception e) {
				errorList.add(i);
				log.error("导入第" + i + "题目时，出现异常，" + e.getMessage());
			}
		}
		map.put("success", successList);
		map.put("error", errorList);
		return map;
	}
	
	public static Map<String, List<Object>> importExcel(MultipartFile file) throws IOException {
		// 判断Excel文件的版本
		String filename = file.getOriginalFilename();
		boolean isExcel2003 = true;
		if (filename.matches("^.+\\.(?i)(xlsx)$")) {
			isExcel2003 = false;
		}
		// 成功读取与失败读取数据的集合
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		List<Object> successList = new ArrayList<Object>();
		List<Object> errorList = new ArrayList<Object>();
		// 装载流
		InputStream is = file.getInputStream();
		// 创建book
		Workbook hw = null;
		if (isExcel2003) {
			hw = new HSSFWorkbook(is);
		} else {
			hw = new XSSFWorkbook(is);
		}
		// 获取第一个sheet页
		Sheet sheet = hw.getSheetAt(0);
		List<String> titles = new ArrayList<String>();
		Row rowOne = sheet.getRow(0);
		if (rowOne != null) {
			for (Cell cell : rowOne) {
				titles.add(getVal(cell));
			}
		}
		// 遍历行 从下标第一行开始（去除标题）
		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			try {
				Row row = sheet.getRow(i);
				if (row != null) {
					Map<String, Object> mapCall = new HashMap<String, Object>();
					for (int j = 0; j < row.getLastCellNum(); j++) {
						Cell cell = row.getCell(j);
						mapCall.put(titles.get(j), getVal(cell));
					}
					successList.add(mapCall);
				}
			} catch (Exception e) {
				errorList.add(i);
				log.error("导入第" + i + "题目时，出现异常，" + e.getMessage());
			}
		}
		map.put("success", successList);
		map.put("error", errorList);
		return map;
	}
	
	/**
	 * 
	 * @Description java反射机制，将row转成map
	 * @return Map<String,Object>  
	 * @author zengzhibin
	 * @date 2019年3月21日
	 */
	private static Map<String, Object> dataObj(Class<?> rowClazz, Row row) {
		Field[] fields = FieldUtils.getAllFields(rowClazz);
		if (fields == null || fields.length < 1) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 注意excel表格字段顺序要和obj字段顺序对齐
		for (int j = 0; j < fields.length; j++) {
			map.put(fields[j].getName(), getVal(row.getCell(j)));
		}
		return map;
	}

	private static String getVal(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		}
		// 判断数据的类型
		CellType cellType = cell.getCellTypeEnum();
		switch (cellType) {
		case NUMERIC: // 数字
			if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
				SimpleDateFormat sdf = null;
				// 验证short值
				if (cell.getCellStyle().getDataFormat() == 14) {
					sdf = new SimpleDateFormat("yyyy/MM/dd");
					Date date = cell.getDateCellValue();
					cellValue = sdf.format(date);
				} else if (cell.getCellStyle().getDataFormat() == 21) {
					sdf = new SimpleDateFormat("HH:mm:ss");
					Date date = cell.getDateCellValue();
					cellValue = sdf.format(date);
				} else if (cell.getCellStyle().getDataFormat() == 22) {
					sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date date = cell.getDateCellValue();
					cellValue = sdf.format(date);
				}
			} else if (cell.getCellStyle().getDataFormat() == 0) {// 处理数值格式
				cell.setCellType(CellType.STRING);
				cellValue = String.valueOf(cell.getRichStringCellValue().getString());
			}
			break;
		case STRING: // 字符串
			cellValue = String.valueOf(cell.getStringCellValue());
			break;
		case BOOLEAN: // Boolean
			cellValue = String.valueOf(cell.getBooleanCellValue());
			break;
		case FORMULA: // 公式
			cellValue = String.valueOf(cell.getCellFormula());
			break;
		case BLANK: // 空值
			cellValue = null;
			break;
		case ERROR: // 故障
			cellValue = "非法字符";
			break;
		default:
			cellValue = "未知类型";
			break;
		}
		return cellValue;
	}
	
	/**
	 * 创建excel文档并写入outStream输出流中
	 * @param outStream
	 * @param mainTitle
	 * @param titles
	 * @param contents
	 */
	public static HSSFWorkbook exportExcel(Class<?> clazz, List<Map<String,Object>> dataList) {
		HSSFWorkbook wb = null;
		wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("sheet1");
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        HSSFRow row = null;
		Field[] fields = FieldUtils.getAllFields(clazz);
		if (fields != null || fields.length >0) {
			//创建标题
			row = sheet.createRow(0);
			for (int i = 0; i < fields.length; i++) {
				HSSFCell cell = row.createCell(i);
	            cell.setCellValue(fields[i].getName());
	            cell.setCellStyle(style);
			}
			//创建内容
			if(dataList!=null&&dataList.size()>0){
				for(int i=0;i<dataList.size();i++){
					row = sheet.createRow(i+1);
					HSSFCell cell = row.createCell(i);
					Map<String,Object> map = dataList.get(i);
					for (int j = 0; j < fields.length; j++) {
						cell = row.createCell(j);
						String value = map.get(fields[j].getName())==null?"":map.get(fields[j].getName()).toString();
						cell.setCellValue(value);
					}
				}
			}
		}
        return wb;
	}
	
	public static HSSFWorkbook exportExcel(String[] title,Class<?> clazz, List<Map<String,Object>> dataList) {
		HSSFWorkbook wb = null;
		wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("sheet1");
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        HSSFRow row = null;
        //创建标题
		row = sheet.createRow(0);
		for (int i = 0; i < title.length; i++) {
			HSSFCell cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
		}
		//创建内容
		Field[] fields = FieldUtils.getAllFields(clazz);
		if (fields != null || fields.length >0) {
			if(dataList!=null&&dataList.size()>0){
				for(int i=0;i<dataList.size();i++){
					row = sheet.createRow(i+1);
					HSSFCell cell = row.createCell(i);
					Map<String,Object> map = dataList.get(i);
					for (int j = 0; j < fields.length; j++) {
						cell = row.createCell(j);
						String value = map.get(fields[j].getName())==null?"":map.get(fields[j].getName()).toString();
						cell.setCellValue(value);
					}
				}
			}
		}
        return wb;
	}
}
