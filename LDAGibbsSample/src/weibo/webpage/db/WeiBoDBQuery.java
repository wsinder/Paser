package weibo.webpage.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WeiBoDBQuery {
	public static Connection getConnection(){
		
		Connection conn = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/weibo",
					"admin", "admin");
			if(!conn.isClosed()){
				System.out.println("succeeded connecting to database");
			}
		}catch (ClassNotFoundException e) {
			System.out.println("Start JDBC driver failed! Class not found!");
		}catch (SQLException e) {			
			e.printStackTrace();
		}
		return conn;
	}
	
	
	/**
	 * 
	 * @param stmt
	 * @param colName
	 * @return
	 * @throws SQLException
	 */
	
	public static ResultSet getSpecificColumn(Statement stmt, String colName) throws SQLException{
		String strSelColumn = "select " + colName + " from vtable";
		ResultSet rs = stmt.executeQuery(strSelColumn);
		return rs;
	}
	/**
	 *@param stmt
	 * @param 
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getMainTextByTime(Statement stmt, String time) throws SQLException{
		String strTextByTime = "select maintext from vtable where date(time) = " + time + "order by time";
		ResultSet rs = stmt.executeQuery(strTextByTime);
		return rs;
	}
	
	/**
	 *  @param stmt
	 * @return
	 * @throws SQLException 
	 */
	public static ResultSet getNickNameCount(Statement stmt) throws SQLException{
		String strNickName = "select count(distinct nickname) from vtable";
		ResultSet rs = stmt.executeQuery(strNickName);
		return rs;
	}
	
	
	/**
	 * @param stmt
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getAll(Statement stmt) throws SQLException{
		String strAll = "select * from vtable";
		ResultSet rs = stmt.executeQuery(strAll);
		return rs;
	}
	
	public static List<String> getTexts(){
		
		List<String> strs = new ArrayList<String>();
		try {
			Connection conn = getConnection();
			Statement stmt = conn.createStatement();
		
			ResultSet rs = getSpecificColumn(stmt, "maintext");
			while(rs.next()){
				String text = rs.getString(1);
				if(text.contains("\u301c"))
					text = text.replaceAll("\\u301c", "\uff5e");
				if(text.contains("\u22ef"))
					text = text.replaceAll("\\u22ef", "\u2026");
				if(text.contains("\u2022"))
					text = text.replaceAll("\\u2022", "");
				System.out.println(text);
				strs.add(text);
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return strs;
	}
	
	public static void main(String[] args){
		Connection conn = getConnection();
		try {
			Statement stmt = conn.createStatement();
			
			ResultSet rs = getSpecificColumn(stmt, "maintext");
			while(rs.next()){
				String mainText = rs.getString(1);
				System.out.println(mainText);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
