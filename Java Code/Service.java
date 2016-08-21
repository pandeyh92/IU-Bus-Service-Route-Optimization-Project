package com.service;

import com.dao.PersistInDB;
import com.util.GenUtil;
import com.util.JsonObjectCreator;

public class Service {
	public static String pathTSV="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\intervaldata2014-2015.tsv";
	private static String folderPath="C:\\Users\\Anup\\Desktop\\DM\\Bus Data\\Spring 2015\\";
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*JsonObjectCreator dao=new JsonObjectCreator();
		dao.insertIntoTable(pathTSV, "99", "bus", "intervaldata2014", "tsv");
		*/
		System.out.println("Inserting into Db.......");
		GenUtil genUtilObj=new GenUtil();
		PersistInDB dbObj=new PersistInDB();
		dbObj.storeInMongo(genUtilObj.readContentOfFolder(folderPath), "bus", "route");
	}

}
