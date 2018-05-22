package spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import entity.LatLon;

public class JzdwSpider {
	public static void main(String[] args) {
		
	}
	public static LatLon readJizhan(String message){
		Pattern pattern=Pattern.compile("\"\\d*[A-Z]*\"");
		Matcher matcher=pattern.matcher(message);
		ArrayList<String> jizhanarray=new ArrayList<String>();
		while(matcher.find()){
			jizhanarray.add(matcher.group(0).substring(1,matcher.group(0).length()-1));
		}
		if(jizhanarray.size()==2){
			return jizhanToGPS(String.valueOf(Integer.parseInt(jizhanarray.get(0),16)),String.valueOf(Integer.parseInt(jizhanarray.get(1),16)));
		}
		return null;
	}
	public static LatLon jizhanToGPS(String lac,String ci) {
		URL url;
		try {
			url = new URL("http://api.cellocation.com:81/cell/?coord=gcj02&output=json&mcc=460&mnc=1&lac="+lac+"&ci="+ci);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.connect();
			BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
			String line=reader.readLine();
			System.out.println(line);
			Gson gson=new Gson();
			LatLon latlon=gson.fromJson(line, LatLon.class);
			reader.close();
			return latlon;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
