package spz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class KegongchangServlet
 */
@WebServlet("/KegongchangServlet")
public class KegongchangServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public KegongchangServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter writer=response.getWriter();
		System.out.println(request.getParameter("pw"));
		List<Map<String,String>> maps=DBUtil.query("select * from kegongchang where first=?", new String[]{request.getParameter("pw")}, new String[]{"first"});
		System.out.println(maps.size());
		if(maps.size()>0){
			writer.println("success");
			DBUtil.excute("delete from kegongchang where first=?", new String[]{request.getParameter("pw")});
		}else{
			writer.println("fail");
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
