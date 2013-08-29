package com.pku.yangliu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

/**����ѵ�����������������������
 * @author yangliu
 * @qq 772330184 
 * @mail yang.liu@pku.edu.cn
 *
 */
public class CreateTrainAndTestSample {
	
	void filterSpecialWords() throws IOException {
		// TODO Auto-generated method stub
		String word;
		ComputeWordsVector cwv = new ComputeWordsVector();
		//String fileDir = "F:/DataMiningSample/processedSample_includeNotSpecial";
		String fileDir = ClassifierMain.DEFALUT_DIR+"processedSample_includeNotSpecial";
		SortedMap<String,Double> wordMap = new TreeMap<String,Double>();
		wordMap = cwv.countWords(fileDir, wordMap);
		cwv.printWordMap(wordMap);//��wordMap������ļ�
		File[] sampleDir = new File(fileDir).listFiles();
		for(int i = 0; i < sampleDir.length; i++){
			File[] sample = sampleDir[i].listFiles();
			String targetDir = ClassifierMain.DEFALUT_DIR+"processedSampleOnlySpecial/"+sampleDir[i].getName();
			if(!new File(ClassifierMain.DEFALUT_DIR+"processedSampleOnlySpecial").exists()){
				new File(ClassifierMain.DEFALUT_DIR+"processedSampleOnlySpecial").mkdir();
			}
			File targetDirFile = new File(targetDir);
			if(!targetDirFile.exists()){
				targetDirFile.mkdir();
			}
			for(int j = 0;j < sample.length; j++){	
				String fileShortName = sample[j].getName();
				if(fileShortName.contains("stemed")){
					targetDir = ClassifierMain.DEFALUT_DIR+"processedSampleOnlySpecial/"+sampleDir[i].getName()+"/"+fileShortName.substring(0,5);
					FileWriter tgWriter= new FileWriter(targetDir);
					FileReader samReader = new FileReader(sample[j]);
					BufferedReader samBR = new BufferedReader(samReader);
					while((word = samBR.readLine()) != null){
						if(wordMap.containsKey(word)){
							tgWriter.append(word + "\n");
						}
					}
					tgWriter.flush();
					tgWriter.close();
				}
			}
		}
	}
	
	void createTestSamples(String fileDir, double trainSamplePercent,int indexOfSample,String classifyResultFile) throws IOException {
		// TODO Auto-generated method stub
		String word, targetDir;
		FileWriter crWriter = new FileWriter(classifyResultFile);//����������ȷ��Ŀ��¼�ļ�
		File[] sampleDir = new File(fileDir).listFiles();
		for(int i = 0; i < sampleDir.length; i++){
			File[] sample = sampleDir[i].listFiles();
			double testBeginIndex = indexOfSample*(sample.length * (1-trainSamplePercent));//������������ʼ�ļ����
			double testEndIndex = (indexOfSample+1)*(sample.length * (1-trainSamplePercent));//�����������Ľ����ļ����
			for(int j = 0;j < sample.length; j++){				
				FileReader samReader = new FileReader(sample[j]);
				BufferedReader samBR = new BufferedReader(samReader);
				String fileShortName = sample[j].getName();
				String subFileName = fileShortName;
				if(j > testBeginIndex && j< testEndIndex){//����ڹ涨�����ڵ���Ϊ������������ҪΪ���������������-����ļ������������Ľ����һ�ж�Ӧһ���ļ�������ͳ��׼ȷ��
					targetDir = ClassifierMain.DEFALUT_DIR+"TestSample"+indexOfSample+"/"+sampleDir[i].getName();
					crWriter.append(subFileName + " " + sampleDir[i].getName()+"\n");
					
					}
				else{//������Ϊѵ������
					targetDir = ClassifierMain.DEFALUT_DIR+"TrainSample"+indexOfSample+"/"+sampleDir[i].getName();
				}
				targetDir = targetDir.replace("\\","/");
				File trainSamFile = new File(targetDir);
				if(!trainSamFile.exists()){
					trainSamFile.mkdir();
				}
				targetDir += "/"+subFileName;
				FileWriter tsWriter = new FileWriter(new File(targetDir));
				while((word = samBR.readLine()) != null){
					tsWriter.append(word + "\n");
				}
				tsWriter.flush();
				tsWriter.close();	
			}
		}
		crWriter.flush();
		crWriter.close();
	}
}