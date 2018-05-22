package entity;

import java.util.ArrayList;

import org.apache.http.impl.client.CloseableHttpClient;

public interface ISchool {
	//如果需要登陆才能获取课表的话，登陆然后返回一个已经保持了session的HttpClient对象
	public CloseableHttpClient login();
	//下载所有课表并保存到文件中
	public void downloadTimeTableAndSaveAsFile();
	//解析每个课表文件并将信息保存到数据库
	public void parseFileAndSaveToDatabase();
	//从数据库中读取数据并封装为ClassRoom对象
	public ArrayList<ClassRoom> getClassRoomFromDatabase();
	//根据参数查询得到无课教室
	public ArrayList<ClassRoom> search(ArrayList<ClassRoom> tts,String campus,String building,String crname,int[] jies,int day,int week);
}
