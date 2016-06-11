package jobshop.algorithm.hyperheuristic;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import jobshop.algorithm.hrules.AbstractRules;
import jobshop.algorithm.hrules.LWKRrules;
import jobshop.algorithm.hrules.MWKRrules;
import jobshop.algorithm.hrules.RandomSPTrules;
import jobshop.algorithm.hrules.SPTrules;
import jobshop.algorithm.hrules.WSPTrules;
import jobshop.calctime.Scheduler;

/*
 * hyperheuristic.FitnessCalc.java
 * 
 * FitnessCalc��        ��װ�˲���������������ݣ���������
 * 		ruleList�а����˸�������ʽ����еĶ��󣬲�����Щ�����±�˳����룬�����������jobshop.algorithm.hrules���еĸ�����
 * 		����double calcFitness(Particle pt)���ڼ��������Ӧ��
 */


public class FitnessCalc
{
	public static int ruleCnt;
	public static int mchCnt = 3;
	private static int jobCnt = 3;
	private static int stepCnt = 3;
	private static String testcase_pcd = "E:\\Java codes\\workspace\\jobshop\\testcase\\case1_pcd.txt";
	private static String testcase_time = "E:\\Java codes\\workspace\\jobshop\\testcase\\case1_time.txt";
	
	private static ArrayList<AbstractRules> ruleList = new ArrayList<AbstractRules>(); //����ʽ����ļ���
	static
	{
		ruleList.add(new MWKRrules());
		ruleList.add(new RandomSPTrules());
		ruleList.add(new SPTrules());
		ruleList.add(new WSPTrules(0.5));
		ruleList.add(new LWKRrules());
		
		ruleCnt = ruleList.size();
	}
	public static double calcFitness(Chromosome cho) throws FileNotFoundException //���������Ӧ��
	{
		return 1.0/(Scheduler.calcTime(mchCnt,jobCnt,stepCnt,testcase_pcd,testcase_time,cho, ruleList)-1);
	}
}
