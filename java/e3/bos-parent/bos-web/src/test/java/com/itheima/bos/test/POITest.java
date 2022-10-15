package com.itheima.bos.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

public class POITest {
	//使用POI解析Excel文件
	@Test
	public void test1() throws FileNotFoundException, IOException{
		String filePath = "C:\\Users\\zhaoqx\\Desktop\\BOS项目(黑马32期)\\BOS-day05\\资料\\区域导入测试数据.xls";
		//包装一个Excel文件对象
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(filePath)));
		//读取文件中第一个Sheet标签页
		HSSFSheet hssfSheet = workbook.getSheetAt(0);
		//遍历标签页中所有的行
		for (Row row : hssfSheet) {
			int rowNum = row.getRowNum();//行号
			if(rowNum == 0){
				continue;
			}
			System.out.println();
			for (Cell cell : row) {
				String value = cell.getStringCellValue();
				System.out.print(value + " ");
			}
		}
	}
}
