package spz;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DBUtil {

	public static final String URL="jdbc:MySQL://47.95.10.45:3306/ych";
	public static final String NAME="root";
	public static final String PASSWORD="Zrw0714521-";
//	public static final String PASSWORD="";
//	public static final String URL="jdbc:MySQL://localhost:3306/test";
//	public static final String NAME="root";
//	public static final String PASSWORD="hui199611";
//	public static final String URL="jdbc:MySQL://localhost:3306/ych";
//	public static final String NAME="root";
//	public static final String PASSWORD="Zrw0714521-";
	
	static{
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("The driven is not found");
		}
	}

	public static Connection getConnection(){
		try {
			return DriverManager.getConnection(URL,NAME,PASSWORD);
		} catch (SQLException e) {
			System.err.println("Connection message is wrong");
		}
		return null;
	}

	public static List<Map<String, String>> query(String sql,String[] params,String[] keys) {
		Connection conn=getConnection();
		ArrayList<Map<String, String>> list=new ArrayList<>();
		if(conn!=null){
			PreparedStatement statement;
			try {
				statement = conn.prepareStatement(sql,params);
				if(params!=null){
					for(int i=0;i<params.length;i++){
						statement.setString(i+1, params[i]);
					}
				}
				ResultSet set=statement.executeQuery();
				if(set!=null){
					while (set.next()) {
						HashMap<String, String> map=new HashMap<String,String>();
						for(String key:keys){
							map.put(key, set.getString(key));
							//							System.out.println(key+":"+set.getString(key));
						}
						list.add(map);
					}
				}
				conn.close();
				return list;
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return null;
			}
		}else{
			System.err.println("There are some exception in query");
			return null;
		}
	}
	public static boolean excute(String sql,String[] params) {
		Connection conn=getConnection();
		if(conn!=null){
			PreparedStatement statement;
			try {
				statement = conn.prepareStatement(sql);
				int j=0;
				if(params!=null){
					for(int i=0;i<params.length;i++){
						if(params[i]!=null){
							statement.setString(j+1, params[i]);
							j++;
						}
					}
				}
				statement.execute();
				conn.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return false;
			}
		}else{
			System.err.println("There are some exception in execute");
			return false;
		}
	}
}
