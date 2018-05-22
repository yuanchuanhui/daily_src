package spider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import utils.Utils;

/**
 * 解析登陆页面，填上各种验证数据
 * */

public class LoginAnalizer {
	public static void main(String[] args) throws IOException {
		
	}
	public static List<NameValuePair> parseAndInputLoginPage(String html){
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
	public static CloseableHttpClient login(){
		try{
			CloseableHttpClient httpclient = HttpClients.createDefault();
			//获取cookie（httpClient自带功能）
			HttpGet httpGet = new HttpGet("http://ids.qfnu.edu.cn/authserver/login?service=http%3A%2F%2Fmy.qfnu.edu.cn%2Findex.portal");
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
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
			return httpclient;
		}catch (Exception e) {
			System.out.println("登陆出错");
			e.printStackTrace();
			return null;
		}
	}
}
