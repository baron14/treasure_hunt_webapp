package treasure_hunt.data.models.route;

import java.util.ArrayList;

import org.bson.types.ObjectId;

/**
 * @author Mei Yii Lim
 *
 * This is the Route class which contains a list of steps.
 */

public class Route {
	private ObjectId id;
	private String name;
	private ArrayList<Step> steps;
	
	public Route() {
		name = "";
		steps = new ArrayList<Step>();
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Step> getSteps() {
		return steps;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setSteps(ArrayList<Step> steps) {
		this.steps = steps;
	}

	@Override
	public String toString() {
		return "Route [name=" + name + ", steps=" + steps + "]";
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
}
