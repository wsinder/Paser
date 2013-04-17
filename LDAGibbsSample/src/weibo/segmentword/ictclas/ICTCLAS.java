package weibo.segmentword.ictclas;

import java.io.UnsupportedEncodingException;

import weibo.cluster.data.Documents.Word;


public class ICTCLAS {
	
	static{
		System.loadLibrary("ICTCLAS");
	}
	
	/**
	 * ��ʼ���ִ�ϵͳ
	 * @param sDataPath �ļ����� �ɳ�ʼΪ.
	 * @param encoding �ļ����룺GBK:0, UTF8:1�� BIG5��2
	 * @return �����Ƿ�ִ�гɹ� 
	 */
	public static native boolean ICTCLAS_Init(byte[] sDataPath, int encoding);
	
	/**
	 * �˳��ִ�ϵͳ
	 * @returen �����Ƿ�ִ�гɹ�
	 */
	public static native boolean ICTCLAS_Exit();
	
	/**
	 * �����û��ֵ�
	 * @param sPath �û��ֵ�����
	 * @return �ʸ���
	 */
	public native int ICTCLAS_ImportUserDict(byte[] sPath);
	
	/**
	 * �ָ��ַ����ı�
	 * @param sSrc �ַ����ı�
	 * @param bPOSTagged �Ƿ���ʾ���Ա�ע 
	 * 		  ��ʾ���ԣ� 1
	 * 		����ʾ���ԣ� 0
	 * @return �ָ���
	 */
	public native byte[] ICTCLAS_ParagrahProcess(byte[] sSrc, int bPOSTagged);
	/**
	 * �ָ��ļ��ı�
	 * @param sSrcFilename �����ļ�����
	 * @param sDestFilename ����ļ�����
	 * @param bPOSTagged ͬ��
	 * @return �����Ƿ�ִ�гɹ�
	 */
	public native boolean ICTCLAS_FileProcess(byte[] sSrcFilename, byte[] sDestFilename, int bPOSTagged);
	/**
	 * ���붯̬�û���
	 * @param sWord ��̬�û���
	 * @return ִ���Ƿ�ɹ�
	 */
	public native int ICTCLAS_AddUserWord(byte[] sWord);
	/**
	 * ���ñ�ע��
	 * @param nPOSmap 
	 * 		�����������ע����0
	 * 		�����һ����ע����1
	 * 		���������ע����  2
	 * 		����һ����ע����  3
	 * @return �����Ƿ�ɹ�
	 */
	public native boolean ICTCLAS_SetPOSmap(int nPOSmap);
	
	public static void main(String[] args) throws UnsupportedEncodingException{
		String sInput = "@С����kiki @��Ӱħ��ʦ֮���ն��� @���ǰ����ɿ��� �����µ��⾰������ǣ�����ľ���Ļ�ɴ�յ�Ǳ�ʡ�";
		if(ICTCLAS_Init(".".getBytes("UTF-8"), 1) == false){
			System.out.println("��ʼ��ʧ�ܣ�");
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
