package treasure_hunt.data.models.route;

import java.util.ArrayList;

/**
 * @author Mei Yii Lim
 *
 * This class holds the Question objects for each step.
 */

public class Question {

	private String question;
	private ArrayList<String> answers;
	private Boolean isImage;
	private String correctAnswer;
	
	public Question(){
		question = "";
		answers = new ArrayList<String>();
		isImage = false;
		correctAnswer = "";
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}
	
	public void setIsImage(Boolean isImage) {
		this.isImage = isImage;
	}
	
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public ArrayList<String> getAnswers() {
		return answers;
	}
	
	public Boolean getIsImage() {
		return isImage;
	}
	
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	
	public String toString() {
		return "question: " + question + ", answers: " + answers.toString() + ", correct answer: " + correctAnswer; 
	}
}
