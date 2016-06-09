package jobshop.algorithm.metaheuristic;

import java.io.FileNotFoundException;

/*
 * FitnessCalc.java
 * 	metaheuristic.FitnessCalc类    封装了对于PSO算法的适应度计算函数
 * 		各个字段描述了输入数据文件的位置以及用例的相关信息
 * 		double calcFitness(Particle pt) 根据个体微粒内的调度方案计算个体适应度
 */

import jobshop.calctime.Scheduler;

/*
 * metaheuristic.FitnessCalc.java
 * 
 * FitnessCalc类        封装了测试用例的相关数据，便于输入
 * 		函数double calcFitness(Particle pt)用于计算个体适应度
 */



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
