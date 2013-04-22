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
			conn = DriverManager.getConnection("jdbc:mysql://10.110.139.147:3306/weibo",
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
	 * 鏁版嵁搴撳瓧娈�id(int)锛�nickname(String)锛�homepageurl(String), authtype(int), weibovip(int), weibodaren(int), maintext(String), 
	 * 		repostnum(int), commentnum(int), time();
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
	 * 鍙栧嚭鏌愪竴澶╃殑姝ｆ枃鏁版嵁锛屽苟鎸夋椂闂村厛鍚庢帓搴�
	 * @param stmt
	 * @param time 渚嬪锛�989-1-1锛�989-01-01锛�1989/1/1; 1989/01/01; 19890101; 890101; 89/1/1绛夌瓑
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getMainTextByTime(Statement stmt, String time) throws SQLException{
		String strTextByTime = "select maintext from vtable where date(time) = " + time + "order by time";
		ResultSet rs = stmt.executeQuery(strTextByTime);
		return rs;
	}
	
	/**
	 * 寰楀埌鐢ㄦ埛鐨勬暟鐩紝涓嶉噸澶�
	 * @param stmt
	 * @return
	 * @throws SQLException 
	 */
	public static ResultSet getNickNameCount(Statement stmt) throws SQLException{
		String strNickName = "select count(distinct nickname) from vtable";
		ResultSet rs = stmt.executeQuery(strNickName);
		return rs;
	}
	
	
	/**
	 * 寰楀埌鎵�湁鐨勬暟鎹�
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
