package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

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
 * Servlet implementation class TimeServlet
 * 闹钟信息
 */
@WebServlet("/time")
public class TimeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimeServlet() {
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
			if(device!=null){
				response.getWriter().println(device.getTime());
			}else{
				response.setStatus(404);
			}
		}else{
			//返回500用来表示没有输入id
			response.setStatus(500);
		}
	}


	/**
	 * 设备更新闹钟信息到数据库
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(request.getParameter("id")!=null){			
			Device device=Utils.readDeviceFromDatabase(request.getParameter("id"));
			if(device!=null){				
				device.setChangeTime(true);
				device.setTime(request.getParameter("time"));
				Utils.outputToDatabase(request.getParameter("id"), new Gson().toJson(device));
			}else{
				response.setStatus(404); 
			}
		}else{
			response.setStatus(500);
		}
	}

}
