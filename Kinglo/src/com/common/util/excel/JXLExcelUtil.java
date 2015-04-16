package com.common.util.excel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.common.util.validator.ValidUtil;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class JXLExcelUtil<T> {
	/**************写Excel相关参数****************/
	public int pageCounts = 60000;//每个Sheet的最大导出行数
	
	private WritableWorkbook wb;//写工作薄
	
	private List<WritableSheet> sheetList;//sheet数列表
	
	private WritableSheet sheet;//sheet
	/**************写Excel相关参数****************/
	
	/**************读Excel相关参数****************/
	private Workbook workBook;//读工作薄
	
	public int maxFileSize=10;//单位为M
	
	private List<T> dataList;
	
	/**************读Excel相关参数****************/
	
	
	public void importExcel(File file,String[] fieldName,Class<T> classes){
	    try {
		if(ValidUtil.isExcel(file.getName())){
			workBook = Workbook.getWorkbook(file);
			Sheet[] sheets=workBook.getSheets();//获取所有的Sheet
			for (int i = 0; i < sheets.length; i++) {
			    int rowNum = sheets[i].getRows();//获取行的总数
			    for (int j = 0; j < rowNum; j++) {
				Cell[] cells = sheets[i].getRow(j);
				if(null != fieldName && cells.length==fieldName.length){
				    for (int l = 0; l < cells.length; l++) {
					T t = classes.newInstance();
					Method[] methods = classes.getMethods();
					for(Method method:methods){
					    if(method.getName(),equals()){
						
					    }
					}
					if(cells[l].getType()==CellType.NUMBER_FORMULA){
						
					}
				    }
				}else{
				    
				}
			    }
			}    
		    }else{
			
		    }
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	
	/**
	 * 导出EXCEL
	 * @param content       	单元格填充内容
	 * @param columnWidth		设置列宽
	 * @param title				设置标题
	 * @param os				输出流
	 * @param titleCellFormat	标题样式  NULL会启用默认样式
	 * @param contentCellFormat 内容样式  NULL会启用默认样式
	 */
	public void exportExcel(ExcelDataFormat[][] content,int[] columnWidth,String[] title,OutputStream os,WritableCellFormat titleCellFormat,WritableCellFormat contentCellFormat){
	try {
		int exportRowNum = content==null?0:content.length;
		createSheet(os,exportRowNum);
			
			setColumnWidth(columnWidth);
			
			setTitle(title,titleCellFormat);
			
			setCellContent(content,contentCellFormat);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null!=wb){
					wb.close();
				}
				if(null!=os){
					os.close();
				}
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//创建sheet数
	private void createSheet(OutputStream os,int totalCount) throws Exception{
		int sheetNum = (int)Math.ceil((double)totalCount/pageCounts);
		
		if(sheetNum==0){
			sheetNum = 1;
		}
		
		wb = Workbook.createWorkbook(os);
		
		sheetList = new ArrayList<WritableSheet>();
		
		for (int i = 0; i < sheetNum; i++) {
			sheet = wb.createSheet("sheet"+(i+1),i);
			sheetList.add(sheet);
		}
	}
	
	//设置列宽
	private void setColumnWidth(int[] columnWidth){
		for (int i = 0; i < sheetList.size(); i++) {
			for (int j = 0; j < columnWidth.length; j++) {
				sheetList.get(i).setColumnView(j, columnWidth[j]);
			}
		}
	}
	
	//创建标题
	private void setTitle(String[] title,WritableCellFormat titelCellFormat) throws Exception{
		
		//标题样式
		WritableFont font = new  WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
		WritableCellFormat defaultCellFormat = new WritableCellFormat(font);
		defaultCellFormat.setBackground(Colour.YELLOW);
		defaultCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		defaultCellFormat.setAlignment(Alignment.CENTRE);
		defaultCellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		
		Label label = null;
		for (int i = 0; i < sheetList.size(); i++) {
			sheetList.get(i).setRowView(0, 400);//设置第一行行高
			for (int j = 0; j < title.length; j++) {
				if(null == titelCellFormat){
					label = new Label(j,0,title[j],defaultCellFormat);
				}else{
					label = new Label(j,0,title[j],titelCellFormat);
				}
				sheetList.get(i).addCell(label);
			}
		}
	}
	
	
	//添加内容
	private void setCellContent(ExcelDataFormat[][] content,WritableCellFormat contentCellFormat) throws Exception{
		Label label = null;
		Number number = null;
		jxl.write.Boolean bool = null;
		
		
		//内容样式
		WritableFont contentFont = new  WritableFont(WritableFont.ARIAL, 11, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
		WritableCellFormat defaultCellFormat = new WritableCellFormat(contentFont);
		defaultCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		defaultCellFormat.setAlignment(Alignment.CENTRE);
		defaultCellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		int count=0;//计算sheet数
		
		WritableCellFormat usedCellFormat = contentCellFormat==null?defaultCellFormat:contentCellFormat;//将会被使用到的内容样式
		
		if(content!=null){
			for (int i = 0; i < sheetList.size(); i++) {
				for (int j = 1+count*pageCounts; j <= content.length; j++) {
					
					sheetList.get(count).setRowView(j, 280);//设置行高
					
					for (int k = 0; k < content[1].length; k++) {
						
						if(content[j-1][k].getCellType().equals(ExcelCellType.CELL_TYPE_BOOLEAN)){
							bool = new jxl.write.Boolean(k,j-(count*pageCounts),new java.lang.Boolean(content[j-1][k].getValue().toString()),usedCellFormat);
							sheetList.get(count).addCell(bool);
						}else if(content[j-1][k].getCellType().equals(ExcelCellType.CELL_TYPE_NUMBER.getValue())){
							number = new Number(k,j-(count*pageCounts),(Double)content[j-1][k].getValue(),usedCellFormat);
							sheetList.get(count).addCell(number);
						}else if(content[j-1][k].getCellType().equals(ExcelCellType.CELL_TYPE_STRING.getValue())){
							label = new Label(k,j-(count*pageCounts),content[j-1][k].getValue().toString(),usedCellFormat);
							sheetList.get(count).addCell(label);
						}
						
					}
					
					/**
					 * 如果Excel导出数目等于用户设置的一个Sheet最大的导出数  
					 * 那么跳转到下一个Sheet继续导出
					 */
					if(j%pageCounts==0){
						count++;
					}
				}
			}
		}
		wb.write();
	}
}
