package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import entity.LatLon;
import server.ServerTest;

//RMC,\d*\.\d*,A,\d*.\d*,[N|S],\d*.\d*,[E|W]
public class GPSAnalizer {
	public static void main(String[] args) throws IOException {
//		System.out.println(ServerTest.parseGPS("$GPRMC,062604.00,A,3535.94698,N,11657.76612,E,0.677,,230417,,,A*79"+
//				"$GPVTG,,T,,M,0.677,N,1.254,K,A*27"+
//				"$GPGGA,062604.00,3535.94698,N,11657.76612,E,1,03,3.84,150.0,M,-4.8,M,,*49"+
//				"$GPGSA,A,2,27,16,09,,,,,,,,,,3.97,3.84,1.00*0B"+
//				"$GPGSV,2,1,08,07,25,314,24,08,57,212,18,09,37,283,22,11,10,192,21*7C"+
//				"$GPGSV,2,2,08,16,49,043,33,26,27,067,,27,81,073,40,31,07,128,19*76"+
//				"$GPGLL,3535.94698,N,11657.76612,E,062604.00,A,A*65"));
		System.out.println(getGPSPosition());
	}
	public static LatLon getGPSPosition(){
		try{
			File file=new File("C:/Users/473574509/Documents/Tencent Files/473574509/FileRecv/MobileFile/GPS(1).txt");
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			Pattern pattern=Pattern.compile("RMC,\\d*\\.\\d*,A,\\d*.\\d*,[N|S],\\d*.\\d*,[E|W]");
			Pattern patternNOrS=Pattern.compile("\\d*.\\d*,[N|S]");
			Pattern patternEOrW=Pattern.compile("\\d*.\\d*,[E|W]");
			String line=br.readLine();
			line="$GPRMC,062604.00,A,3535.94698,N,11657.76612,E,0.677,,230417,,,A*79"+
					"$GPVTG,,T,,M,0.677,N,1.254,K,A*27"+
					"$GPGGA,062604.00,3535.94698,N,11657.76612,E,1,03,3.84,150.0,M,-4.8,M,,*49"+
					"$GPGSA,A,2,27,16,09,,,,,,,,,,3.97,3.84,1.00*0B"+
					"$GPGSV,2,1,08,07,25,314,24,08,57,212,18,09,37,283,22,11,10,192,21*7C"+
					"$GPGSV,2,2,08,16,49,043,33,26,27,067,,27,81,073,40,31,07,128,19*76"+
					"$GPGLL,3535.94698,N,11657.76612,E,062604.00,A,A*65";
			while(line!=null){			
				Matcher matcher=pattern.matcher(line);
				if(matcher.find()){
					Matcher matcherNOrS=patternNOrS.matcher(matcher.group(0));
					Matcher matcherEOrW=patternEOrW.matcher(matcher.group(0));
					matcherNOrS.find();
					matcherEOrW.find();
					String slat=matcherNOrS.group(0).substring(0,matcherNOrS.group(0).length()-2);
					String slng=matcherEOrW.group(0).substring(0,matcherEOrW.group(0).length()-2);
					return LatLon.standard(slat,slng);
				}
				line=br.readLine();
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("��ȡλ��ʧ��");
		}
		return null;
	}
}
