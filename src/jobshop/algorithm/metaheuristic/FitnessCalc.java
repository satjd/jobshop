package jobshop.algorithm.metaheuristic;

import java.io.FileNotFoundException;

import jobshop.calctime.Scheduler;

public class FitnessCalc
{
	public static int mchCnt = 12;
	public static int jobCnt = 15;
	public static int stepCnt = 12;
	public static String testcase_pcd = "E:\\Java codes\\workspace\\jobshop\\testcase\\case4_pcd.txt";
	public static String testcase_time = "E:\\Java codes\\workspace\\jobshop\\testcase\\case4_time.txt";
	
	public static double calcFitness(Particle pt) throws FileNotFoundException //计算个体适应度
	{
		return 1.0/(Scheduler.calcTime(mchCnt,jobCnt,stepCnt,testcase_pcd,testcase_time,pt));
	}
}
