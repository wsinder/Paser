package weibo.segmentword.ictclas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


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
		
		String sInput = "感觉自己发烧了，群友说是禽流感，事实上是犬流感，窗外阳光灿烂，我却没有温暖，发抖。";
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
