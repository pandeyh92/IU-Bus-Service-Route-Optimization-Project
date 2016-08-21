package com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dao.PersistInDB;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.WorkbookFactory;
public class JsonObjectCreator {

	public static String pathExcel="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\Spring 2015\\qry_Trips_A.xlsx";
	public static String pathTSV="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\intervaldata2014-2015.tsv";
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JsonObjectCreator ob=new JsonObjectCreator();
		//ob.readExcelFile("","6");
		//ob.readTSVFile(-1,"0");
		
	}

	/*public String createJsonObjectFromExcel()
	{
		FileInputStream inp = new FileInputStream( "" );
		Workbook workbook = WorkbookFactory.create( inp );

		// Get the first Sheet.
		Sheet sheet = workbook.getSheetAt( 0 );

		    // Start constructing JSON.
		    JSONObject json = new JSONObject();

		    // Iterate through the rows.
		    JSONArray rows = new JSONArray();
		    for ( Iterator<Row> rowsIT = sheet.rowIterator(); rowsIT.hasNext(); )
		    {
		        Row row = rowsIT.next();
		        JSONObject jRow = new JSONObject();

		        // Iterate through the cells.
		        JSONArray cells = new JSONArray();
		        for ( Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext(); )
		        {
		            Cell cell = cellsIT.next();
		            cells.put( cell.getStringCellValue() );
		        }
		        jRow.put( "cell", cells );
		        rows.put( jRow );
		    }

		    // Create the JSON.
		    json.put( "rows", rows );

		// Get the JSON text.
		return json.toString();
	}*/
	
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
			 
	         //Get first/desired sheet from the workbook
			 SimpleDateFormat format=new SimpleDateFormat("h:mm a"); 
			 SimpleDateFormat format2=new SimpleDateFormat("MM/dd/yyyy"); 
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
	                    			value.add(cell.getNumericCellValue()+"");
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

	                    	System.out.print(cell.getStringCellValue()+" ");
	                    	break;

	                    }
	                    colCount++;
	                }

	                if(!isHeader)
	                {
	                	stringJson.add(createStringJson(key, value));
	                }
	                isHeader=false;
	               // System.out.println("");
	            }
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
	
	
	public ArrayList<String> readTSVFile(int datePosition,String colToSkip,String path)
	{
		TsvParserSettings settings = new TsvParserSettings();
	    //the file used in the example uses '\n' as the line separator sequence.
	    //the line separator sequence is defined here to ensure systems such as MacOS and Windows
	    //are able to process this file correctly (MacOS uses '\r'; and Windows uses '\r\n').
		Long start=System.currentTimeMillis();
	    settings.getFormat().setLineSeparator("\n");
	    ArrayList<Integer>skip=convertStringToArrayList(colToSkip);
	    ArrayList<String> key=new ArrayList<>();
	    
	    ArrayList<String> stringJson=new ArrayList<>();
	    //file = new FileInputStream(new File("C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\Spring 2015\\qry_Trips_A.xlsx"));
	    // creates a TSV parser
	    TsvParser parser = new TsvParser(settings);
	    try {
			parser.beginParsing(new FileReader(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	    String []row;
	    boolean isheader=true;
	    while ((row = parser.parseNext()) != null) 
	    {
	       // System.out.println(Arrays.toString(row));
	        ArrayList<String> value=new ArrayList<>();
	        int size=row.length;
	        for(int i=0;i<size;i++)
	        {
	        	if(!skip.contains(i))
	        	{
	        		if(isheader)
	        		{
	        			key.add(row[i].replaceAll(" ","_"));
	        		}
	        		else
	        		{
	        			value.add(row[i]);
	        		}
	        	}
	        }
	      
	        if(!isheader)
	        {
	        	stringJson.add(createStringJson(key, value));
	        }
	        isheader=false;
	    }
	    
	    System.out.println("Size "+stringJson.size());
	    
	    System.out.println("last json "+stringJson.get(stringJson.size()-1));
	    Long end=System.currentTimeMillis();
	    System.out.println("Toatal Time Taken In MS "+(end-start));
	    return stringJson;

	}
	
	public void insertIntoTable(String path,String columnToSkip,String dbName,String collection,String fileType)
	{
		ArrayList<String> jsons=null;
		if(fileType.equalsIgnoreCase("tsv"))
		{
			jsons=readTSVFile(-1, columnToSkip, path);
		}
		else if(fileType.equalsIgnoreCase("excel"))
		{
			jsons=readExcelFile(path, columnToSkip);
		}
		
		PersistInDB dao=new PersistInDB();
		dao.storeInMongo(jsons, dbName, collection);
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
	
	public String createStringJson(ArrayList<String>label,ArrayList<String>value)
	{
		if(label.size()!=value.size())
		{
			System.err.println("Cannot convert");
			return null;
		}
		StringBuilder sb=new StringBuilder();
		sb.append("{ ");
		int j=0;
		for(String lab:label)
		{
			sb.append('"'+lab+'"'+":"+'"'+value.get(j)+'"'+" ,");
			j++;
		}
		
		int comma=sb.lastIndexOf(",");
		sb.deleteCharAt(comma);
		sb.append(" }");
		return sb.toString();
		
		
	}
	
}
