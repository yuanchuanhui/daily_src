package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import entity.Device;
import utils.Utils;

/**
 * Servlet implementation class HeartServlet
 * 心率
 */
@WebServlet("/dian")
public class DianServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DianServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * 手机APP查询电量
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(request.getParameter("test")!=null){
			doPost(request, response);
			return;
		}
		if(request.getParameter("id")!=null){			
			Device device=Utils.readDeviceFromDatabase(request.getParameter("id"));
			if(device.getRequestDian()==Device.REQUEST_NO){
				//返回404表示目前APP没有发送指令
				response.setStatus(404);
				device.setRequestDian(Device.REQUEST_START);
				Utils.outputToDatabase(request.getParameter("id"), new Gson().toJson(device));
			}else if(device.getRequestDian()==Device.REQUEST_END){
				device.setRequestDian(Device.REQUEST_NO);				
				Utils.outputToDatabase(request.getParameter("id"), new Gson().toJson(device));
				response.getWriter().println(new Gson().toJson(device));
			}else{
				response.setStatus(404);
			}
		}
	}

	/**
	 * 设备更新电量信息到数据库
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(request.getParameter("id")!=null){			
			Device device=Utils.readDeviceFromDatabase(request.getParameter("id"));
			if(device!=null){				
				device.setHeart(Integer.parseInt(request.getParameter("heart")));
				Utils.outputToDatabase(request.getParameter("id"), new Gson().toJson(device));
			}else{
				response.setStatus(404); 
			}
		}else{
			response.setStatus(500);
		}
	}

}
