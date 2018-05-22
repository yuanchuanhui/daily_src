package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

import spider.JzdwSpider;
import utils.Constant;

public class ClientTest {
	public static void main(String[] args) throws IOException {
		Socket socket=new Socket(Constant.host,8079);
		receive(socket);
		send(socket);
	}
	public static void receive(final Socket client){
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("开始接收信息");
				byte[] buffer=new byte[1024];
				try {
					InputStream input=client.getInputStream();
					while(true){
						buffer=new byte[1024];
						input.read(buffer);
						String msg=new String(buffer);
						System.out.println("收到信息："+msg);
						if(msg.startsWith("+CREG")){
							System.out.println(JzdwSpider.readJizhan(msg));
						}
					}
				}catch (SocketException e){
					System.out.println("连接断开");
					e.printStackTrace();
				}catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}).start();
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
						out.write(sb.toString().getBytes());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
