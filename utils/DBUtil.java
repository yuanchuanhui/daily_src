package utils;
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

public class DBUtil {

//	public static final String URL="jdbc:MySQL://39.108.180.80:3306/test?useUnicode=true&amp;characterEncoding=utf-8";
	public static final String URL="jdbc:MySQL://localhost:3306/test?useUnicode=true&amp;characterEncoding=utf-8";
	public static final String NAME="root";
	public static final String PASSWORD="hui199611";
	public static Connection conn=null;
	//	public static final String PASSWORD="";

	static{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection(URL,NAME,PASSWORD);
		}catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			System.err.println("获取连接失败");
		}
	}

	/**
	 * @return map��list����
	 * @param sql Ҫִ�е�sql��䣬֧��ռλ��
	 * @param params sql���Ĳ���
	 * @param keys Ҫ���ҵ����������飬���ڴ���map
	 * */
	public static List<Map<String, String>> query(String sql,String[] params,String[] keys) {
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
						if(keys!=null){
							for(String key:keys){
								map.put(key, set.getString(key));
							}
						}
						list.add(map);
					}
				}
				return list;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}else{
			System.err.println("����Ϊ��");
			return null;
		}
	}
	public static boolean excute(String sql,String[] params) {
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
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}else{
			System.err.println("����Ϊ��");
			return false;
		}
	}
}
