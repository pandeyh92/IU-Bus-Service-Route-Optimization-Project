package com.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class PersistInDB {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		PersistInDB db=new PersistInDB();
	//	db.storeInMongo(null,"");
	}

	public void storeInMongo(ArrayList<String>jsons,String datBase,String collection)
	{
		MongoClient mongo=null;
		Long start=System.currentTimeMillis();
		try {
			 mongo = new MongoClient( "localhost" , 27017 );
		//	DB db = mongo.getDB("database name");
			/*List<String> dbs = mongo.getDatabaseNames();
			for(String db : dbs){
				System.out.println(db);
			}
			
			DB db = mongo.getDB("bus");
			Set<String> tables = db.getCollectionNames();
				System.out.println("Collection ----------------->");	
			for(String coll : tables){
				System.out.println(coll);
			}*/
			DB dbt = mongo.getDB(datBase);
			DBCollection table = dbt.getCollection(collection);
			
			/*System.out.println(table.getFullName() + " 1 "+table.getName()+" 2 "+table.find());
			
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("Name", "Anup");
			
			//DBCursor cursor = table.find();
*/
			
			
			
			
			//String json = "{\"votes\": {\"funny\": 0, \"useful\": 2, \"cool\": 1}, \"user_id\": \"Xqd0DzHaiyRqVH3WRG7hzg\", \"review_id\": \"15SdjuK7DmYqUAj6rjGowg\", \"stars\": 5, \"date\": \"2007-05-17\", \"text\": \"dr. goldberg offers everything i look for in a general practitioner.  he's nice and easy to talk to without being patronizing; he's always on time in seeing his patients; he's affiliated with a top-notch hospital (nyu) which my parents have explained to me is very important in case something happens and you need surgery; and you can get referrals to see specialists without having to see him first.  really, what more do you need?  i'm sitting here trying to think of any complaints i have about him, but i'm really drawing a blank.\", \"type\": \"review\", \"business_id\": \"vcNAWiLM4dR7D2nwwJ7nCA\"}";
            int count=0;
			for(String json:jsons)
			{
				count++;
				DBObject dbObject = (DBObject)JSON.parse(json);
				table.insert(dbObject);
				System.out.println(count);
			}
		
			
			Long end=System.currentTimeMillis();
			System.out.println("Time Taken To Insert in Ms "+(end-start));
			/*while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}*/
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			mongo.close();
		}
	}

	
}
