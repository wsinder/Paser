package weibo.cluster.method;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import weibo.cluster.configure.LDAModelParameters;
import weibo.cluster.data.Documents;
import weibo.webpage.db.WeiBoDBQuery;

public class LDAGibbsSample {

	int [][] docs;
	int [][] z;
	int K, V, M;
	int [][] nmk;
	int [][] nkt;
	int [] nm;
	int [] nk;
	double [][] phi;
	double [][] theta;
	double alpha;
	double beta;	
	int numstats;
	
	int burnInPeriod;
	int iterations;
	int sampleLag;
	
	public void loadConfigure(){
		this.alpha = LDAModelParameters.ALPHA;
		this.beta = LDAModelParameters.BETA;
		this.K = LDAModelParameters.TOPICNUM;
		this.burnInPeriod = LDAModelParameters.BURNINPERIOD;
		this.sampleLag = LDAModelParameters.SAMPLELAG;
		this.iterations = LDAModelParameters.ITERATIONS;
	}
	
	public LDAGibbsSample(){
		loadConfigure();
	}
	
	public void initializeModel(Documents docSet){
		this.M = docSet.getDocs().size();
		this.V = docSet.getTermToIndexMap().size();
		
		nmk = new int[M][K];
		nkt = new int[K][V];
		nm = new int[M];
		nk = new int[K];
		phi = new double[K][V];
		theta = new double[M][K];
		
		for(int k = 0; k < K; k++){
			for(int v = 0; v < V; v++){
				phi[k][v] = 0;
			}
		}
		
		for(int m = 0; m < M; m++){
			for(int k = 0; k < K; k++){
				theta[m][k] = 0;
			}
		}
		
		docs = new int[M][];
		for(int m = 0; m < M; m++){
			int [] words = docSet.getDocs().get(m).getNWords();
			int N = words.length;
			
			docs[m] = new int[N];
			for(int n = 0; n < N; n++){
				docs[m][n] = words[n];
			}
		}
		
		z = new int[M][];
		for(int m = 0; m < M; m++){
			int [] words = docSet.getDocs().get(m).getNWords();
			int N = words.length;
			
			z[m] = new int[N];
			for(int n = 0; n < N; n++){
				int initTopic = (int)(Math.random() * K);
				z[m][n] = initTopic;
				nmk[m][initTopic]++;
				nkt[initTopic][docs[m][n]]++;
				nk[initTopic]++;
			}
			nm[m] = N;
		}
	}
	
	public void gibbsSample(Documents docSet){
		
		this.numstats = 0;
		initializeModel(docSet);
		
		for(int i = 0; i < iterations; i++){
			
			for(int m = 0; m < z.length; m++){
				for(int n = 0; n < z[m].length; n++){
					int newTopic = sampleTopicZ(m, n);
					z[m][n] = newTopic;
				}
			}
			
			if(i > burnInPeriod && sampleLag > 0 && i % sampleLag == 0){
				updateParameters();
			}
		}		
	}
	
	public int sampleTopicZ(int m, int n){
		
		int oldTopic = z[m][n];
		nmk[m][oldTopic]--;
		nkt[oldTopic][docs[m][n]]--;
		nm[m]--;
		nk[oldTopic]--;
		
		double [] p = new double[K];
		for(int k = 0; k < K; k++){
			p[k] = (nmk[m][k] + alpha)/(nm[m] + K*alpha)*
				(nkt[k][docs[m][n]] + beta)/(nk[k] + V*beta);
		}
		
		for(int k = 1; k < K; k++){
			p[k] += p[k-1];
		}
		
		double u = Math.random() * p[K - 1];
		int newTopic = 0;
		for(newTopic = 0; newTopic < K; newTopic ++){
			if(u < p[newTopic])
				break;
		}
		nmk[m][newTopic] ++;
		nkt[newTopic][docs[m][n]] ++;
		nm[m] ++;
		nk[newTopic] ++;
		
		return newTopic;
	}
	
	public void updateParameters(){
		for(int m = 0; m < M; m++){
			for(int k = 0; k < K; k++){
				theta[m][k] += (nmk[m][k] + alpha)/(nm[m] + K*alpha);
			}
		}
		
		for(int k = 0; k < K; k++){
			for(int v = 0; v < V; v++){
				phi[k][v] += (nkt[k][v] + beta)/(nk[k] + V*beta);
			}
		}
		numstats++;
	}
	
	public double[][] getTheta(){
		if(sampleLag > 0){
			for(int m = 0; m < M; m++){
				for(int k = 0; k < K; k++){
					theta[m][k] /= numstats;
				}
			}
		} else {
			for(int m = 0; m < M; m++){
				for(int k = 0; k < K; k++){
					theta[m][k] = (nmk[m][k] + alpha)/(nm[m] + K*alpha);
				}
			}
		}
		
		return theta;
	}	
	
	public double[][] getPhi(){
		if(sampleLag > 0){
			for(int k = 0; k < K; k++){
				for(int v = 0; v < V; v++){
					phi[k][v] /= numstats; 
				}
			}
		} else {
			for(int k = 0; k < K; k++){
				for(int v = 0; v < V; v++){
					phi[k][v] = (nkt[k][v] + beta)/(nk[k] + V*beta);
				}
			}
		}
		return phi;
	}
	
	public static void main(String [] args){
		
		Documents docSet = new Documents();
//		List<String> texts = new ArrayList<String>();
		
		List<String> texts;
		try {
			
			texts = WeiBoDBQuery.getTexts();
			
			docSet.readDocs(texts);
			texts = null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LDAGibbsSample ldaTest = new LDAGibbsSample();
		ldaTest.gibbsSample(docSet);
		
		double [][] phi = ldaTest.getPhi();
		int topNum = 10;
		
		for(int i = 0; i < ldaTest.K; i++){
			
			List<Integer> tWordsIndexArray = new ArrayList<Integer>();
			for(int j = 0; j < ldaTest.V; j++){
				tWordsIndexArray.add(new Integer(j));
			}
			Collections.sort(tWordsIndexArray, new LDAGibbsSample.TwordsComparable(phi[i]));
			System.out.println("topic " + i + ":");
			for(int k = 0; k < topNum; k++){
				System.out.println(docSet.getIndexToTermMap().get(tWordsIndexArray.get(k)) + ". " + phi[i][tWordsIndexArray.get(k)]);
			}
		}
		
	}
	
	public static class TwordsComparable implements Comparator<Integer>{
		public double[] sortProb;
		
		public TwordsComparable(double[] sortProb){
			this.sortProb = sortProb;
		}

		@Override
		public int compare(Integer arg0, Integer arg1) {
			// TODO Auto-generated method stub
			
			if(sortProb[arg0] > sortProb[arg1])
				return -1;
			else if(sortProb[arg0] < sortProb[arg1])
				return 1;
			else
				return 0;
		}
	}
}
