package weibo.segmentword.ictclas;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import weibo.webpage.db.WeiBoDBQuery;

import com.mysql.jdbc.Connection;


public class ICTCLAS {
	
	static{
		System.loadLibrary("ICTCLAS");
	}
	
	/**
	 * 初始化分词系统
	 * @param sDataPath 文件名称 可初始为.
	 * @param encoding 文件编码：GBK:0, UTF8:1， BIG5：2
	 * @return 函数是否执行成功 
	 */
	public static native boolean ICTCLAS_Init(byte[] sDataPath, int encoding);
	
	/**
	 * 退出分词系统
	 * @returen 函数是否执行成功
	 */
	public static native boolean ICTCLAS_Exit();
	
	/**
	 * 导入用户字典
	 * @param sPath 用户字典名称
	 * @return 词个数
	 */
	public native int ICTCLAS_ImportUserDict(byte[] sPath);
	
	/**
	 * 分割字符串文本
	 * @param sSrc 字符串文本
	 * @param bPOSTagged 是否显示词性标注 
	 * 		  显示词性： 1
	 * 		不显示词性： 0
	 * @return 分割结果
	 */
	public native byte[] ICTCLAS_ParagrahProcess(byte[] sSrc, int bPOSTagged);
	/**
	 * 分割文件文本
	 * @param sSrcFilename 输入文件名称
	 * @param sDestFilename 输出文件名称
	 * @param bPOSTagged 同上
	 * @return 程序是否执行成功
	 */
	public native boolean ICTCLAS_FileProcess(byte[] sSrcFilename, byte[] sDestFilename, int bPOSTagged);
	/**
	 * 输入动态用户词
	 * @param sWord 动态用户词
	 * @return 执行是否成功
	 */
	public native int ICTCLAS_AddUserWord(byte[] sWord);
	/**
	 * 设置标注集
	 * @param nPOSmap 
	 * 		计算机二级标注集：0
	 * 		计算机一级标注集：1
	 * 		北大二级标注集：  2
	 * 		北大一级标注集：  3
	 * @return 设置是否成功
	 */
	public native boolean ICTCLAS_SetPOSmap(int nPOSmap);
	
	public static void main(String[] args) throws UnsupportedEncodingException{
		
/*		File file = new File("./test/test.txt");
		List<String> texts = new ArrayList();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String temp = null;
		
			while((temp = reader.readLine()) != null){
					texts.add(temp);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < texts.size(); i++){
			String sInput = texts.get(i);
			if(ICTCLAS_Init(".".getBytes("UTF-8"), 1) == false){
				System.out.println("初始化失败！");
				return;
			}
			
			ICTCLAS test = new ICTCLAS();
			
			test.ICTCLAS_SetPOSmap(0);
			
			byte[] src = sInput.getBytes("UTF-8");
			byte[] resultByte = test.ICTCLAS_ParagrahProcess(src, 1);
			String resultStr = new String(resultByte, 0, resultByte.length, "UTF-8");
			System.out.println(resultStr.trim());
		}
		
		
		ICTCLAS.ICTCLAS_Exit();
		*/
		String sInput = null;
		Connection conn = (Connection) WeiBoDBQuery.getConnection();
		try {
			Statement stmt = conn.createStatement();
			String strsql = "select maintext from vtable where id=254953";
			ResultSet rs = stmt.executeQuery(strsql);
			if(rs.next())
				sInput = rs.getString(1);
			
			if(sInput.contains("\u301c")){
				sInput = sInput.replaceAll("\\u301c", "\uff5e");
			}
			if(sInput.contains("\u22ef"))
				sInput = sInput.replaceAll("\\u22ef", "\u2026");
			if(sInput.contains("\u2022"))
				sInput = sInput.replaceAll("\\u2022", "");
			String str = "";
			for(int i = 0; i < sInput.length(); i++){
				int ch = (int)sInput.charAt(i);
				str += "\\u" + Integer.toHexString(ch);
			}
			System.out.println(str);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		sInput = "小朋友出生以来，每天夜里醒来哭邻居院子里的公鸡准陪着啼叫，时间久了，这一哭一啼的两个似乎成了高山流水的知音。不过，因为“禽流感”事件，昨天主人不得不把公鸡杀了?对我们来说，这是一只特别的鸡。此时此刻，我们惦记它。｜公鸡哥哥，你在天上要都好哦｜";
		System.out.println(sInput);
		if(ICTCLAS_Init(".".getBytes("UTF-8"), 1) == false){
			System.out.println("初始化失败！");
			return;
		}
		
		ICTCLAS test = new ICTCLAS();
		
		test.ICTCLAS_SetPOSmap(0);
		
		byte[] resultByte = test.ICTCLAS_ParagrahProcess(sInput.getBytes("UTF-8"), 1);
		String resultStr = new String(resultByte, 0, resultByte.length, "UTF-8");
		System.out.println(resultStr.trim());
	
		ICTCLAS.ICTCLAS_Exit();
	}
}
