package entity;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import utils.DBUtil;
import utils.Utils;

public class QfnuSchool implements ISchool{

	@Override
	public CloseableHttpClient login(){
		try{
			CloseableHttpClient httpclient = HttpClients.createDefault();
			//获取cookie（httpClient自带功能）
			HttpGet httpGet = new HttpGet("http://ids.qfnu.edu.cn/authserver/login?service=http%3A%2F%2Fmy.qfnu.edu.cn%2Findex.portal");
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			response1.getEntity().consumeContent();
			//执行post登陆
			HttpPost httpPost = new HttpPost("http://ids.qfnu.edu.cn/authserver/login?service=http%3A%2F%2Fmy.qfnu.edu.cn%2Findex.portal");
			UrlEncodedFormEntity logingEntity = new UrlEncodedFormEntity(parseAndInputLoginPage(Utils.entityAsString(response1.getEntity(),"utf-8")), Consts.UTF_8);
			httpPost.setEntity(logingEntity);
			CloseableHttpResponse response2=httpclient.execute(httpPost);
			httpGet = new HttpGet("http://ids.qfnu.edu.cn/authserver/login?service=http%3A%2F%2Fmy.qfnu.edu.cn%2Findex.portal");
			response2.getEntity().consumeContent();
			//		//登陆成功
			CloseableHttpResponse response3=httpclient.execute(httpGet);
			httpGet = new HttpGet("http://202.194.188.19/caslogin.jsp");
			response3.getEntity().consumeContent();
			CloseableHttpResponse response4 = httpclient.execute(httpGet);
			response4.getEntity().consumeContent();
			//		//请求课表主页面，编码gbk
			httpGet = new HttpGet("http://202.194.188.19/jskbcxAction.do?oper=jskb_lb");
			CloseableHttpResponse response5 = httpclient.execute(httpGet);
			response5.getEntity().consumeContent();
			return httpclient;
		}catch (Exception e) {
			System.out.println("登陆出错");
			e.printStackTrace();
			return null;
		}
	}

	//解析html得到并添加登陆参数
	public List<NameValuePair> parseAndInputLoginPage(String html){
		Document doc=Jsoup.parse(html);
		String lt=doc.select("#casLoginForm > input[type='hidden']:nth-child(4)").attr("value");
		String execution=doc.select("#casLoginForm > input[type='hidden']:nth-child(5)").attr("value");
		String _eventId=doc.select("#casLoginForm > input[type='hidden']:nth-child(6)").attr("value");
		String rmShown=doc.select("#casLoginForm > input[type='hidden']:nth-child(7)").attr("value");

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("username", "2015413912"));
		formparams.add(new BasicNameValuePair("password", "051510"));
		formparams.add(new BasicNameValuePair("lt", lt));
		formparams.add(new BasicNameValuePair("execution", execution));
		formparams.add(new BasicNameValuePair("_eventId", _eventId));
		formparams.add(new BasicNameValuePair("rmShown", rmShown));
		formparams.add(new BasicNameValuePair("submit", "登陆"));
		return formparams;
	}

	@Override
	public void downloadTimeTableAndSaveAsFile() {
		String baseUrl="http://202.194.188.19";
		//缓存所有课表页面
		ClassRoom[] tts=ClassRoom.getAll("C:/Users/473574509/PycharmProjects/untitled/test/test.html");
		CloseableHttpClient httpClient=login();
		HttpGet get=null;
		for(int i=0;i<204;i++){
			ClassRoom tt=tts[i];
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(
					tt.getId()+"_"+
							tt.getCampus()+"_"+
							tt.getBuilding()+"_"+
							tt.getCrid()+"_"+
							tt.getCrname()+"_"+
							tt.getSetsnum()+"_"+
							tt.getFaculty()+"_"+
							tt.getRoomtype()+"_"+
							baseUrl+tt.getTtinfourl());
			get=new HttpGet(baseUrl+tt.getTtinfourl().replaceAll(" ", URLEncoder.encode(" ")));
			CloseableHttpResponse response;
			try {
				response = httpClient.execute(get);
				Utils.save("C:\\Users\\473574509\\Documents\\timetable\\"+
						tt.getId()+"_"+
						tt.getCampus()+"_"+
						tt.getBuilding()+"_"+
						tt.getCrid()+"_"+
						tt.getCrname()+"_"+
						tt.getSetsnum()+"_"+
						tt.getFaculty()+"_"+
						tt.getRoomtype()+"_"+".html",response.getEntity().getContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//解析一个教室的课表文件，返回一个ClassRoom对象
	public static ClassRoom parseTT(String dir,String fileName){
		String html=Utils.fileToString(dir+fileName, "gbk");
		Document doc=Jsoup.parse(html);
		Pattern tpattern=Pattern.compile("\\d{1,2}-\\d{1,2}");
		//匹配每一节课
		Pattern cpattern=Pattern.compile("[^| ].*?\\(.*?,.*?,.*?\\)");
		ArrayList<ArrayList<String>> classes=new ArrayList<ArrayList<String>>();
		//迭代所有行
		for(int i=3;i<=15;i++){
			Element tr=doc.select("#user > thead > tr:nth-child("+i+")").get(0);
			Elements tds=tr.children();
			ArrayList<String> days=new ArrayList<String>();
			//t为1-7天
			if(tds.size()>1){
				//迭代一行的所有格子
				for(int j=tds.size()-7;j<tds.size();j++){
					Element td=tds.get(j);
					Matcher cmatcher=cpattern.matcher(td.text());
					StringBuilder sb=new StringBuilder();
					//只要找到有课即循环
					while(cmatcher.find()){
						Matcher tmatcher=tpattern.matcher(cmatcher.group());
						while(tmatcher.find()){
							if(!cmatcher.group().contains("虚拟课")){
								sb.append(tmatcher.group()+",");
							}
						}
					}
					days.add(sb.toString());
				}
				classes.add(days);
			}
		}
		ClassRoom tt=new ClassRoom(fileName);
		tt.parseAndSetClass(classes);
		return tt;
	}

	@Override
	public void parseFileAndSaveToDatabase() {
		File dir=new File("C:\\Users\\473574509\\Documents\\timetable\\");
		if(dir.isDirectory()){
			String[] filenames=dir.list();
			for (String filename : filenames) {
				ClassRoom tt=parseTT("C:\\Users\\473574509\\Documents\\timetable\\", filename);
//				for(int r=0;r<11;r++){
//					System.out.print((r+1)+"\t");
//					for(int c=0;c<7;c++){
//						System.out.print(Utils.intarrayToString(tt.getClass_()[r][c])+"\t");
//					}
//					System.out.println();
//				}
				System.out.println(tt.getId());
				DBUtil.excute("insert into timetable(id,campus,building,crid,crname,setsnum,faculty,roomtype,classes) values(?,?,?,?,?,?,?,?,?)", new String[]{
						tt.getId(),tt.getCampus(),tt.getBuilding(),tt.getCrid(),tt.getCrname(),tt.getSetsnum(),tt.getFaculty(),tt.getRoomtype(),new Gson().toJson(tt.getClass_())
				});
			}
		}
	}

	@Override
	public ArrayList<ClassRoom> getClassRoomFromDatabase() {
		List<Map<String, String>> ttMaps=DBUtil.query("select * from timetable", null, new String[]{"id","campus","building","crid","crname","setsnum","faculty","roomtype","classes"});
		ArrayList<ClassRoom> tts=new ArrayList<>();
		for (Map<String, String> map : ttMaps) {
			tts.add(new ClassRoom(map.get("id"), map.get("campus"), map.get("building"), map.get("crid"), map.get("crname"), map.get("setsnum"), map.get("faculty"), map.get("roomtype"), null, new Gson().fromJson(map.get("classes"),int[][][].class)));
		}
		return tts;
	}

	@Override
	public ArrayList<ClassRoom> search(ArrayList<ClassRoom> tts, String campus, String building, String crname,
			int[] jies, int day, int week) {
		long start=System.currentTimeMillis();
		ArrayList<ClassRoom> result=new ArrayList<ClassRoom>();
		for (ClassRoom tt : tts) {
			//判断校区
			if(tt.getCampus().equals(campus)){
				//判断教学楼
				if(tt.getBuilding().equals(building)){
					//需不需要判断教室号
					if(crname!=null){						
						if(tt.getCrname().equals(crname)){
							for (int i=0;i<jies.length;i++) {
								if(tt.getClass_()[jies[i]-1][day-1][week-1]!=0){
									break;
								}
								if(i==jies.length-1){
									result.add(tt);
								}
							}
						}
					}else{
						for (int i=0;i<jies.length;i++) {
							if(tt.getClass_()[jies[i]-1][day-1][week-1]!=0){
								break;
							}
							if(i==jies.length-1){
								result.add(tt);
							}
						}
					}
				}
			}
		}
		System.out.println("本次查询共花费"+(System.currentTimeMillis()-start)+"ms");
		return result;
	}

}
