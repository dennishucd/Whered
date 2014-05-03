package cn.dennishucd.whered.db;

public class TLocation {
	
	private String timestamp;
	private int  locType; // 0:GPS, 1:Baidu, 2: Google
	private double latitude;
	private double longitude;
	private int uploaded; //0: unUploaded,  1: uploaed 
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public int getLocType() {
		return locType;
	}
	public void setLocType(int locType) {
		this.locType = locType;
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
	
	public int getUploaded() {
		return uploaded;
	}
	public void setUploaded(int uploaded) {
		this.uploaded = uploaded;
	}
}
