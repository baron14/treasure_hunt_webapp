package treasure_hunt.data.models.route;

public class Point {
	private int pointNo;
	private String name;
	private double latitude;
	private double longitude;
	private HeartRate hr;

	public Point() {
		name = "";
		latitude = 0.0d;
		longitude = 0.0d;
	}

	public int getPointNo() {
		return pointNo;
	}

	public void setPointNo(int pointNo) {
		this.pointNo = pointNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public HeartRate getHr() {
		return hr;
	}

	public void setHr(HeartRate hr) {
		this.hr = hr;
	}

	public String toString() {
		return "name: " + name + ", latitude: " + String.valueOf(latitude) + ", longitude: " + String.valueOf(longitude) + ", hr: " + hr.toString();
	}
}
