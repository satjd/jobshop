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
 * FitnessCalc类        封装了测试用例的相关数据，便于输入
 * 		ruleList中包含了各种启发式类库中的对象，并将这些对象按下标顺序编码，规则库具体请见jobshop.algorithm.hrules包中的各个类
 * 		函数double calcFitness(Particle pt)用于计算个体适应度
 */


public class FitnessCalc
{
	public static int ruleCnt;
	public static int mchCnt = 3;
	private static int jobCnt = 3;
	private static int stepCnt = 3;
	private static String testcase_pcd = "E:\\Java codes\\workspace\\jobshop\\testcase\\case1_pcd.txt";
	private static String testcase_time = "E:\\Java codes\\workspace\\jobshop\\testcase\\case1_time.txt";
	
	private static ArrayList<AbstractRules> ruleList = new ArrayList<AbstractRules>(); //启发式规则的集合
	static
	{
		ruleList.add(new MWKRrules());
		ruleList.add(new RandomSPTrules());
		ruleList.add(new SPTrules());
		ruleList.add(new WSPTrules(0.5));
		ruleList.add(new LWKRrules());
		
		ruleCnt = ruleList.size();
	}
	public static double calcFitness(Chromosome cho) throws FileNotFoundException //计算个体适应度
	{
		return 1.0/(Scheduler.calcTime(mchCnt,jobCnt,stepCnt,testcase_pcd,testcase_time,cho, ruleList)-1);
	}
}
