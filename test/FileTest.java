package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.gson.Gson;

import entity.Device;

public class FileTest {
	public static void main(String[] args) {
		System.out.println(readFromTemp().getLatlon());
	}
	public static void outputToTemp(String str){
		File file=new File("C:/Users/473574509/workspace2/daily/WebContent/temp.txt");
		try {
			FileOutputStream output=new FileOutputStream(file);
			output.write(str.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("未找到文件");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("文件访问失败");
		}
	}
	public static Device readFromTemp(){
		Gson gson=new Gson();
		File file=new File("C:/Users/473574509/workspace2/daily/WebContent/temp.txt");
		try {
			FileInputStream input=new FileInputStream(file);
			byte[] buffer=new byte[1024];
			int n=input.read(buffer);
			return gson.fromJson(new String(buffer,0,n), Device.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("未找到文件");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("文件访问失败");
		}
		return null;
	}
}
