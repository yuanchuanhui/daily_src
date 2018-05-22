package spider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.Utils;

public abstract class Spider {
	//下载html页面并返回html包含的字符串
	public String downloadPage(CloseableHttpClient client,String url) throws ClientProtocolException, IOException{
		HttpGet get=new HttpGet(url);
		HttpResponse response=client.execute(get);
		System.out.println("返回状态码为："+response.getStatusLine());
		return inputToString(response.getEntity().getContent());
	}
	//下载文件
	public File downloadFile(CloseableHttpClient client,String url,String path,String fileName) throws ClientProtocolException, IOException{
		HttpGet get=new HttpGet(url);
		HttpResponse response=client.execute(get);
		File file=new File(path+"/"+fileName);
		FileOutputStream fout=new FileOutputStream(file);
		InputStream in=response.getEntity().getContent();
		byte[] buffer=new byte[8192];
		int len=0;
		while((len=in.read(buffer))!=-1){
			fout.write(buffer, 0, len);
		}
		return file;
	}
	public ArrayList<String> parse(String html){
		Document doc=Jsoup.parse(html);
		Elements elements=doc.getElementsByTag("a");
		ArrayList<String> urls=new ArrayList<>();
		for (int i=0;i<elements.size();i++) {
			Element element=elements.get(i);
			if (element.attr("href").startsWith("/move")) {
				System.out.println("https://www.5525df.com"+element.attr("href"));
				urls.add("https://www.5525df.com"+element.attr("href"));
			}
		}
		return urls;
	}
	public static String inputToString(InputStream in){
		StringBuilder sb=new StringBuilder();
		int len=0;
		byte[] buffer=new byte[1024];
		try {
			while((len=in.read(buffer))!=-1){				
				sb.append(new String(buffer,0,len));
			}
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
