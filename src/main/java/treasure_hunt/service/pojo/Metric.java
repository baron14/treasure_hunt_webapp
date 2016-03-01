package treasure_hunt.service.pojo;

import java.io.Serializable;

public class Metric implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1282149782089584692L;
	
	private Long distance;
	
	
	public Metric(){
		super();
	}
	
	public Metric(Long distance) {
		super();
		this.distance = distance;
	}

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}
	


}
