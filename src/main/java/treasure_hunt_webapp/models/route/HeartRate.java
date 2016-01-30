package treasure_hunt_webapp.models.route;

public class HeartRate {
	private float mRange;
	private float sRange;
	
	public HeartRate() {
		setMRange(0.0f);
		setSRange(0.0f);
	}

	public float getMRange() {
		return mRange;
	}

	public void setMRange(float mRange) {
		this.mRange = mRange;
	}
	
	public String toString() {
		return "mRange: " + mRange + " sRange: " + sRange; 
	}

	public float getSRange() {
		return sRange;
	}

	public void setSRange(float sRange) {
		this.sRange = sRange;
	}
}
