package entity;

public class Device {
	//设备ID
	private int id;
	//设备目前所在位置
	private LatLon latlon;
	//设备所监测到的心率
	private int heart;
	//APP要给设备定的吃药闹钟时间，共五位数，对应一天86400秒
	private String time;	
	//设备当前电量，目前有三个级别，分别是0，1，2，3
	private int dian;
	//目前APP有没有正在请求位置信息，具体用下面三个常量描述
	private int requestLatlon;
	private int requestHeart;
	private int requestDian;
	//APP是否需要给设备设置闹钟
	private boolean changeTime;
	//设备不需要执行任何指令
	public static final int REQUEST_NO=0;
	//设备开始执行某个指令
	public static final int REQUEST_START=1;
	//设备执行某个指令完毕
	public static final int REQUEST_END=2;
	public Device() {
	}
	public Device(LatLon latlon, int heart, String time) {
		super();
		this.latlon = latlon;
		this.heart = heart;
		this.time = time;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LatLon getLatlon() {
		return latlon;
	}
	public void setLatlon(LatLon latlon) {
		this.latlon = latlon;
	}
	public int getHeart() {
		return heart;
	}
	public void setHeart(int heart) {
		this.heart = heart;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getRequestLatlon() {
		return requestLatlon;
	}
	public void setRequestLatlon(int requestLatlon) {
		this.requestLatlon = requestLatlon;
	}
	public int getRequestHeart() {
		return requestHeart;
	}
	public void setRequestHeart(int requestHeart) {
		this.requestHeart = requestHeart;
	}
	public boolean isChangeTime() {
		return changeTime;
	}
	public void setChangeTime(boolean changeTime) {
		this.changeTime = changeTime;
	}
	public int getDian() {
		return dian;
	}
	public void setDian(int dian) {
		this.dian = dian;
	}
	public int getRequestDian() {
		return requestDian;
	}
	public void setRequestDian(int requestDian) {
		this.requestDian = requestDian;
	}
	@Override
	public String toString() {
		return "Device [id=" + id + ", latlon=" + latlon + ", heart=" + heart + ", time=" + time + ", dian=" + dian
				+ ", requestLatlon=" + requestLatlon + ", requestHeart=" + requestHeart + ", requestDian=" + requestDian
				+ ", changeTime=" + changeTime + "]";
	}	
}
