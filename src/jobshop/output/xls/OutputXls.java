package jobshop.output.xls;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class OutputXls
{
	public static void outputResultToXLS(ArrayList<Double> list) throws IOException
	{
		Workbook wb = new HSSFWorkbook();
		FileOutputStream fileOut = new FileOutputStream("E:\\Java codes\\workspace\\jobshop\\result\\EXP2.xls");
		
		Sheet s1 = wb.createSheet("Sheet 1");
		int index = 0;
		for(Double d:list)
		{
			Row row = s1.createRow(index++);
			row.createCell(0).setCellValue(d);
		}
		
		wb.write(fileOut);
		fileOut.close();
	}
	
	
	public static void main(String[] args) throws IOException
	{
		// TODO 自动生成的方法存根
		
	}

}
