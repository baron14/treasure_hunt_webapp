package treasure_hunt.data.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.bson.types.ObjectId;
import org.xml.sax.SAXException;

import com.googlecode.mjorm.MongoDao;
import com.googlecode.mjorm.MongoDaoImpl;
import com.googlecode.mjorm.XmlDescriptorObjectMapper;
import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import treasure_hunt.data.models.route.Route;

public class RouteDao {

	private static final String ROUTES_COLLECTION = "routes";
	private static final String DB_NAME = "heroku_jbmk6wxh";
	private static final String DB_USER = "admin";
	private static final String DB_PASSWORD = "admin";

	private MongoDao dao;
	private DB db;

	public RouteDao(File xmlFile) throws XPathExpressionException,
			ClassNotFoundException, IOException, ParserConfigurationException,
			SAXException {
		MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://"
				+ DB_USER + ":" + DB_PASSWORD
				+ "@ds047945.mongolab.com:47945/heroku_jbmk6wxh"));

		XmlDescriptorObjectMapper objectMapper = new XmlDescriptorObjectMapper();
		objectMapper.addXmlObjectDescriptor(xmlFile);

		db = mongo.getDB(DB_NAME);

		dao = new MongoDaoImpl(db, objectMapper);
	}

	public void create(Route route) {
		//Generated ID should be unique so remove should only remove old versions of same object
		dao.deleteObject("routes", route.getId()); 
		dao.createObject(ROUTES_COLLECTION, route);
	}

	public void delete(Route route) {
		dao.deleteObject(ROUTES_COLLECTION, route.getId());
	}
	
	public Route[] getRoutes(){
		List<ObjectId> ids = new ArrayList<ObjectId>();
		for(Object id : ((BasicDBList)db.getCollection("routes").distinct("_id"))){
			ids.add((ObjectId)id);
		}
	
		return dao.readObjects("routes", ids.toArray(), Route.class);
	}

}
