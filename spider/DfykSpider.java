package spider;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DfykSpider {
	public static void main(String[] args) throws ClientProtocolException, IOException {
		Spider spider=new Spider() {};
		CloseableHttpClient client=HttpClients.createDefault();
		ArrayList<String> pageUrls=new ArrayList<>();
//		pageUrls.addAll(spider.parse(spider.downloadPage(client, "https://www.5525df.com/s/shunvrenqi/")));
//		pageUrls.addAll(spider.parse(spider.downloadPage(client, "https://www.5525df.com/s/shunvrenqi/index_2.html")));
//		pageUrls.addAll(spider.parse(spider.downloadPage(client, "https://www.5525df.com/s/shunvrenqi/index_3.html")));
//		ArrayList<String> mp4Urls=new ArrayList<>();
//		for (String url : pageUrls) {
			try{
//				System.out.println(url);
				String html=spider.downloadPage(client, "https://www.5525df.com/move/2/2016-04-11/1254.html");
				Document doc=Jsoup.parse(html);
				System.out.println(html);
//				mp4Urls.add(doc.select("body > div:nth-child(15) > div:nth-child(4) > b > font > a").attr("href"));
				System.out.println(doc.select("body > div:nth-child(15) > div:nth-child(4) > b > font > a").attr("href"));
				System.out.println(doc.select("body > div:nth-child(15) > div:nth-child(4) > b > font > a").html());
			}catch (Exception e) {
				e.printStackTrace();
			}
//		}
//		for (String mp4Url : mp4Urls) {
//			System.out.println(mp4Url);
//		}
	}
}
