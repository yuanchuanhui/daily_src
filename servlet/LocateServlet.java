package servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import entity.Device;
import entity.LatLon;
import utils.Utils;

/**
 * Servlet implementation class LocateServlet
 * GPS定位信息和基站定位信息
 */
@WebServlet("/locate")
public class LocateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LocateServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 手机APP查询位置信息从数据库
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(request.getParameter("test")!=null){
			doPost(request, response);
			return;
		}
		if(request.getParameter("id")!=null){		
			Device device=Utils.readDeviceFromDatabase(request.getParameter("id"));
			if(device.getRequestLatlon()==Device.REQUEST_NO){
				response.setStatus(404);
				device.setRequestLatlon(Device.REQUEST_START);
				Utils.outputToDatabase(request.getParameter("id"), new Gson().toJson(device));
			}else if(device.getRequestLatlon()==Device.REQUEST_END){
				device.setRequestLatlon(Device.REQUEST_NO);				
				Utils.outputToDatabase(request.getParameter("id"), new Gson().toJson(device));
				response.getWriter().println(new Gson().toJson(device));
			}else{
				response.setStatus(404);
			}
		}else{
			//返回500用来表示没有输入id
			response.setStatus(500);
		}
	}


	/**
	 * 设备更新位置信息到数据库
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(request.getParameter("id")!=null){			
			Device device=Utils.readDeviceFromDatabase(request.getParameter("id"));
			if(device!=null){				
				device.setLatlon(new LatLon(request.getParameter("lat"), request.getParameter("lon")));
				Utils.outputToDatabase(request.getParameter("id"), new Gson().toJson(device));
			}else{
				response.setStatus(404); 
			}
		}else{
			response.setStatus(500);
		}
	}

}
