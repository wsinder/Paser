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
				System.out.println("��ʼ��ʧ�ܣ�");
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
		
		String sInput = "�о��Լ������ˣ�Ⱥ��˵�������У���ʵ����Ȯ���У�����������ã���ȴû����ů��������";
		System.out.println(sInput);
		if(ICTCLAS_Init(".".getBytes("UTF-8"), 1) == false){
			System.out.println("��ʼ��ʧ�ܣ�");
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
