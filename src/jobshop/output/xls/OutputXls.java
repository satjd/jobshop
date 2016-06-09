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
	private static Workbook wb = new HSSFWorkbook();
	public static <T> void outputResultToXLS(String xlsPath,String description,ArrayList<T> list) throws IOException
	{
		FileOutputStream fileOut = new FileOutputStream(xlsPath);
		
		Sheet s1 = wb.createSheet(description);
		int index = 0;
		for(T d:list)
		{
			Row row = s1.createRow(index++);
			row.createCell(0).setCellValue((double) d);
		}
		
		wb.write(fileOut);
		fileOut.close();
	}
	
	
	public static void main(String[] args) throws IOException
	{
		// TODO 自动生成的方法存根
		
	}

}
