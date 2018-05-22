package entity;

public class LatLon{
	private double lat,lon;
	public LatLon(String lat,String lon) {
		this.lat=Double.valueOf(lat);
		this.lon=Double.valueOf(lon);
	}
	public LatLon(double lat,double lon) {
		this.lat=lat;
		this.lon=lon;
	}
	public static LatLon standard(String slat,String slon){
		double lat=Double.parseDouble(slat.substring(0,2));// γ��-��
        lat+=Double.parseDouble(slat.substring(2))/60;// γ��-��
        double lon=Double.parseDouble(slon.substring(0,3));// ����-��
        lon+=Double.parseDouble(slon.substring(3))/60;// ����-��
        return new LatLon(lat, lon);
	}
	
	@Override
	public String toString() {
		return "LatLon [lat=" + lat + ", lon=" + lon + "]";
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	
}