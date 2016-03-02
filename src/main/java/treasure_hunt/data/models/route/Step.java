package treasure_hunt.data.models.route;

import java.util.ArrayList;

/**
 * @author Mei Yii Lim
 *
 * This class holds the steps for a Route.
 */

public class Step {
	
	private Integer stepNo;
	private String name, task;
	private String treasure;
	private String solution;
	private ArrayList<Point> points;
	private ArrayList<Question> questions;
	
	public Step(){
		questions = new ArrayList<Question>();
		points = new ArrayList<Point>();
	}
	
	public void setStepNo(Integer i){
		this.stepNo = i;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setTask(String task){
		this.task = task;
	}
	
	public void setTreasure(String treasure) {
		this.treasure = treasure;
	}
	
	public void setSolution(String solution){
		this.solution = solution;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	
	public void setQuestions(ArrayList<Question> questions){
		this.questions = questions;
	}
		
	public ArrayList<Point> getPoints() {
		return points;
	}

	public ArrayList<Question> getQuestions(){
		return questions;
	}
	
	public Integer getStepNo(){
		return this.stepNo;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getTask(){
		return this.task;
	}
	
	public String getTreasure() {
		return treasure;
	}

	public String getSolution(){
		return this.solution;
	}
		
	public String toString(){
		return ("Step:" + this.stepNo + ", " + this.treasure + ", " + this.task + "\n" + "Points:" + this.points.toString() + "\n" + "Question:" + this.questions.toString());
	}
}
