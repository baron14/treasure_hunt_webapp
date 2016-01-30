package treasure_hunt_webapp.dao;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import treasure_hunt_webapp.models.route.Route;

import com.googlecode.mjorm.MongoDao;
import com.googlecode.mjorm.MongoDaoImpl;
import com.googlecode.mjorm.XmlDescriptorObjectMapper;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class RouteDao {

	private static final String ROUTES_COLLECTION = "routes";
	private static final String DB_NAME = "heroku_jbmk6wxh";
	private static final String DB_USER = "admin";
	private static final String DB_PASSWORD = "admin";

	private MongoDao dao;

	public RouteDao(File xmlFile) throws XPathExpressionException,
			ClassNotFoundException, IOException, ParserConfigurationException,
			SAXException {
		MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://"
				+ DB_USER + ":" + DB_PASSWORD
				+ "@ds047945.mongolab.com:47945/heroku_jbmk6wxh"));

		XmlDescriptorObjectMapper objectMapper = new XmlDescriptorObjectMapper();
		objectMapper.addXmlObjectDescriptor(xmlFile);

		DB db = mongo.getDB(DB_NAME);

		dao = new MongoDaoImpl(db, objectMapper);
	}

	public void create(Route route) {
		dao.createObject(ROUTES_COLLECTION, route);
	}

	public void delete(Route route) {
		dao.deleteObject(ROUTES_COLLECTION, route.getId());
	}

}
