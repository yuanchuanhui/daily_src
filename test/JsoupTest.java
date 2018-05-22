package test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupTest {
	public static void main(String[] args) throws IOException {
		Document doc = Jsoup.connect("http://ids.qfnu.edu.cn/authserver/login?service=http%3A%2F%2Fmy.qfnu.edu.cn%2Flogin.portal").get();
		Elements submit = doc.select("#casLoginForm > div.login_ipt.pt0 > button");
	}
}
