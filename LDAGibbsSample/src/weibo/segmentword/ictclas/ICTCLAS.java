package weibo.segmentword.ictclas;

import java.io.UnsupportedEncodingException;

import weibo.cluster.data.Documents.Word;


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
		String sInput = "@小艳子kiki @光影魔术师之择日而栖 @就是爱黑巧克力 尝试新的外景风格，亲们，我有木有拍婚纱照的潜质。";
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
		StringBuilder strBuilder = new StringBuilder(resultStr);
		while(strBuilder.length() != 0){
			int slashPos = strBuilder.indexOf("/");
			String word = strBuilder.substring(0, slashPos);
			System.out.println(word);
			int spacePos = strBuilder.indexOf(" ");
			String attribute = strBuilder.substring(slashPos + 1, spacePos);
			System.out.println(attribute);
		    strBuilder.delete(0, spacePos);
		    
		    System.out.println(strBuilder);
		    char ch = strBuilder.charAt(0);
		    while(String.valueOf(ch).equals(" ")){
		    	strBuilder.deleteCharAt(0);
		    	if(strBuilder.length() != 0)
		    		ch = strBuilder.charAt(0);
		    	else
		    		break;
		    }
		}
		ICTCLAS.ICTCLAS_Exit();
	}		
}
