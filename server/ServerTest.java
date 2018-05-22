package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import entity.Device;
import entity.LatLon;
import spider.JzdwSpider;
import utils.Utils;

public class ServerTest {
	public static void main(String[] args) throws IOException {
		ServerSocket server=new ServerSocket(8079);
		Socket client=server.accept();
		receive(client);
		send(client);
	}
	public static void receive(final Socket server){
		//多线程，使得可以同时发送信息与接收信息
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("开始接收信息");
				try {
					InputStream input=server.getInputStream();
					while(true){
						//目前阶段只有一台设备，即只有一个默认设备id即为1。设备信息通过Json将对象转化为字符串，直接存储在数据库中，需要时可将字符串取出用Gson还原为devie对象
						Device device=Utils.readDeviceFromDatabase("1");
						//如果APP发送了定闹钟的指令
						if(device.isChangeTime()){
							time(server,device);
							Utils.outputToDatabase("1", new Gson().toJson(device));
						}//如果APP发送了请求位置的指令
						else if(device.getRequestLatlon()==Device.REQUEST_START){
							locate(server,device);
							Utils.outputToDatabase("1", new Gson().toJson(device));
						}//如果APP发送了查询电量的指令
						else if(device.getRequestDian()==Device.REQUEST_START){
							dian(server,device);
							Utils.outputToDatabase("1", new Gson().toJson(device));
						}
						//每一秒轮询检查一次是否有指令需要执行
						Thread.sleep(1000);
					}
				}catch (SocketException e){
					System.out.println("连接断开");
					try {
						server.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	public static int parseHeart(String message){
		String[] strs=message.split(" ");
		return Integer.parseInt(strs[1]);
	}
	public static void dian(Socket server,Device device) throws IOException{
		OutputStream out=server.getOutputStream();
		InputStream input=server.getInputStream();
		System.out.println("dian");
		out.write("dian".getBytes());
		String message=Utils.inputToString(input);
		device.setDian(Integer.parseInt(message.substring(3,message.length())));
		device.setRequestDian(Device.REQUEST_END);
	}
	public static void send(final Socket client){
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("开始发送信息");
				try {
					OutputStream out=client.getOutputStream();
					while(true){
						System.out.println("发送信息：");
						Scanner scanner=new Scanner(System.in);
						StringBuilder sb=new StringBuilder();
						String temp=null;
						while(!(temp=scanner.nextLine()).equals("")){
							sb.append(temp);
						}
						System.out.println(sb.toString());
						out.write(sb.toString().getBytes());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	public static void time(Socket server,Device device) throws IOException{
		OutputStream out=server.getOutputStream();
		InputStream input=server.getInputStream();
		System.out.println("clk");
		out.write("clk".getBytes());
		try{
			if(Utils.inputToString(input).startsWith(">")){
				String msg=Utils.readTimeFromTomcat().replaceAll("\r\n", "").replace("\n", "");
				Thread.sleep(500);
				System.out.println(Utils.parseTime(Long.parseLong(msg)));
				out.write(Utils.parseTime(Long.parseLong(msg)).getBytes());
				if(Utils.inputToString(input).startsWith("clk ok")){
					System.out.println("tim");
					out.write("tim".getBytes());
					if(Utils.inputToString(input).startsWith(">")){
						Thread.sleep(500);
						System.out.println(Utils.parseTime(Calendar.getInstance().getTimeInMillis()));
						out.write(Utils.parseTime(Calendar.getInstance().getTimeInMillis()).getBytes());
						if(Utils.inputToString(input).startsWith("tim ok")){
							System.out.println("闹钟设定成功");
							device.setChangeTime(false);
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void locate(Socket server,Device device) throws IOException{
		OutputStream out=server.getOutputStream();
		InputStream input=server.getInputStream();
		System.out.println("dw");
		out.write("dw".getBytes());
		String message=Utils.inputToString(input);
		if(message.contains("CREG")){
			device.setLatlon(JzdwSpider.readJizhan(message));
			device.setRequestLatlon(Device.REQUEST_END);
		}
	}
}
