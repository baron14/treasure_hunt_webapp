package treasure_hunt_webapp.dao;


import java.util.ArrayList;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import treasure_hunt.service.pojo.Metric;
import org.bson.Document;
public class MetricDao {

	
	private static final String ROUTES_COLLECTION = "routes";
	private static final String DB_NAME = "heroku_jbmk6wxh";
	private static final String DB_USER = "admin";
	private static final String DB_PASSWORD = "admin";

	MongoClient mongo;
	MongoDatabase db;
	public MetricDao()  {
		try{
			System.out.println("yes");
			mongo = new MongoClient(new MongoClientURI("mongodb://"+ DB_USER + ":" + DB_PASSWORD
					+ "@ds047945.mongolab.com:47945/heroku_jbmk6wxh"));
			
			db = mongo.getDatabase(DB_NAME);
			System.out.println("yes");
			
		}
		catch(Exception e){
			System.out.println("nooooooooooo");
			e.printStackTrace();
		}
				

	}
	
	
	
	
	public void saveMetrics(Metric metric){
		db.getCollection("metric").insertOne(new Document().append("distance", metric.getDistance()));
	}
	
	
	public ArrayList<Metric> getMetrics(){
		 FindIterable<Document> a = db.getCollection("metric").find();
		 ArrayList<Metric> ar = new ArrayList<Metric>(); 
		 MongoCursor<Document> it = a.iterator();
		 while(it.hasNext()){
			 ar.add(new Metric(it.next().getLong("distance")));
		 }
		 return ar;
		 
		 //
	}
}
