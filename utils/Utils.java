package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;

import com.google.gson.Gson;

import entity.Device;

public class Utils {
	//�ж�����Ӧ��Ĭ����һ�ڿ�
	public static int[] judgeJie(){
		Date date=new Date();
		SimpleDateFormat df = new SimpleDateFormat("HH");
		String str=df.format(date);
		int a=Integer.parseInt(str);
		if (a>=0&&a<12) {
			return new int[]{1,2,3,4};
		}else if (a>=12&&a<18) {
			return new int[]{5,6,7,8};
		}else {
			return new int[]{9,10,11};
		}
	}
	//�жϽ������ܼ�
	public static int getWeekOfDate() {
		Date date=new Date();
	    int[] weekOfDays = {7,1,2,3,4,5,6};        
	    Calendar calendar = Calendar.getInstance();      
	    if(date != null){        
	         calendar.setTime(date);      
	    }        
	    int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;      
	    if (w < 0){        
	        w = 0;      
	    }      
	    return weekOfDays[w];    
	}
	public static String intarrayToString(int[] intarr){
		StringBuilder sb=new StringBuilder();
		for (int i : intarr) {
			sb.append(i);
		}
		return sb.toString();
	}
	public static String getFileLocation(String key){
//		return "C:/Users/473574509/workspace2/daily/WebContent/temp.txt";
		return "/home/ych/apache-tomcat-8.0.51/webapps/daily/temp.txt";
	}
	public static Device readFromTemp(){
		File file=new File(getFileLocation("temp_location"));
		try {
			FileInputStream input=new FileInputStream(file);
			byte[] buffer=new byte[1024];
			int n=input.read(buffer);
			input.close();
			Gson gson=new Gson();
			if(n!=-1){				
				return gson.fromJson(new String(buffer,0,n),Device.class);
			}else{
				return new Device();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("δ�ҵ��ļ�");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("�ļ�����ʧ��");
		}
		return new Device();
	}
	public static Device readFromTomcat(){
		try {
			URL url=new URL("http://"+Constant.host+":8080/daily/locate?id=1");
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			InputStream in=conn.getInputStream();
			return new Gson().fromJson(inputToString(in), Device.class);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String readTimeFromTomcat(){
		try {
			URL url=new URL("http://"+Constant.host+":8080/daily/time?id=1");
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			InputStream in=conn.getInputStream();
			return inputToString(in);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static Device readDeviceFromDatabase(String deviceid){
		List<Map<String,String>> maps=DBUtil.query("select * from device where id=?", new String[]{deviceid}, new String[]{"id","message"});
		Device device=null;
		if(maps.size()>0){
			Map map=maps.get(0);
			System.out.println("read:"+map.get("message"));
			device=new Gson().fromJson((String) map.get("message"),Device.class);
		}
		return device;
	}
	public static String readDeviceJsonFromDatabase(String deviceid){
		List<Map<String,String>> maps=DBUtil.query("select * from device where id=?", new String[]{deviceid}, new String[]{"id","message"});
		if(maps.size()>0){
			Map map=maps.get(0);
			System.out.println("read:"+map.get("message"));
			return (String) map.get("message");
		}
		return "";
	}
	public static boolean outputToDatabase(String deviceid,String message){
		System.out.println("out:"+message);
		return DBUtil.excute("update device set message=? where id=?", new String[]{message,deviceid});
	}
	public static void outputToTemp(String str){
		File file=new File(getFileLocation("temp_location"));
		try {
			FileOutputStream output=new FileOutputStream(file);
			output.write(str.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("文件未找到");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("文件读取或写入错误");
		}
	}
	public static String readJson(){
		File file=new File(getFileLocation("temp_location"));
		try {
			FileInputStream input=new FileInputStream(file);
			byte[] buffer=new byte[1024];
			int len=input.read(buffer);
			return new String(buffer,0,len);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("δ�ҵ��ļ�");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("�ļ�����ʧ��");
		}
		return null;
	}
	public static String entityAsString(HttpEntity entity,String encode){
		byte[] buffer=new byte[10240];
		int len=0;
		StringBuffer sb=new StringBuffer();
		try {
			while((len=entity.getContent().read(buffer))!=-1){
				sb.append(new String(buffer,0,len));
			}
			return sb.toString();
		} catch (UnsupportedOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String fileToString(String path,String encode){
		File file=new File(path);
		try {
			FileInputStream input=new FileInputStream(file);
			StringBuilder sb=new StringBuilder();
			byte[] buffer=new byte[4096];
			int len=0;
			while((len=input.read(buffer))!=-1){
				sb.append(new String(buffer,0,len,encode));
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			System.out.println("�ļ�δ�ҵ������������ļ�·��");
		} catch (IOException e) {
			System.out.println("�ļ���ȡ����");
			e.printStackTrace();
		}
		return null;
	}
	public static void save(String locate,InputStream input){
		try{
			FileOutputStream fout=new FileOutputStream(new File(locate));
			byte[] buffer=new byte[4096];
			int len=0;
			while((len=input.read(buffer))!=-1){
				if(len!=-1){
					fout.write(buffer, 0, len);
				}
			}
			System.out.println("д���ļ���"+locate+"���");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static int[] strToIntArray(String str){
		if(str==null){
			return new int[]{};
		}
		String[] ss=str.split(",");
		int[] ints=new int[ss.length];
		for (int i=0;i<ss.length;i++) {
			ints[i]=Integer.parseInt(ss[i]);
		}
		return ints;
	}
	//智能转换1kb
	public static String inputToString(InputStream in){
		StringBuilder sb=new StringBuilder();
		int len=0;
		byte[] buffer=new byte[1024];
		try {
			len=in.read(buffer);
			sb.append(new String(buffer,0,len));
			System.out.println(sb.toString());
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String parseTime(long millis){
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		int time=(calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE))*60+calendar.get(Calendar.SECOND);
		int weishu=5;
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<weishu-getWeishu(time);i++){
			sb.append("0");
		}
		return sb.append(String.valueOf(time)).toString();
	}
	public static int getWeishu(int a){
		int b=1;
		while((a=a/10)!=0){
			b++;
		}
		return b;
	}
}
