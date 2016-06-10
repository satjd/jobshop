package jobshop.algorithm.metaheuristic;

import java.io.FileNotFoundException;

/*
 * FitnessCalc.java
 * 	metaheuristic.FitnessCalc��    ��װ�˶���PSO�㷨����Ӧ�ȼ��㺯��
 * 		�����ֶ����������������ļ���λ���Լ������������Ϣ
 * 		double calcFitness(Particle pt) ���ݸ���΢���ڵĵ��ȷ������������Ӧ��
 */

import jobshop.calctime.Scheduler;

/*
 * metaheuristic.FitnessCalc.java
 * 
 * FitnessCalc��        ��װ�˲���������������ݣ���������
 * 		����double calcFitness(Particle pt)���ڼ��������Ӧ��
 */



public class FitnessCalc
{
	public static int mchCnt = 12;
	public static int jobCnt = 15;
	public static int stepCnt = 12;
	public static String testcase_pcd = "E:\\Java codes\\workspace\\jobshop\\testcase\\case4_pcd.txt";
	public static String testcase_time = "E:\\Java codes\\workspace\\jobshop\\testcase\\case4_time.txt";
	
	public static double calcFitness(Particle pt) throws FileNotFoundException //���������Ӧ��
	{
		return 1.0/(Scheduler.calcTime(mchCnt,jobCnt,stepCnt,testcase_pcd,testcase_time,pt)-1);
	}
}
