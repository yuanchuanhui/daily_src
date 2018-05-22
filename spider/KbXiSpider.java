package spider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;

import entity.ClassRoom;
import utils.Utils;

public class KbXiSpider {
	public static void main(String[] args) throws ClientProtocolException, IOException {
		//fidder代理
//		HttpHost proxy = new HttpHost("localhost", 8888);
//		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
//		CloseableHttpClient httpclient = HttpClients.custom().setMaxConnPerRoute(80)
//		        .setRoutePlanner(routePlanner)
//		        .build();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		//获取cookie（httpClient自带功能）
		HttpGet httpGet = new HttpGet("http://ids.qfnu.edu.cn/authserver/login?service=http%3A%2F%2Fmy.qfnu.edu.cn%2Findex.portal");
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		//同时保持的连接数有限制，所以用完必须先关闭
		response1.getEntity().consumeContent();
		//执行post登陆
		HttpPost httpPost = new HttpPost("http://ids.qfnu.edu.cn/authserver/login?service=http%3A%2F%2Fmy.qfnu.edu.cn%2Findex.portal");
		UrlEncodedFormEntity logingEntity = new UrlEncodedFormEntity(LoginAnalizer.parseAndInputLoginPage(Utils.entityAsString(response1.getEntity(),"utf-8")), Consts.UTF_8);
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
//		//请求信息
		httpGet = new HttpGet("http://202.194.188.19/jskbcxAction.do?oper=cxkb&js_zxjxjhh=2017-2018-2-1&js_xq=&js_jxl=&js_js=&pageSize=460&page=1&currentPage=1&pageNo=");
		CloseableHttpResponse cxkbResponse=httpclient.execute(httpGet);
		Utils.save("C:/Users/473574509/PycharmProjects/untitled/test/test.html", cxkbResponse.getEntity().getContent());
	}
	public static void downloadTTHtml() throws ClientProtocolException, IOException{
		String baseUrl="http://202.194.188.19";
		//缓存所有课表页面
		ClassRoom[] tts=ClassRoom.getAll("C:/Users/473574509/PycharmProjects/untitled/test/test.html");
		CloseableHttpClient httpClient=LoginAnalizer.login();
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
			CloseableHttpResponse response=httpClient.execute(get);
			Utils.save("C:\\Users\\473574509\\Documents\\timetable\\"+
					tt.getId()+"_"+
					tt.getCampus()+"_"+
					tt.getBuilding()+"_"+
					tt.getCrid()+"_"+
					tt.getCrname()+"_"+
					tt.getSetsnum()+"_"+
					tt.getFaculty()+"_"+
					tt.getRoomtype()+"_"+".html",response.getEntity().getContent());
		}
	}
}
