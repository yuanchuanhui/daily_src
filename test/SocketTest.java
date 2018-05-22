package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTest {
	public static void main(String[] args) throws IOException {
		ServerSocket server=new ServerSocket(8080);
		Socket socket=server.accept();
		BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String line=reader.readLine();
		while(line!=null&&!reader.readLine().equals("exit")){
			System.out.println(line);
			line=reader.readLine();
		}
	}
}
