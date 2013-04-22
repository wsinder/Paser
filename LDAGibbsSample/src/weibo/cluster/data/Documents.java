package weibo.cluster.data;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weibo.segmentword.ictclas.ICTCLAS;

public class Documents {

	List<Document> docs; //�ı���
	Map<String, Integer> termToIndexMap; //�ʵ�
	List<String> indexToTermMap; //���ݴʵ䣬��ȡ�ʵ�����
	Map<String, Integer> termCountMap; //ÿ�������ı����еĴ�Ƶ
	
	public Documents(){
		docs = new ArrayList<Document>();
		termToIndexMap = new HashMap<String, Integer>();
		indexToTermMap = new ArrayList<String>();
		termCountMap = new HashMap<String, Integer>();
	}
	
	public List<Document> getDocs(){
		return docs != null ? docs : (docs = new ArrayList<Document>());
	}
	
	public Map<String, Integer> getTermToIndexMap(){
		return termToIndexMap != null ? termToIndexMap : (termToIndexMap = new HashMap<String, Integer>());
	}
	
	public List<String> getIndexToTermMap(){
		return indexToTermMap != null ? indexToTermMap : (indexToTermMap = new ArrayList<String>());
	}
	
	public Map<String, Integer> getTermCountMap(){
		return termCountMap != null ? termCountMap : (termCountMap = new HashMap<String, Integer>());
	}
	
	/**
	 * ���ı����ִʲ�����
	 * @param texts δ������ı���
	 * @throws UnsupportedEncodingException
	 */
	public void readDocs(List<String> texts) throws UnsupportedEncodingException{
		
		if(ICTCLAS.ICTCLAS_Init(".".getBytes("UTF-8"), 1) == false){
			System.out.println("��ʼ��ʧ�ܣ�");
			return;
		}
		
		/*int i = 0;
		for(String text : texts){
			System.out.println(++i);
			Document doc = new Document(text, termToIndexMap, indexToTermMap, termCountMap);				
			docs.add(doc);
		}*/
		
		for(int i = 0; i < texts.size(); i++){
			System.out.println(i);
			String text = texts.get(i);
			Document doc = new Document(text, termToIndexMap, indexToTermMap, termCountMap);
			docs.add(doc);
		}
		ICTCLAS.ICTCLAS_Exit();
	}
	
	public class Word{
		String word;
		String attribute;
	}
	
	public class Document{

		int [] nWords;
		
		/**
		 * �����ı��ִʶ���
		 * @param text �����ı�
		 * @param termToIndexMap
		 * @param indexToTermMap
		 * @param termCountMap
		 * @throws UnsupportedEncodingException
		 */
		public Document(String text, Map<String, Integer> termToIndexMap, List<String> indexToTermMap,
				Map<String, Integer> termCountMap) throws UnsupportedEncodingException{
			
			List<Word> words = new ArrayList<Word>();
			
			readDoc(text, words);
			
			int size = words.size();
			this.nWords = new int[size];
			for(int i = 0; i < size; i++){
				String word = words.get(i).word;
				if(!termToIndexMap.containsKey(word)){
					int newIndex = termToIndexMap.size();
					termToIndexMap.put(word, newIndex);
					indexToTermMap.add(word);
					termCountMap.put(word, new Integer(1));
					nWords[i] = newIndex;
				} else {
					nWords[i] = indexToTermMap.indexOf(word);
					termCountMap.put(word, termCountMap.get(word)+1);
				}
			}
		}
		/**
		 * �����ı����ִ�
		 * @param text ���ִ��ı�
		 * @param words �ִʺ�Ĵʼ�
		 * @throws UnsupportedEncodingException
		 */
		private void readDoc(String text, List<Word> words) throws UnsupportedEncodingException{
			System.out.println(text);
			
			ICTCLAS test = new ICTCLAS();		
			test.ICTCLAS_SetPOSmap(0);
	
			byte[] resultByte = test.ICTCLAS_ParagrahProcess(text.getBytes("UTF-8"), 1);			
			String resultStr = new String(resultByte, 0, resultByte.length, "UTF-8");
			System.out.println(resultStr);

			StringBuilder strBuilder = new StringBuilder(resultStr);
			while(strBuilder.length() != 0){
				
				Word word = new Word();
				
				int slashPos = (slashPos = strBuilder.indexOf("/")) == 0 ? 1 : slashPos;			
				int spacePos = strBuilder.indexOf(" ");				
				if(slashPos != -1 && spacePos != -1 && slashPos + 1 < spacePos){
					word.word = strBuilder.substring(0, slashPos);
					word.attribute = strBuilder.substring(slashPos + 1, spacePos);
					if(word.attribute.charAt(0) != 'w' && word.word.charAt(0) != '@')
						words.add(word);					
				}
				
			    strBuilder.replace(0, spacePos, "");
			    char ch = strBuilder.charAt(0);
			    while(String.valueOf(ch).equals(" ")){
			    	strBuilder.deleteCharAt(0);
			    	if(strBuilder.length() != 0)
			    		ch = strBuilder.charAt(0);
			    	else
			    		break;
			    }
			}
			
		}
		
		public int[] getNWords(){
			return nWords;
		}
		
	}
}
