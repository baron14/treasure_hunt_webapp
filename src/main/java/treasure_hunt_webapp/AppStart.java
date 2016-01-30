package treasure_hunt_webapp;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import treasure_hunt_webapp.models.route.Route;
import xml.XMLRouteReader;

import com.googlecode.mjorm.MongoDao;
import com.googlecode.mjorm.MongoDaoImpl;
import com.googlecode.mjorm.XmlDescriptorObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@SpringBootApplication
public class AppStart {

	private static String dbuser = "admin";
	private static String dbpassword = "admin";

	public static void main(String[] args) {
/*		try {
			XMLRouteReader reader = new XMLRouteReader(new FileInputStream(
					"routes.xml"));
			Route route = reader.getRoutes().get(0);

			MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://"
					+ dbuser + ":" + dbpassword
					+ "@ds047945.mongolab.com:47945/heroku_jbmk6wxh"));

			XmlDescriptorObjectMapper objectMapper = new XmlDescriptorObjectMapper();
			objectMapper.addXmlObjectDescriptor(new File("routes.mjorm.xml"));

			DB db = mongo.getDB("heroku_jbmk6wxh"); // 10gen driver
	
			//System.out.println(mongo.getDatabaseNames());
			
			DBCollection coll = db.getCollection("test");
			coll.count();
			
			MongoDao dao = new MongoDaoImpl(db, objectMapper);

			route = dao.createObject("routes", route);
			System.out.println(route.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}*/

		SpringApplication.run(AppStart.class);
	}
}
