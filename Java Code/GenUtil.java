package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GenUtil {

	public static String testPath="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\Spring 2015\\test2.txt";
	private  String routeIDPath="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\routeID.txt";
	private  String weatherData="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\weatherData.txt";
	public String routeJson="\"Route\":[";
	private String dateFormat="yyyy-MM-dd";
	private String weatherDateFormat="MM/dd/yyyy";
	private String folderPath="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\Spring 2015\\";
	private String outPutFolderPath="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\Spring 2015\\";
	private String routMapPath="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\RouteMapper.txt";
	private String startPointsPath="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\startPoints.txt";
	private String pathOfBusFolder="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\";
	private HashMap<String,String>weatherEventMapper=new HashMap<>();
	private static int totalRouteCount=0;
	private  static HashMap<String,String> routeIDMapper=new HashMap<>();
	private  static HashMap<String,String> percipitationMapper=new HashMap<>();
	private  static HashMap<String,HashMap<String,String>> scheduleNameMapper;
	private static HashMap<String,ArrayList<String>> weatherMap=new HashMap<>();
	private static HashMap<String,ArrayList<String>> startPointMapper=new HashMap<>();
	private static HashMap<String,String> routeMap=new HashMap<>();
	private static String testRouteId="372";
	private int timelimit=120;
	public static void main(String[] args) {
		GenUtil util=new GenUtil();
		/*ArrayList<String>content=util.readContentOfFolder(util.folderPath);
		System.out.println("Total Number Of Route "+content.size());
		 */	//util.populateWeatherData("C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\"+"WeatherData\\","2015");
		//util.executeRouteCalculator();
		//util.populateWeatherEventMapper(util.pathOfBusFolder,"weatherEvent.txt");
		//util.reFormatWeatherEvent(util.pathOfBusFolder,"weatherData.txt");
		//util.populateWeatherData();
		/*	util.populateRouteMapper();
		util.populateWeatherData();
		util.populateRouteMap(util.routMapPath);
		util.populateStartPointMapper(util.startPointsPath);
		util.buildRoute2(testPath, util.outPutFolderPath);*/

		util.executeRouteCalculator2();
	}

	public void populateRouteMap(String path)
	{
		BufferedReader br=null;
		String sCurrentLine=null; 
		try
		{
			br = new BufferedReader(new FileReader(path));
			while ((sCurrentLine = br.readLine()) != null)
			{
				String arr[]=sCurrentLine.split(" ");
				routeMap.put(arr[0], arr[1]);
			}
			br.close();
		}catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}


	public void executeRouteCalculator()
	{
		GenUtil util=new GenUtil();
		//System.out.println(util.readFolder("C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\Spring 2015\\372",".tsv"));
		//System.out.println(util.convStringToTime("13:30:36"));
		util.populateRouteMapper();
		util.populateWeatherData();
		util.populateRouteMap(routMapPath);
		//System.out.println(weatherMap.toString());
		ArrayList<String> folder=util.readFolder(util.folderPath, null, true);
		for(String fold:folder)
		{
			String inPutPath=util.folderPath+fold;
			String outPutPath=util.outPutFolderPath+fold+"\\output\\";
			new File(outPutPath).mkdir();
			util.buildRoute(inPutPath,outPutPath);
		}
		System.out.println("Total Number of Route Identified "+totalRouteCount);
	}
	public void executeRouteCalculator2()
	{
		GenUtil util=new GenUtil();
		
		//System.out.println(util.readFolder("C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\Spring 2015\\372",".tsv"));
		//System.out.println(util.convStringToTime("13:30:36"));
		ReadSchedule readScheduleobj=new ReadSchedule();
		scheduleNameMapper=readScheduleobj.readShiftTable();
		util.populateRouteMapper();
		util.populateWeatherData();
		util.populateRouteMap(routMapPath);
		util.populateStartPointMapper(util.startPointsPath);
		//System.out.println(weatherMap.toString());
		ArrayList<String> folder=util.readFolder(util.folderPath, null, true);
		for(String fold:folder)
		{
			String inPutPath=util.folderPath+fold;
			String outPutPath=util.outPutFolderPath+fold+"\\output\\";
			new File(outPutPath).mkdir();
			util.buildRoute2(inPutPath,outPutPath,fold);
		}
		System.out.println("Total Number of Route Identified "+totalRouteCount);
	}

	public ArrayList<String> readFolder(String folderPath,String typeOfFile,boolean isFolder)
	{
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> fileName=new ArrayList<>();
		if(!isFolder)
		{
			for (int i = 0; i < listOfFiles.length; i++) 
			{
				if (listOfFiles[i].isFile()&& listOfFiles[i].getName().endsWith(typeOfFile)) 
				{
					fileName.add(listOfFiles[i].getName());
				}
			}
		}
		else
		{
			for (int i = 0; i < listOfFiles.length; i++) 
			{
				if (listOfFiles[i].isDirectory()) 
				{
					fileName.add(listOfFiles[i].getName());
				}
			}
		}
		return fileName;
	}

	public void buildRoute(String folderPath,String outPutPath)
	{
		//ArrayList<String> files=readFolder(folderPath);
		BufferedReader br = null;
		try {

			ArrayList<String> fileNames=readFolder(folderPath,".tsv",false);
			ArrayList<String> reportList=new ArrayList<>();
			for(String file:fileNames)
			{

				ArrayList<String> routesToWrite=new ArrayList<String>();
				//System.out.println("Processing routeID "+file);
				String tempPath=folderPath+"\\"+file;
				String sCurrentLine;
				ArrayList<String> tuples=new ArrayList<>();
				br = new BufferedReader(new FileReader(tempPath));

				while ((sCurrentLine = br.readLine()) != null)
				{
					tuples.add(sCurrentLine);
				}
				int length=tuples.size();
				long time=0;
				//System.out.println("Run Length "+findRunLength(tuples,5,"38","38","2015-01-11"));
				boolean isStartFound=false;
				int prevRunLength=99999999;
				int rewindPos=-1;
				String start="";
				String prevTime="0";
				String prevStart="";

				StringBuilder sb=new StringBuilder(routeJson);
				HashMap<String,String> jsonMap=new HashMap<>();
				StringBuilder travelSequence=new StringBuilder();
				int tripLengthCount=0;
				int loadCount=0;
				int travelCount=0;
				for(int i=0;i<length-1;i++)
				{
					String arr[]=tuples.get(i).split("\t");
					String dateAndTime[]=arr[6].split(" ");
					String from=arr[1];
					String to=arr[2];

					if(!canPropagate(tuples.get(i), tuples.get(i+1)))
					{
						if(isStartFound)
						{

							if(canPropagate( tuples.get(i-1),tuples.get(i)))
							{
								if(arr[1].equals(arr[2]))
								{
									formRouteJson(sb,GenUtil.routeIDMapper.get(arr[1]) , GenUtil.routeIDMapper.get(arr[2]), arr[3], true, dateAndTime[1]);
									travelSequence.append(GenUtil.routeIDMapper.get(arr[1])+"--->");
									tripLengthCount++;

								}
								else
								{
									formRouteJson(sb, GenUtil.routeIDMapper.get(arr[1]) , GenUtil.routeIDMapper.get(arr[2]), ""+(new Integer(arr[3])-new Integer(prevTime)), false, dateAndTime[1]);
									travelSequence.append(GenUtil.routeIDMapper.get(arr[1])+"--->");
									travelSequence.append(GenUtil.routeIDMapper.get(arr[2])+"--->");
									tripLengthCount++;
									tripLengthCount++;
								}

							}
							//System.out.println(processJson(sb));

							jsonMap.put("EndTime", dateAndTime[1]);
							jsonMap.put("RoutID",arr[5]);
							jsonMap.put("BusID",arr[4]);

							jsonMap.put("TotalStops",""+tripLengthCount);
							jsonMap.put("TotalTravelTime",""+travelCount);
							jsonMap.put("TotalLoadTime", ""+loadCount);
							processSb(travelSequence,"--->","");
							jsonMap.put("TravelSequence",travelSequence.toString());
							//formMetaRouteJson(jsonMap).append(processJson(sb));
							//System.out.println(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
							//routesToWrite.add(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
							//writeInToFile(outPutFolderPath,file , formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
							routesToWrite.add(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
							//writeCount++;
						}
						prevTime="0";
						prevStart="";
						prevRunLength=99999999;
						rewindPos=-1;
						isStartFound=false;
						tripLengthCount=0;
						loadCount=0;
						travelCount=0;
						travelSequence.delete(0, travelSequence.length());
						sb.delete(0,sb.length());
						sb.append(routeJson);
						jsonMap.clear();
						//System.out.println("=============== Cannot Prop =============================");
						continue;
					}
					if(i==0)
					{
						time=new Long(arr[3]);
						time=time/60;

						if(time>30)
						{
							//possibleStart=arr[2];
							continue;
						}

					}
					else
					{

						if(!isStartFound)
						{
							if(!from.equals(to))
							{
								continue;
							}
							else
							{
								int run=findRunLength(tuples,i,from,to,dateAndTime[0]);
								if(run-prevRunLength==0 || run==-2)
								{
									isStartFound=true;
									String startTime="";
									String date="";
									String rName="";
									if(rewindPos==-1)
									{
										start=tuples.get(i).split("\t")[2];
										startTime=tuples.get(i).split("\t")[6].split(" ")[1];
										date=tuples.get(i).split("\t")[6].split(" ")[0];
										rewindPos=i;
										rName=tuples.get(i).split("\t")[5];
										prevStart=start;
										i=i-1;
									}
									else
									{
										start=tuples.get(rewindPos).split("\t")[2];
										startTime=tuples.get(rewindPos).split("\t")[6].split(" ")[1];
										date=tuples.get(rewindPos).split("\t")[6].split(" ")[0];
										rName=tuples.get(rewindPos).split("\t")[5];
										i=rewindPos-1;
										prevStart=start;
									}
									jsonMap.put("StartPoint",GenUtil.routeIDMapper.get(start));
									jsonMap.put("StartTime", startTime);
									jsonMap.put("WeekDay", getDayOfWeek(date));
									jsonMap.put("RouteName",routeMap.get(rName));
									jsonMap.put("Date", date);
									ArrayList<String> weatherInfo=getWeatherInfo(date);
									jsonMap.put("Temperature",weatherInfo.get(0));
									jsonMap.put("WeatherEvent",weatherInfo.get(1));


								}else if(run==-1 && prevStart.equals(to) && from.equals(to))
								{
									isStartFound=true;
									String temp[]=tuples.get(i).split("\t");
									jsonMap.put("StartPoint",GenUtil.routeIDMapper.get(temp[1]));
									jsonMap.put("RouteName",routeMap.get(temp[5]));
									String tempTimestamp[]=temp[6].split(" ");
									jsonMap.put("StartTime", tempTimestamp[1]);
									jsonMap.put("WeekDay", getDayOfWeek(tempTimestamp[0]));
									jsonMap.put("Date", tempTimestamp[0]);
									ArrayList<String> weatherInfo=getWeatherInfo(tempTimestamp[0]);
									jsonMap.put("Temperature",weatherInfo.get(0));
									jsonMap.put("WeatherEvent",weatherInfo.get(1));
									i=i-1;
								}

								if(run >=0 && run<prevRunLength)
								{
									prevRunLength=run;
									rewindPos=i;
								}
							}
						}
						else
						{

							//System.out.println("From "+from);
							//System.out.println("To "+to);
							boolean isLoad=false;
							String loadOrTravelTime="";
							if(from.equals(to))
							{
								//	System.out.println("load Time "+arr[3]);

								prevTime=arr[3];
								isLoad=true;
								loadOrTravelTime=arr[3];
								loadCount=loadCount+(new Integer(loadOrTravelTime));

							}
							else
							{
								//System.out.println("Travel Time "+(new Integer(arr[3])-new Integer(prevTime)));

								loadOrTravelTime=""+(new Integer(arr[3])-new Integer(prevTime));
								travelSequence.append(GenUtil.routeIDMapper.get(from)+"--->");
								tripLengthCount++;
								travelCount=travelCount+(new Integer(loadOrTravelTime));
							}
							//System.out.println("When"+dateAndTime[1]);

							formRouteJson(sb, GenUtil.routeIDMapper.get(from), GenUtil.routeIDMapper.get(to), loadOrTravelTime, isLoad, dateAndTime[1]);

							if(rewindPos!=i && to.equals(start))
							{

								prevRunLength=99999999;
								rewindPos=-1;
								isStartFound=false;
								prevTime="0";
								start="";
								//System.out.println(processJson(sb));

								jsonMap.put("EndTime", dateAndTime[1]);
								jsonMap.put("RoutID",arr[5]);
								jsonMap.put("BusID",arr[4]);
								jsonMap.put("TotalStops",""+tripLengthCount);
								jsonMap.put("TotalTravelTime",""+travelCount);
								jsonMap.put("TotalLoadTime", ""+loadCount);
								processSb(travelSequence,"--->","");
								jsonMap.put("TravelSequence",travelSequence.toString());
								//System.out.println(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());

								//writeInToFile(outPutFolderPath,file , formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
								routesToWrite.add(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
								//writeCount++;
								sb.delete(0,sb.length());
								sb.append(routeJson);

								tripLengthCount=0;
								loadCount=0;
								travelCount=0;
								travelSequence.delete(0, travelSequence.length());
								jsonMap.clear();
								//System.out.println("===========================================");
							}

							if(i==length-2)
							{
								String arr2[]=tuples.get(i+1).split("\t");
								String dateAndTime2[]=arr2[6].split(" ");

								String loadOrTravelTime2="";
								boolean isLoad2=false;

								//System.out.println("From "+arr2[1]);
								//System.out.println("To "+arr2[2]);
								if(arr2[1].equals(arr2[2]))
								{
									//	System.out.println("load Time "+arr2[3]);
									//prevTime=arr[3];
									loadOrTravelTime2=arr2[3];
									isLoad2=true;
									loadCount=loadCount+(new Integer(loadOrTravelTime2));
									tripLengthCount++;
									travelSequence.append(GenUtil.routeIDMapper.get(arr2[1])+"--->");

								}
								else
								{
									//System.out.println("Travel Time "+(new Integer(arr2[3])-new Integer(prevTime)));
									loadOrTravelTime2=""+(new Integer(arr2[3])-new Integer(prevTime));
									travelSequence.append(GenUtil.routeIDMapper.get(arr2[1])+"--->");
									if(!start.equals(arr2[2]))
									{
										travelSequence.append(GenUtil.routeIDMapper.get(arr2[2])+"--->");
										tripLengthCount++;
									}

									tripLengthCount++;
									travelCount=travelCount+(new Integer(loadOrTravelTime2));
								}
								//System.out.println("Time Of occurance "+dateAndTime2[1]);

								formRouteJson(sb, GenUtil.routeIDMapper.get(arr2[1]), GenUtil.routeIDMapper.get(arr2[2]), loadOrTravelTime2, isLoad2, dateAndTime2[1]);
								jsonMap.put("EndTime", dateAndTime2[1]);
								jsonMap.put("RoutID",arr2[5]);
								jsonMap.put("BusID",arr2[4]);
								jsonMap.put("TotalStops",""+tripLengthCount);
								jsonMap.put("TotalTravelTime",""+travelCount);
								jsonMap.put("TotalLoadTime", ""+loadCount);
								processSb(travelSequence,"--->","");
								jsonMap.put("TravelSequence",travelSequence.toString());
								//System.out.println(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
								//writeInToFile(outPutFolderPath,file , formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
								routesToWrite.add(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
								//writeCount++;
								prevTime="0";
								prevStart="";
								prevRunLength=99999999;
								rewindPos=-1;
								isStartFound=false;
								tripLengthCount=0;
								loadCount=0;
								travelCount=0;
								travelSequence.delete(0, travelSequence.length());
								sb.delete(0,sb.length());
								sb.append(routeJson);
								jsonMap.clear();
								/*if(arr2[2].equals(start))
							{
								count=0;
								prevRunLength=99999999;
								rewindPos=-1;
								isStartFound=false;
								prevTime="0";
								start="";
							}*/
							}
						}

					}

				}

				writeInToFile(outPutPath, file.replace(".tsv",""), routesToWrite);
				totalRouteCount=totalRouteCount+routesToWrite.size();
				//System.out.println("Number of routes identified for Bus ID "+file+" is "+routesToWrite.size());
				reportList.add("Number of routes identified for Bus ID "+file+" is "+routesToWrite.size());

				//System.out.println("last ----------->"+processJson(sb));
			}
			writeInToFile(outPutPath, "Report", reportList);

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	public String convStringToDate(String date)
	{
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		String formatedDate=null;
		try {
			formatedDate= format.format(format.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatedDate;

	}

	public String convStringToTime(String date)
	{
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		String formatedDate=null;
		try {
			formatedDate= format.format(format.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatedDate;
	}
	/*
	 * pos is tuple number for which from and to are same , 
	 * From and To are stop. They should be same
	 * Date is date
	 * return -1 if date changes or cannot propagate
	 * -2 if end of file is reached
	 * 
	 */
	public int findRunLength(ArrayList<String> tuple,int pos,String from,String to,String date)
	{
		int len=tuple.size();
		int count=0;

		for(;pos<len;pos++)
		{
			String arr[]=tuple.get(pos).split("\t");
			String dateTime[]=arr[6].split(" ");
			if(dateTime[0].equals(date) && arr[1].equals(to))
			{
				to=arr[2];
				count++;

				if(count!=1 && arr[2].equals(from))
				{
					return count;
				}

			}

			else
			{
				return -1;
			}
		}
		return -2;
	}

	public boolean canPropagate(String curr,String next)
	{
		String arrCur[]=curr.split("\t");
		String arrNext[]=next.split("\t");
		String currDate=arrCur[6].split(" ")[0];
		String nextDate=arrNext[6].split(" ")[0];
		if(!currDate.equals(nextDate))
		{
			return false;
		}
		if(arrCur[2].equals(arrNext[1]))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public StringBuilder processJson(StringBuilder sb)
	{
		int lIndex=sb.lastIndexOf(",");
		sb.replace(lIndex, lIndex+1, "");
		sb.append("]}");
		//sb.insert(0, "{");
		return sb;
	}

	public StringBuilder processSb(StringBuilder sb,String replace,String with)
	{
		int lIndex=sb.lastIndexOf(replace);
		try
		{
			if(sb.length()<replace.length())
			{
				return sb;
			}
			sb.replace(lIndex, lIndex+replace.length(), with);
		}catch(StringIndexOutOfBoundsException ex)
		{
			System.out.println("last Index "+lIndex);
			System.out.println(sb.toString()+" Replace "+replace);
			System.exit(-1);
		}
		//sb.insert(0, "{");
		return sb; 
	}
	public StringBuilder formRouteJson(StringBuilder sb,String from,String to,String time,boolean isLoad,String when)
	{
		sb.append("{"+"\"From\":\""+from+"\",");
		sb.append("\"To\":\""+to+"\",");
		if(isLoad)
		{
			sb.append("\"LoadTime\":\""+time+"\",");	
		}
		else
		{

			sb.append("\"Travel_Time\":\""+time+"\",");
		}
		sb.append("\"When\":\""+when+"\"},");
		return sb;
	}

	public String getDay(int day)
	{
		if(day==0)
		{
			return "Sunday";
		}else if(day==1)
		{
			return "Monday";
		}
		else if(day==2)
		{
			return "Tuesday";
		}
		else if(day==3)
		{
			return "Wednesday";
		}
		else if(day==4)
		{
			return "Thursday";
		}
		else if(day==5)
		{
			return "Friday";
		}
		else
		{
			return "Saturday";
		}

	}

	public String getDayOfWeek(String date)
	{
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Date dateTemp=null;
		try {
			dateTemp = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getDay(dateTemp.getDay());
	}

	public StringBuilder formMetaRouteJson(HashMap<String,String> metadata)
	{
		StringBuilder sb=new StringBuilder("{");

		for(String key:metadata.keySet())
		{
			if(!key.equals("WeatherEvent"))
			{
				sb.append("\""+key+"\":"+"\""+metadata.get(key)+"\",");
			}
			else
			{
				sb.append("\""+key+"\":["+metadata.get(key)+"],");
			}
		}

		return sb;
	}

	public void populateRouteMapper()
	{
		BufferedReader br = null;


		String sCurrentLine;

		try {
			br = new BufferedReader(new FileReader(routeIDPath));
			while ((sCurrentLine = br.readLine()) != null)
			{
				String keyValue[]=sCurrentLine.split(":");
				routeIDMapper.put(keyValue[0].trim(),keyValue[1].trim());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void populateWeatherData()
	{
		BufferedReader br = null;


		String sCurrentLine;

		try {
			br = new BufferedReader(new FileReader(weatherData));
			while ((sCurrentLine = br.readLine()) != null)
			{
				String keyValue[]=sCurrentLine.split("\t");
				String key=changeDateFormat(weatherDateFormat, dateFormat, keyValue[0]);
				ArrayList<String> weatherFactor=new ArrayList<>();
				weatherFactor.add(keyValue[1].trim());
				if(keyValue.length<4)
				{
					weatherFactor.add("Normal");
				}
				else
				{
					weatherFactor.add(keyValue[3].trim());
				}
				percipitationMapper.put(key, keyValue[2]);
				weatherMap.put(key, weatherFactor);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String changeDateFormat(String from,String to,String date)
	{
		SimpleDateFormat format = new SimpleDateFormat(from);
		SimpleDateFormat format2=new SimpleDateFormat(to);
		String newDate=null;
		try {
			newDate= format2.format(format.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return newDate;
	}
	public ArrayList<String> getWeatherInfo(String date)
	{

		ArrayList<String> weather=new ArrayList<>();
		if(weatherMap.containsKey(date))
		{
			weather.add(weatherMap.get(date).get(0));
			weather.add(weatherMap.get(date).get(1));

		}
		else
		{
			weather.add("-NA-");
			weather.add("-NA-");
		}
		return weather;
		//process date to convert into a format that can be passed to NOA API to get weather
	}

	public void writeInToFile(String folderPath,String fileName,ArrayList<String> content)
	{
		try {

			String path=folderPath+fileName+".txt";
			PrintWriter writer = new PrintWriter(path, "UTF-8");
			for(String json:content)
			{
				writer.println(json);
			}

			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<String> readContentOfFolder(String folderPath)
	{
		ArrayList<String> folderName=readFolder(folderPath, null, true);
		ArrayList<String> content= new ArrayList<>();

		BufferedReader br = null;
		try
		{
			for(String fName:folderName)
			{
				String fileName=folderPath+fName+"\\output\\";
				ArrayList<String>  files=readFolder(fileName,".txt", false);
				for(String f:files)
				{
					if(!f.contains("Report"))
					{
						String name=fileName+f;
						br = new BufferedReader(new FileReader(name));
						String sCurrentLine=null;
						while ((sCurrentLine = br.readLine()) != null)
						{
							content.add(sCurrentLine);
						}
					}
				}
				br.close();
			}
		}catch(IOException ex)
		{
			ex.printStackTrace();
		}

		return content;
	}

	public String monthMapper(String month)
	{
		if(month.equals("Aug"))
		{
			return "8";
		}else if(month.equals("July"))
		{
			return "7";
		}
		else if(month.equals("June"))
		{
			return "6";
		}
		else if(month.equals("Nov"))
		{
			return "11";
		}
		else if(month.equals("Oct"))
		{
			return "10";
		}
		if(month.equals("Sep"))
		{
			return "9";
		}
		if(month.equals("May"))
		{
			return "5";
		}
		return "";

	}

	public void populateWeatherData(String path,String year)
	{
		BufferedReader br=null;
		ArrayList<String> files= readFolder(path,".csv",false);
		ArrayList<String> tuples=new ArrayList<>();
		for(String fileName:files)
		{
			String filePath=path+fileName;
			try
			{
				br = new BufferedReader(new FileReader(filePath));
				String sCurrentLine=null;

				while ((sCurrentLine = br.readLine()) != null)
				{
					String arr[]=sCurrentLine.split(",");
					StringBuilder sb=new StringBuilder();
					String date=monthMapper(fileName.replace(".csv",""))+"/"+arr[0]+"/"+year;
					if(arr.length==3)
					{
						sb.append(date);
						sb.append("\t");
						sb.append(arr[1]);
						sb.append("\t");
						sb.append(arr[2]);
						sb.append("\t");
						sb.append("-NA-");
					}
					else
					{
						sb.append(date);
						sb.append("\t");
						sb.append(arr[1]);
						sb.append("\t");
						sb.append(arr[2]);
						sb.append("\t");
						sb.append(arr[3]);
					}
					tuples.add(sb.toString());
				}
				br.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		writeInToFile(path,"WeatherReport.txt",tuples);

	}

	public void populateWeatherEventMapper(String path,String fName)
	{
		String fPath=path+fName;
		BufferedReader br=null;
		try
		{
			br = new BufferedReader(new FileReader(fPath));
			String sCurrentLine=null;

			while ((sCurrentLine = br.readLine()) != null)
			{
				String arr[]=sCurrentLine.split("=");
				weatherEventMapper.put(arr[0], arr[1]);
			}
			br.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}

	}

	public void reFormatWeatherEvent(String path,String fName)
	{
		BufferedReader br=null;
		String filePath=path+fName;
		ArrayList<String> tuples=new ArrayList<>();
		try
		{
			br = new BufferedReader(new FileReader(filePath));
			String sCurrentLine=null;

			while ((sCurrentLine = br.readLine()) != null)
			{
				StringBuilder sb=new StringBuilder();
				String arr[]=sCurrentLine.split("\t");
				sb.append(arr[0]);
				sb.append("\t");
				sb.append(arr[1]);
				sb.append("\t");
				sb.append(arr[2]);
				sb.append("\t");
				if(arr.length==3)
				{
					sb.append("\"-NA-\"");
				}else
				{

					if(!arr[3].equals("-NA-"))
					{
						try{
							Integer.parseInt(arr[3]);
							for(int i=0;i<arr[3].length();i++)
							{
								sb.append("\""+weatherEventMapper.get(""+arr[3].charAt(i))+"\"");
								sb.append(",");
							}

							// is an integer!
						} catch (NumberFormatException e) {
							// not an integer!
							String eventArray[]=arr[3].split("-");

							for(String s:eventArray)
							{
								sb.append("\""+s+"\"");
								sb.append(",");
							}
						}
						int index=sb.toString().lastIndexOf(',');
						sb.replace(index, index+1,"");
					}
					else
					{
						sb.append("\"-NA-\"");
					}
				}
				tuples.add(sb.toString());

			}
			br.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}

		writeInToFile(path, fName, tuples);
	}

	public void buildRoute2(String folderPath,String outPutPath,String rID)
	{

		System.out.println("Calculating route .......");
		
		BufferedReader br = null;
		try {

			ArrayList<String> fileNames=readFolder(folderPath,".tsv",false);
			ArrayList<String> reportList=new ArrayList<>();
			for(String file:fileNames)
			{

				ArrayList<String> startPoint=startPointMapper.get(routeMap.get(rID));
				//String startPoint=startPointMapper.get(routeMap.get(testRouteId));
				ArrayList<String> routesToWrite=new ArrayList<String>();
				//System.out.println("Processing routeID "+file);
				String tempPath=folderPath+"\\"+file;
				String sCurrentLine;
				ArrayList<String> tuples=new ArrayList<>();
				br = new BufferedReader(new FileReader(tempPath));
				//br = new BufferedReader(new FileReader(folderPath));

				while ((sCurrentLine = br.readLine()) != null)
				{
					tuples.add(sCurrentLine);
				}
				int length=tuples.size();
				long time=0;
				//System.out.println("Run Length "+findRunLength(tuples,5,"38","38","2015-01-11"));
				boolean isStartFound=false;
				int prevRunLength=99999999;
				int rewindPos=-1;
				String start="";
				String prevTime="0";
				String prevStart="";

				StringBuilder sb=new StringBuilder(routeJson);
				HashMap<String,String> jsonMap=new HashMap<>();
				StringBuilder travelSequence=new StringBuilder();
				int tripLengthCount=0;
				int loadCount=0;
				int travelCount=0;
				for(int i=0;i<length-1;i++)
				{
					String arr[]=tuples.get(i).split("\t");
					String dateAndTime[]=arr[6].split(" ");
					String from=arr[1];
					String to=arr[2];

					if(!canPropagate(tuples.get(i), tuples.get(i+1)))
					{
						if(isStartFound)
						{

							if(canPropagate( tuples.get(i-1),tuples.get(i)))
							{
								if(arr[1].equals(arr[2]))
								{
									formRouteJson(sb,GenUtil.routeIDMapper.get(arr[1]) , GenUtil.routeIDMapper.get(arr[2]), arr[3], true, dateAndTime[1]);
									travelSequence.append(GenUtil.routeIDMapper.get(arr[1])+"--->");
									tripLengthCount++;

								}
								else
								{
									formRouteJson(sb, GenUtil.routeIDMapper.get(arr[1]) , GenUtil.routeIDMapper.get(arr[2]), ""+(new Integer(arr[3])-new Integer(prevTime)), false, dateAndTime[1]);
									travelSequence.append(GenUtil.routeIDMapper.get(arr[1])+"--->");
									travelSequence.append(GenUtil.routeIDMapper.get(arr[2])+"--->");
									tripLengthCount++;
									tripLengthCount++;
								}

							}
							//System.out.println(processJson(sb));

							jsonMap.put("EndTime", dateAndTime[1]);
							jsonMap.put("RoutID",arr[5]);
							jsonMap.put("BusID",arr[4]);

							jsonMap.put("TotalStops",""+tripLengthCount);
							jsonMap.put("TotalTravelTime",""+travelCount);
							jsonMap.put("TotalLoadTime", ""+loadCount);
							processSb(travelSequence,"--->","");
							jsonMap.put("TravelSequence",travelSequence.toString());
							//formMetaRouteJson(jsonMap).append(processJson(sb));
							//System.out.println(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
							//routesToWrite.add(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
							//writeInToFile(outPutFolderPath,file , formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
							routesToWrite.add(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
							//writeCount++;
						}
						prevTime="0";
						prevStart="";
						prevRunLength=99999999;
						rewindPos=-1;
						isStartFound=false;
						tripLengthCount=0;
						loadCount=0;
						travelCount=0;
						travelSequence.delete(0, travelSequence.length());
						sb.delete(0,sb.length());
						sb.append(routeJson);
						jsonMap.clear();
						//System.out.println("=============== Cannot Prop =============================");
						continue;
					}
					/*if(i==0 || dateAndTime[0].equals(tuples.get(i-1).split("\t")[6].split(" ")[0]))
					{
					 */	time=new Long(arr[3]);
					 time=time/60;

					 if(time>timelimit)
					 {
						 //possibleStart=arr[2];
						 if(isStartFound)
							{

								if(canPropagate( tuples.get(i-1),tuples.get(i)))
								{
									if(arr[1].equals(arr[2]))
									{
										formRouteJson(sb,GenUtil.routeIDMapper.get(arr[1]) , GenUtil.routeIDMapper.get(arr[2]), arr[3], true, dateAndTime[1]);
										travelSequence.append(GenUtil.routeIDMapper.get(arr[1])+"--->");
										tripLengthCount++;

									}
									else
									{
										formRouteJson(sb, GenUtil.routeIDMapper.get(arr[1]) , GenUtil.routeIDMapper.get(arr[2]), ""+(new Integer(arr[3])-new Integer(prevTime)), false, dateAndTime[1]);
										travelSequence.append(GenUtil.routeIDMapper.get(arr[1])+"--->");
										travelSequence.append(GenUtil.routeIDMapper.get(arr[2])+"--->");
										tripLengthCount++;
										tripLengthCount++;
									}

								}
								//System.out.println(processJson(sb));

								jsonMap.put("EndTime", dateAndTime[1]);
								jsonMap.put("RoutID",arr[5]);
								jsonMap.put("BusID",arr[4]);

								jsonMap.put("TotalStops",""+tripLengthCount);
								jsonMap.put("TotalTravelTime",""+travelCount);
								jsonMap.put("TotalLoadTime", ""+loadCount);
								processSb(travelSequence,"--->","");
								jsonMap.put("TravelSequence",travelSequence.toString());
								//formMetaRouteJson(jsonMap).append(processJson(sb));
								//System.out.println(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
								//routesToWrite.add(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
								//writeInToFile(outPutFolderPath,file , formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
								routesToWrite.add(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
								//writeCount++;
							}
						 prevTime="0";
						 prevStart="";
						 prevRunLength=99999999;
						 rewindPos=-1;
						 isStartFound=false;
						 tripLengthCount=0;
						 loadCount=0;
						 travelCount=0;
						 travelSequence.delete(0, travelSequence.length());
						 sb.delete(0,sb.length());
						 sb.append(routeJson);
						 jsonMap.clear();
						 continue;
					 }
					 else
					 {

						 if(!isStartFound)
						 {
							 /*if(!from.equals(to)||!from.equals(startPoint))
							 {*/
								 /*Code changed to handle situation when multiple start points are there*/
								 boolean isContinue=true;
								 for(String st:startPoint)
								 {
									 if(from.equals(st))
									 {
										 isContinue=false;
										 break;
									 }
								 }
								 if(isContinue || !from.equals(to))
								 {
									 continue;
								 }

							// }
							/* else
							 {*/
								 int run=findRunLength(tuples,i,from,to,dateAndTime[0]);
								 if(run>0 || run==-2)
								 {
									 isStartFound=true;
									 String startTime="";
									 String date="";
									 String rName="";
									 if(rewindPos==-1)
									 {
										 start=tuples.get(i).split("\t")[2];
										 startTime=tuples.get(i).split("\t")[6].split(" ")[1];
										 date=tuples.get(i).split("\t")[6].split(" ")[0];
										 rewindPos=i;
										 rName=tuples.get(i).split("\t")[5];
										 String name=findScheduleID(date, routeMap.get(rName), tuples.get(i).split("\t")[4]);
										 jsonMap.put("ScheduleName",name);
										 prevStart=start;
										 i=i-1;
									 }
									 else
									 {
										 start=tuples.get(rewindPos).split("\t")[2];
										 startTime=tuples.get(rewindPos).split("\t")[6].split(" ")[1];
										 date=tuples.get(rewindPos).split("\t")[6].split(" ")[0];
										 rName=tuples.get(rewindPos).split("\t")[5];
										 i=rewindPos-1;
										 prevStart=start;
										 String name=findScheduleID(date, routeMap.get(rName), tuples.get(rewindPos).split("\t")[4]);
										 jsonMap.put("ScheduleName",name);
										 
									 }
									 jsonMap.put("StartPoint",GenUtil.routeIDMapper.get(start));
									 jsonMap.put("StartTime", startTime);
									 jsonMap.put("WeekDay", getDayOfWeek(date));
									 jsonMap.put("RouteName",routeMap.get(rName));
									 jsonMap.put("Date", date);
									
									 jsonMap.put("Percipitation",percipitationMapper.get(date));
									 ArrayList<String> weatherInfo=getWeatherInfo(date);
									 jsonMap.put("Temperature",weatherInfo.get(0));
									 jsonMap.put("WeatherEvent",weatherInfo.get(1));


								 }/*run==-1 if there is change of date*/else if(run==-1 /*&& prevStart.equals(to)*/ && from.equals(to))
								 {
									 isStartFound=true;
									 String temp[]=tuples.get(i).split("\t");
									 jsonMap.put("StartPoint",GenUtil.routeIDMapper.get(temp[1]));
									 jsonMap.put("RouteName",routeMap.get(temp[5]));
									 String tempTimestamp[]=temp[6].split(" ");
									 jsonMap.put("StartTime", tempTimestamp[1]);
									 jsonMap.put("WeekDay", getDayOfWeek(tempTimestamp[0]));
									 jsonMap.put("Date", tempTimestamp[0]);
									 ArrayList<String> weatherInfo=getWeatherInfo(tempTimestamp[0]);
									 jsonMap.put("Temperature",weatherInfo.get(0));
									 jsonMap.put("WeatherEvent",weatherInfo.get(1));
									 
									 String name=findScheduleID(tempTimestamp[0], routeMap.get(temp[5]), tuples.get(i).split("\t")[4]);
									 jsonMap.put("ScheduleName",name);
									 jsonMap.put("Percipitation",percipitationMapper.get(tempTimestamp[0]));
									 i=i-1;
								 }

								 /*if(run >=0 && run<prevRunLength)
								{
									prevRunLength=run;
									rewindPos=i;
								}*/
							// }
						 }
						 else
						 {

							 //System.out.println("From "+from);
							 //System.out.println("To "+to);
							 boolean isLoad=false;
							 String loadOrTravelTime="";
							 if(from.equals(to))
							 {
								 //	System.out.println("load Time "+arr[3]);

								 prevTime=arr[3];
								 isLoad=true;
								 loadOrTravelTime=arr[3];
								 loadCount=loadCount+(new Integer(loadOrTravelTime));

							 }
							 else
							 {
								 //System.out.println("Travel Time "+(new Integer(arr[3])-new Integer(prevTime)));

								 loadOrTravelTime=""+(new Integer(arr[3])-new Integer(prevTime));
								 travelSequence.append(GenUtil.routeIDMapper.get(from)+"--->");
								 tripLengthCount++;
								 travelCount=travelCount+(new Integer(loadOrTravelTime));
							 }
							 //System.out.println("When"+dateAndTime[1]);

							 formRouteJson(sb, GenUtil.routeIDMapper.get(from), GenUtil.routeIDMapper.get(to), loadOrTravelTime, isLoad, dateAndTime[1]);
                             //Checking if end point of route is reached 
							 if(rewindPos!=i && to.equals(start))
							 {

								 prevRunLength=99999999;
								 rewindPos=-1;
								 isStartFound=false;
								 prevTime="0";
								 start="";
								 //System.out.println(processJson(sb));

								 jsonMap.put("EndTime", dateAndTime[1]);
								 jsonMap.put("RoutID",arr[5]);
								 jsonMap.put("BusID",arr[4]);
								 jsonMap.put("TotalStops",""+tripLengthCount);
								 jsonMap.put("TotalTravelTime",""+travelCount);
								 jsonMap.put("TotalLoadTime", ""+loadCount);
								 processSb(travelSequence,"--->","");
								 jsonMap.put("TravelSequence",travelSequence.toString());
								 //System.out.println(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());

								 //writeInToFile(outPutFolderPath,file , formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
								 routesToWrite.add(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
								 //writeCount++;
								 prevTime="0";
								 prevStart="";
								 prevRunLength=99999999;
								 rewindPos=-1;
								 isStartFound=false;
								 tripLengthCount=0;
								 loadCount=0;
								 travelCount=0;
								 travelSequence.delete(0, travelSequence.length());
								 sb.delete(0,sb.length());
								 sb.append(routeJson);
								 jsonMap.clear();
								 continue;
								 //System.out.println("===========================================");
							 }

							 if(i==length-2)
							 {
								 String arr2[]=tuples.get(i+1).split("\t");
								 String dateAndTime2[]=arr2[6].split(" ");

								 String loadOrTravelTime2="";
								 boolean isLoad2=false;

								 //System.out.println("From "+arr2[1]);
								 //System.out.println("To "+arr2[2]);
								 if(arr2[1].equals(arr2[2]))
								 {
									 //	System.out.println("load Time "+arr2[3]);
									 //prevTime=arr[3];
									 loadOrTravelTime2=arr2[3];
									 isLoad2=true;
									 loadCount=loadCount+(new Integer(loadOrTravelTime2));
									 tripLengthCount++;
									 travelSequence.append(GenUtil.routeIDMapper.get(arr2[1])+"--->");

								 }
								 else
								 {
									 //System.out.println("Travel Time "+(new Integer(arr2[3])-new Integer(prevTime)));
									 loadOrTravelTime2=""+(new Integer(arr2[3])-new Integer(prevTime));
									 travelSequence.append(GenUtil.routeIDMapper.get(arr2[1])+"--->");
									 if(!start.equals(arr2[2]))
									 {
										 travelSequence.append(GenUtil.routeIDMapper.get(arr2[2])+"--->");
										 tripLengthCount++;
									 }

									 tripLengthCount++;
									 travelCount=travelCount+(new Integer(loadOrTravelTime2));
								 }
								 //System.out.println("Time Of occurance "+dateAndTime2[1]);

								 formRouteJson(sb, GenUtil.routeIDMapper.get(arr2[1]), GenUtil.routeIDMapper.get(arr2[2]), loadOrTravelTime2, isLoad2, dateAndTime2[1]);
								 jsonMap.put("EndTime", dateAndTime2[1]);
								 jsonMap.put("RoutID",arr2[5]);
								 jsonMap.put("BusID",arr2[4]);
								 jsonMap.put("TotalStops",""+tripLengthCount);
								 jsonMap.put("TotalTravelTime",""+travelCount);
								 jsonMap.put("TotalLoadTime", ""+loadCount);
								 processSb(travelSequence,"--->","");
								 jsonMap.put("TravelSequence",travelSequence.toString());
								 //System.out.println(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
								 //writeInToFile(outPutFolderPath,file , formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
								 routesToWrite.add(formMetaRouteJson(jsonMap).append(processJson(sb)).toString());
								 //writeCount++;
								 prevTime="0";
								 prevStart="";
								 prevRunLength=99999999;
								 rewindPos=-1;
								 isStartFound=false;
								 tripLengthCount=0;
								 loadCount=0;
								 travelCount=0;
								 travelSequence.delete(0, travelSequence.length());
								 sb.delete(0,sb.length());
								 sb.append(routeJson);
								 jsonMap.clear();
								 /*if(arr2[2].equals(start))
							{
								count=0;
								prevRunLength=99999999;
								rewindPos=-1;
								isStartFound=false;
								prevTime="0";
								start="";
							}*/
							 }
						 }

					 }

				}

				writeInToFile(outPutPath, file.replace(".tsv",""), routesToWrite);
				totalRouteCount=totalRouteCount+routesToWrite.size();
				//System.out.println("Number of routes identified for Bus ID "+file+" is "+routesToWrite.size());
				reportList.add("Number of routes identified for Bus ID "+file+" is "+routesToWrite.size());
				//writeInToFile(outPutPath, "testOutput", routesToWrite);
				//System.out.println("last ----------->"+processJson(sb));
			}
			writeInToFile(outPutPath, "Report", reportList);

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	public void populateStartPointMapper(String path)
	{
		BufferedReader br=null;
		String sCurrentLine=null; 
		try
		{
			br = new BufferedReader(new FileReader(path));
			while ((sCurrentLine = br.readLine()) != null)
			{
				ArrayList<String> tempArray=new ArrayList<>();
				String arr[]=sCurrentLine.split(":");
				if(arr[1].contains(","))
				{
					String tempArr[]=arr[1].split(",");
					for(String s:tempArr)
					{
						tempArray.add(s);
					}
				}
				else
				{
					tempArray.add(arr[1]);
				}
				startPointMapper.put(arr[0], tempArray);
			}
			br.close();
		}catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public String findScheduleID(String date,String routeName,String busId)
	{
		if(scheduleNameMapper.containsKey(date))
		{
			HashMap<String,String> hm=scheduleNameMapper.get(date);
			
			if(hm.containsKey(busId))
			{
				
				if(scheduleNameMapper.get(date).get(busId).contains(routeName))
				{
					return scheduleNameMapper.get(date).get(busId);
				}
			}
		}
		
		return "";
	}

}


