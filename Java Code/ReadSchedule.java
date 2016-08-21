package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadSchedule {
   public String shiftPath="C:\\Users\\Anup\\Desktop\\BusDataSchedule\\tblShifts.xlsx";
   public String writePath="C:\\Users\\Anup\\Desktop\\BusDataSchedule\\tblShifts.csv";
   public String readPath="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\tblShifts.csv";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
    ReadSchedule sd=new ReadSchedule();
    System.out.println(sd.readShiftTable());
   // System.out.println(sd.getSchedule("W-E2.2"));
   // sd.readExcelFile(sd.shiftPath,"2,4,5");
	}
	
	public  HashMap<String,HashMap<String,String>> readShiftTable()
	{
		BufferedReader br=null;
		String sCurrentLine=null; 
		HashMap<String,HashMap<String,String>> scheduleMap=new HashMap<>();
		try
		{
			br = new BufferedReader(new FileReader(readPath));
			while ((sCurrentLine = br.readLine()) != null)
			{
				String arr[]=sCurrentLine.split(",");
				if(scheduleMap.containsKey(arr[0]))
				{
					
					HashMap<String,String> tempMap=scheduleMap.get(arr[0]);
					if(tempMap.containsKey(arr[2]))
					{
						if(!tempMap.get(arr[2]).equals(getSchedule(arr[1])))
						{
							System.out.println("Error ! Different value found "+arr[2]);
						}
						
					}
					else
					{
						tempMap.put(arr[2].trim(), getSchedule(arr[1]));
						scheduleMap.put(arr[0].trim(), tempMap);
					}
					
					
				}
				else
				{
					HashMap<String,String> hm=new HashMap<>();
					String schedule=getSchedule(arr[1]);
					if(!schedule.isEmpty())
					{
						hm.put(arr[2].trim(),schedule);
						
					}
					scheduleMap.put(arr[0].trim(), hm);
				}
				
			}
			br.close();
		}catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
		return scheduleMap;
	}
	
	
	public String getSchedule(String scheduleName)
	{
		if(scheduleName.contains("SNOW")||scheduleName.contains("Special")|| scheduleName.contains("Auditorium") || scheduleName.contains("MLK"))
		{
			return "";
		}
		
		String arr[]=scheduleName.split("-");
		if(arr.length==3)
		{
			String schedule[]=arr[2].split("\\.");
			return schedule[0];
		}
		else if(arr.length==2)
		{
			String schedule[]=arr[1].split("\\.");
			return schedule[0];
		}
		
		return "";
	}
	public ArrayList<String> readExcelFile(String path,String columnToSkip)
	{
		FileInputStream file;
		ArrayList<Integer> skip=convertStringToArrayList(columnToSkip);
		ArrayList<String> key=new ArrayList<>();
		ArrayList<String> stringJson=new ArrayList<>();
		boolean isHeader=true;
		try {
			file = new FileInputStream(new File(path));
			 XSSFWorkbook workbook = new XSSFWorkbook(file);
			  PrintWriter writer = new PrintWriter(writePath, "UTF-8");

					
	         //Get first/desired sheet from the workbook
			 SimpleDateFormat format=new SimpleDateFormat("h:mm a"); 
			// SimpleDateFormat format2=new SimpleDateFormat("MM/dd/yyyy"); 
			 SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd"); 
	         XSSFSheet sheet = workbook.getSheetAt(0);

	         
	         //Iterate through each rows one by one
	         int count=0;
	         Iterator<Row> rowIterator = sheet.iterator();
	         while (rowIterator.hasNext())
	            {
	        	 /* if(count==10)
	        	  {
	        		  break;
	        	  }
	        	  count++;*/
	        	 ArrayList<String> value=new ArrayList<>();
	        	 
	                Row row = rowIterator.next();
	                //For each row, iterate through all the columns
	                Iterator<Cell> cellIterator = row.cellIterator();
	                 int colCount=0;
	                while (cellIterator.hasNext())
	                {
	                    Cell cell = cellIterator.next();
	                    //Check the cell type and format accordingly
	                      
	                    switch (cell.getCellType())
	                    {
	                    case Cell.CELL_TYPE_NUMERIC:
	                    	if(!skip.contains(colCount))
	                    	{
	                    		if(HSSFDateUtil.isCellDateFormatted(cell))
	                    		{
	                    			if(cell.getDateCellValue().getYear()==-1)
	                    			{
	                    				//System.out.print(format.format(cell.getDateCellValue()) +" ");
	                    				value.add(format.format(cell.getDateCellValue()));
	                    			}
	                    			else
	                    			{
	                    				//System.out.print(cell.getDateCellValue() +" ");
	                    				value.add(format2.format(cell.getDateCellValue())+"");
	                    			}

	                    		}
	                    		else
	                    		{
	                    			//System.out.print(cell.getNumericCellValue() +" ");
	                    			value.add((cell.getNumericCellValue()+"").replace(".0",""));
	                    		}
	                    	}
	                    	break;
	                    case Cell.CELL_TYPE_STRING:
	                    	if(!skip.contains(colCount))
	                    	{
	                    		if(isHeader)
	                    		{
	                    			key.add(cell.getStringCellValue().replaceAll(" ","_"));
	                    		}
	                    		else
	                    		{
	                    			value.add(cell.getStringCellValue());
	                    		}

	                    	}

	                    	//System.out.print(cell.getStringCellValue()+" ");
	                    	break;

	                    }
	                    colCount++;
	                }

	                if(!isHeader)
	                {
	                	//System.out.println(value);
	                	writer.println(value.toString().replace("[","").replace("]",""));
	    				
	    				
	                	//stringJson.add(createStringJson(key, value));
	                }
	                isHeader=false;
	               // System.out.println("");
	            }
	           writer.close();
	            file.close();
	            workbook.close();
	            
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			return stringJson;
		}
		
	}
	public ArrayList<Integer> convertStringToArrayList(String s)
	{
		if(s.isEmpty())
		{
			System.err.println("Error : String is empty");
			return null;
		}
		ArrayList<Integer> arrList=new ArrayList<Integer>();
		if(s.contains(","))
		{
			String arr[]=s.split(",");
			for(String a: arr)
			{
				arrList.add(new Integer(a));
			}
		}
		else
		{
			for(int i=0;i<s.length();i++)
			{
				arrList.add(new Integer(""+s.charAt(i)));
			}
		}
		
		
		return arrList;
	}
	

}
