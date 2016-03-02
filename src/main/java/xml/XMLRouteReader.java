package xml;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.json.JSONException;

import treasure_hunt.data.models.route.HeartRate;
import treasure_hunt.data.models.route.Point;
import treasure_hunt.data.models.route.Question;
import treasure_hunt.data.models.route.Route;
import treasure_hunt.data.models.route.Step;


/**
 * @author Mei Yii Lim
 *
 * This class reads the route.xml files and return a list of Route objects.
 * 
 */

public class XMLRouteReader extends XMLReader{
	//private final static String STEP_FILE = "steps.xml"; 
	private ArrayList<Route> routes;
	
	public XMLRouteReader(InputStream isRoute) throws JSONException, MalformedURLException{
		try {
			Document stepDoc = parse(isRoute);
			routes = getRoutes(stepDoc);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Route> getRoutes(){
		return routes;
	}
	
	private ArrayList<Route> getRoutes(Document scriptDoc) throws DocumentException, JSONException {
		ArrayList<Route> routes = new ArrayList<Route>();
		Element root = scriptDoc.getRootElement();
		Integer in = 0;
		for (Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            if (element.getName().equals("route")){      
            	Route route = new Route();
                ArrayList<Step> steps = new ArrayList<Step>();
            	for (Iterator j = element.elementIterator(); j.hasNext();){            		
            		Element temp = (Element) j.next();
            		if (temp.getName().equals("name")){
            			route.setName(temp.getText());
            		} else if (temp.getName().equals("steps")){
            			for (Iterator k = temp.elementIterator(); k.hasNext();){
                    		Element e = (Element) k.next();
                    		Step step = getStepInfo(in, e);
                    		in++;
                    		steps.add(step);
                    		//System.out.println(step.toString());
            			}
            			route.setSteps(steps);
            		}      		            		
            	}
            	routes.add(route);
            }
           
        }
		return routes;
	}
	
	private Step getStepInfo(Integer index, Element e) {
		Step step =  new Step();
		Integer in = 0;
		ArrayList<Question> questions = new ArrayList<Question>();
		ArrayList<Point> points = new ArrayList<Point>();
		for (Iterator<Element> i = e.elementIterator(); i.hasNext(); ) {
            Element temp = (Element) i.next();
            step.setStepNo(index);
            if (temp.getName().equals("name")){
            	//System.out.println("Name: " + temp.getText());
            	step.setName(temp.getText());
            } else if (temp.getName().equals("text")){
            	//System.out.println("Text: " + temp.getText());
            	step.setTask(temp.getText());
            } else if (temp.getName().equals("treasure")){
            	//System.out.println("treasure: " + temp.getText());
            	step.setTreasure(temp.getText());
            } else if (temp.getName().equals("solution")){
            	//System.out.println("Gesture: " + temp.getText());
            	step.setSolution(temp.getText());
            } else if (temp.getName().equals("points")){   
            	for (Iterator<Element> k = temp.elementIterator(); k.hasNext(); ){
            		Element e2 = (Element) k.next();
            		Point point = getPointInfo(in, e2);
            		in++;
            		points.add(point);
            		System.out.println(point.toString());
    			}
            	step.setPoints(points);
            } else if (temp.getName().equals("question")){    
            	Question question = new Question();
            	for (Iterator<Element> j = temp.elementIterator(); j.hasNext();){            		
            		Element e2 = (Element) j.next();
            		if (e2.getName().equals("answer")){
            			for ( Iterator<Attribute> l = e2.attributeIterator(); l.hasNext(); ) {
            				Attribute attribute = (Attribute) l.next();
                            //System.out.println("Attribute:" + attribute.getName());
                            if (attribute.getName().equals("isImage")){
                            	//System.out.println("Is image?:" + attribute.getText());
                            	question.setIsImage(Boolean.parseBoolean(attribute.getText()));
                            }
            			}
                		ArrayList<String> answers = new ArrayList<String>();
                		for (Iterator<Element> j2 = e2.elementIterator(); j2.hasNext();){
                    		Element e3 = (Element) j2.next();
                    		//System.out.println("Answer:" + e3.getText());
                    		answers.add(e3.getText());
                    		for ( Iterator<Attribute> k = e3.attributeIterator(); k.hasNext(); ) {
                                Attribute attribute = (Attribute) k.next();
                                //System.out.println("Attribute:" + attribute.getName());
                                if (attribute.getName().equals("correct")){
                                	//System.out.println("Correct answer:" + e3.getText());
                                	question.setCorrectAnswer(e3.getText());
                                }
                    		}
                		}
                		question.setAnswers(answers);
            		} else {
            			if (e2.getName().equals("text")){
            				//System.out.println("Question:" + e2.getText());
            				question.setQuestion(e2.getText());
            			} 
            			/* name & gesture
            			else {
            				q.put(e2.getName(), e2.getText());
            			}*/
            		}                   		
            	}     
            	questions.add(question);
                //System.out.println("Questions:" + questions.toString());
            }
            step.setQuestions(questions);            
        }
		return step;
	}
	
	private Point getPointInfo(Integer index, Element e) {
		Point point =  new Point();
		for (Iterator<Element> i = e.elementIterator(); i.hasNext(); ) {
            Element temp = (Element) i.next();
            point.setPointNo(index);
            if (temp.getName().equals("name")){
            	//System.out.println("Name: " + temp.getText());
            	point.setName(temp.getText());
            } else if (temp.getName().equals("latitude")){
            	//System.out.println("Latitude: " + temp.getText());
            	point.setLatitude(Double.parseDouble(temp.getText()));
            } else if (temp.getName().equals("longitude")){
	            //System.out.println("Latitude: " + temp.getText());
	            point.setLongitude(Double.parseDouble(temp.getText()));
            } else if (temp.getName().equals("hr")){
	            //System.out.println("Latitude: " + temp.getText());
	            point.setHr(getHRInfo(temp));
            }
		}
		return point;
	}
	
	private HeartRate getHRInfo(Element e) {
		HeartRate hr = new HeartRate();
		for (Iterator<Element> i = e.elementIterator(); i.hasNext(); ) {
            Element temp = (Element) i.next();
            if (temp.getName().equals("mrange")){
            	System.out.println("mRange: " + temp.getText());
            	hr.setMRange(Float.parseFloat(temp.getText()));
            } else if (temp.getName().equals("srange")){
            	System.out.println("sRange: " + temp.getText());
            	hr.setSRange(Float.parseFloat(temp.getText()));
            }
		}
		return hr;
	}
}
