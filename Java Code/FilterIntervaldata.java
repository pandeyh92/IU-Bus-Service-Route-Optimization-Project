package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class FilterIntervaldata {

	public String path="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\2015Data.tsv";
	public static String writePath="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\Spring 2015\\";
	public String readPath="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\Spring 2015\\";
	public String writePathBusID="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\Spring 2015\\";
	public  HashMap<String,ArrayList<String>> intervalData = new HashMap<>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FilterIntervaldata obj=new FilterIntervaldata();
		ArrayList<String> files=obj.readFolder("C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\Spring 2015\\",".tsv");
		for(String name:files)
		{
			String folderPath=writePath+name.replace(".tsv", "");
			if(obj.createFolder(folderPath))
			{
				String filePath=obj.readPath+name;
				ArrayList<String> rid=obj.findRouteId(4,filePath);
				obj.writeIntoFile(rid,folderPath,".tsv",false);
				ArrayList<String> reportData=new ArrayList<>();
				reportData.add("Size "+rid.size());
				
				reportData.add(rid.toString());
				
				int sum=0;
				for(String key:obj.intervalData.keySet())
				{
					//System.out.println();
					reportData.add("Key "+key+" Size "+obj.intervalData.get(key).size());
					sum=sum+obj.intervalData.get(key).size();
				}
				//System.out.println("total Sum "+sum);
				reportData.add("total Sum "+sum);
				obj.writeIntoFile(reportData,folderPath,".txt",true);
			}
			
			obj.intervalData.clear();
		}
	//	ArrayList<String> rid=obj.findRouteId(4,obj.readPath);
		

		
	}
	
	public ArrayList<String> findRouteId(int colNo,String path)
	{
		BufferedReader br = null;
		ArrayList<String>stpId=new ArrayList<>();

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(path));

			while ((sCurrentLine = br.readLine()) != null) {

              String arr[]=sCurrentLine.split("\t");
              String key=arr[colNo];
              if(!stpId.contains(key))
                  stpId.add(key);
              if(arr[colNo].equals("NULL"))
            	  System.out.println(sCurrentLine);
              
              if(intervalData.containsKey(key))
              {
            	  ArrayList<String> tuple=intervalData.get(key);
            	  tuple.add(sCurrentLine);
              }
              else
              {
            	  ArrayList<String> newTuple=new ArrayList<>();
            	  newTuple.add(sCurrentLine);
            	  intervalData.put(key, newTuple);
              }
              
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return stpId;

	}
	
	public void writeIntoFile(ArrayList<String> dataToWrite,String writePath,String fileType,boolean isReport)
	{
		
		if(intervalData.size()==0)
		{
			System.err.println("Cannot write into file . Please read interval.tsv first");
			return ;
		}
		try {
			if(!isReport)
			{
				for(String rID:intervalData.keySet())
				{
					String path=writePath+"\\"+rID+fileType;
					/*File f=new File(path);
					f.createNewFile();*/
					PrintWriter writer = new PrintWriter(path, "UTF-8");

					for(String line:intervalData.get(rID))
					{
						writer.println(line);
					}
					writer.close();
					System.out.println("File "+rID+".tsv got created");
				}
			}
			else
			{
				String path=writePath+"\\"+"Report"+fileType;
				PrintWriter writer=null;
				writer = new PrintWriter(path, "UTF-8");
				for(String data:dataToWrite)
				{
				   
					writer.println(data);
					
				}
				writer.close();
				System.out.println("Report  got created");
			}
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> readFolder(String folderPath,String typeOfFile)
	{
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> fileName=new ArrayList<>();
		for (int i = 0; i < listOfFiles.length; i++) 
		{
			if (listOfFiles[i].isFile()&& listOfFiles[i].getName().endsWith(typeOfFile)) 
			{
				fileName.add(listOfFiles[i].getName());
			}
		}
		return fileName;
	}
	
	public boolean createFolder(String path)
	{
		return  new File(path).mkdir();
	}

}
