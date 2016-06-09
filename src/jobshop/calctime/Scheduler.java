package jobshop.calctime;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

import jobshop.algorithm.hrules.AbstractRules;
import jobshop.algorithm.hrules.LWKRrules;
import jobshop.algorithm.hrules.MWKRrules;
import jobshop.algorithm.hrules.RandomSPTrules;
import jobshop.algorithm.hrules.SPTrules;
import jobshop.algorithm.hrules.WSPTrules;
import jobshop.algorithm.hyperheuristic.Chromosome;
import jobshop.algorithm.metaheuristic.FitnessCalc;
import jobshop.algorithm.metaheuristic.Particle;



/*
 * Scheduler.java
 * Scheduler��:
 * 	
 * 	jobSet��    �����Ӧ�Ķ�ά���飬���ļ�����
 * 	timeSet�� �ӹ�ʱ���Ӧ�Ķ�ά���飬���ļ�����
 * 	jobidSet����ÿ�����������һ��Ψһʶ����jobid������������ͬһ�����ϵĲ�ͬ������Ҳ���ڱ�ʾ�������Ⱥ�˳��
 * 	machineSet��������Machine����Ķ��󼯺�
 * 	curStep��  ��¼�˼ӹ����̣�ʵ����Ϊÿ��������һ���ӹ������ʵ�ʱ��
 * 
 * 	void inputData(int mchCnt,int jobCnt,int stepCnt,String fileNameJobset,String fileNameTimeset)�����ļ��ж�������
 * 	void initMachineSet(int mchCnt,AbstractRules rule)�� ��ʼ���������ϣ�������ʽ�����ƶ����ȼ�����
 * 	void initMachineSet(int mchCnt,Chromosome cm,ArrayList<AbstractRules> ruleList)����ʼ���������ϣ���Ⱦɫ��͵ײ������ƶ����ȼ�
 * 	Particle getParticleFromMachineSet()��ͨ���������ϵõ���Ӧ���ӣ�����PSO�㷨��
 * 	void initCurStep(int jobCnt)����ʼ�����̼�¼����
 * 	long calcTime(int mchCnt,int jobCnt,int stepCnt,
 *								String testcase_pcd,String testcase_time,AbstractRules rule)�����ĳһ������ʽ�����Ӧ�ļӹ�����ʱ��;�����ȷ���
 *
 * 	long calcTime(int mchCnt,int jobCnt,int stepCnt,
 *			String testcase_pcd,String testcase_time,Particle pt)�����ĳһ��ȷ����΢����Ӧ�ļӹ�ʱ��Ͷ�Ӧ����
 * 	long calcTime(int mchCnt,int jobCnt,int stepCnt,
 *								String testcase_pcd,String testcase_time,
 *								Chromosome cm,ArrayList<AbstractRules> ruleList)�����ĳһ��Ⱦɫ���Ӧ�ļӹ�ʱ��Ͷ�Ӧ����
 * 
 * 
 * 
 * 
 */

public class Scheduler
{
	//ʱ�����,�������,�����ž��� 
	private static ArrayList<ArrayList<Integer>> jobSet = new ArrayList<ArrayList<Integer>>();
	private static ArrayList<ArrayList<Integer>> timeSet = new ArrayList<ArrayList<Integer>>();
	public static ArrayList<ArrayList<Integer>> jobidSet = new ArrayList<ArrayList<Integer>>();
	//��������,�����ӹ��̶�����
	private static ArrayList<Machine> machineSet = new ArrayList<Machine>();
	private static ArrayList<Integer> curStep = new ArrayList<Integer>();
	
	//���ļ��ж�������
	private static void inputData(int mchCnt,int jobCnt,int stepCnt,String fileNameJobset,String fileNameTimeset) throws FileNotFoundException
	{
		Scanner jobin = new Scanner(new FileInputStream(fileNameJobset));
		Scanner timein = new Scanner(new FileInputStream(fileNameTimeset));
		
		for(int i=1,id = 1;i<=jobCnt;i++)
		{
			ArrayList<Integer> jobLine = new ArrayList<Integer>();
			ArrayList<Integer> timeLine = new ArrayList<Integer>();
			ArrayList<Integer> idline = new ArrayList<Integer>();
			
			for(int j=1;j<=stepCnt;j++)
			{
				if(jobin.hasNextInt()&&timein.hasNextInt())
				{
					jobLine.add(jobin.nextInt()-1);
					timeLine.add(timein.nextInt());
					idline.add(id++);
				}
			}
			jobSet.add(jobLine);
			timeSet.add(timeLine);
			jobidSet.add(idline);
		}	
	}
	
	//��ʼ���������ϣ�������ʽ�����ƶ����ȼ�����
	private static void initMachineSet(int mchCnt,AbstractRules rule) 
	{
		machineSet.clear(); //��ջ�������
		
		for(int i=0;i<mchCnt;i++)
		{
			Machine m = new Machine(i);
			ArrayList<Job>  jlist = m.getInstanceOfPriority();
			
			for(Iterator<ArrayList<Integer>> it1 = jobSet.iterator(),
					it2 = timeSet.iterator(),
					it3 = jobidSet.iterator();
					it1.hasNext()&&it2.hasNext()&&it3.hasNext();)
			{
				for(Iterator<Integer> init1 = it1.next().iterator(),
						init2 = it2.next().iterator(),init3 = it3.next().iterator()
						;init1.hasNext()&&init2.hasNext()&&init3.hasNext();)
				{
					int mchnum = init1.next();
					int time = init2.next();
					int id = init3.next();
					if(mchnum==i) 
					{
						jlist.add(new Job(id, time));
						//System.out.print(id+","+mchnum+","+time+" ");
					}
				}
				//System.out.println();
			}
			
			rule.setPriority(m);
			m.setMachineBuffer();
			machineSet.add(m);
			
		}
	}
	
	//��ʼ���������ϣ���Ⱦɫ��͵ײ������ƶ����ȼ�
	private static void initMachineSet(int mchCnt,Chromosome cm,ArrayList<AbstractRules> ruleList)
	{
		initMachineSet(mchCnt,new SPTrules());
		for(int i=0;i<mchCnt;i++)
		{
			Machine curMachine = machineSet.get(i);
			ArrayList<Integer> gene = cm.getGenes();
			ruleList.get(gene.get(i)).setPriority(curMachine);
		}
	}
	
	//��ʼ�����̼�¼����
	private static void initCurStep(int jobCnt)
	{
		curStep.clear(); //��չ��̼�¼����
		for(int i=1;i<=jobCnt;i++)
			curStep.add(0);
	}
	
	//ͨ���������ϵõ���Ӧ���ӣ�����PSO�㷨��
	public static Particle getParticleFromMachineSet() throws FileNotFoundException
	{
		if(jobSet.size()==0)
			inputData(FitnessCalc.mchCnt, FitnessCalc.jobCnt, FitnessCalc.stepCnt, FitnessCalc.testcase_pcd, FitnessCalc.testcase_time);
		initMachineSet(FitnessCalc.mchCnt,new SPTrules());
		return new Particle(FitnessCalc.jobCnt,machineSet);
	}
	
	
	//���ĳһ������ʽ�����Ӧ�ļӹ�����ʱ��;�����ȷ���
	public static long calcTime(int mchCnt,int jobCnt,int stepCnt,
								String testcase_pcd,String testcase_time,AbstractRules rule) throws FileNotFoundException
	{
		PriorityQueue<Event> pq = new PriorityQueue<Event>();
		inputData(mchCnt, jobCnt, stepCnt, testcase_pcd, 
											testcase_time);
//		for(Iterator<ArrayList<Integer>> it = jobSet.iterator();it.hasNext();)
//		{
//			for(Iterator<Integer> init = it.next().iterator();init.hasNext();)
//			{
//				System.out.print(init.next()+" ");
//			}
//			System.out.println();
//		}
		
		Trigger.resetTrigger(); //���ô�����
		initMachineSet(mchCnt,rule); //��ʼ����������
		initCurStep(jobCnt); //��ʼ���ӹ��̶�����
		
		for(int i=0;i<jobCnt;i++)
			pq.add(new Event(Event.StatusSet.NEW_JOB_ARRIVED,0,0,i));
		
		while(Trigger.next(pq, jobSet, timeSet, jobidSet, machineSet, curStep));
		
		return Trigger.getMaxTime();
	}
	
	//���ĳһ��ȷ����΢����Ӧ�ļӹ�ʱ��Ͷ�Ӧ����
	public static long calcTime(int mchCnt,int jobCnt,int stepCnt,
			String testcase_pcd,String testcase_time,Particle pt) throws FileNotFoundException
	{
		PriorityQueue<Event> pq = new PriorityQueue<Event>();
//		inputData(mchCnt, jobCnt, stepCnt, testcase_pcd, 
//				testcase_time);
		
		//for(Iterator<ArrayList<Integer>> it = jobSet.iterator();it.hasNext();)
		//{
		//for(Iterator<Integer> init = it.next().iterator();init.hasNext();)
		//{
		//System.out.print(init.next()+" ");
		//}
		//System.out.println();
		//}

		Trigger.resetTrigger(); //���ô�����
		//initMachineSet(mchCnt); //��ʼ����������
		initCurStep(jobCnt); //��ʼ���ӹ��̶�����

		for(int i=0;i<jobCnt;i++)
			pq.add(new Event(Event.StatusSet.NEW_JOB_ARRIVED,0,0,i));

		while(Trigger.next(pq, jobSet, timeSet, jobidSet, pt.getMachineSet() , curStep));

		return Trigger.getMaxTime();
	}
	
	//���ĳһ��Ⱦɫ���Ӧ�ļӹ�ʱ��Ͷ�Ӧ����
	public static long calcTime(int mchCnt,int jobCnt,int stepCnt,
								String testcase_pcd,String testcase_time,
								Chromosome cm,ArrayList<AbstractRules> ruleList) throws FileNotFoundException
	{
		PriorityQueue<Event> pq = new PriorityQueue<Event>();
		inputData(mchCnt, jobCnt, stepCnt, testcase_pcd, 
											testcase_time);
//		for(Iterator<ArrayList<Integer>> it = jobSet.iterator();it.hasNext();)
//		{
//			for(Iterator<Integer> init = it.next().iterator();init.hasNext();)
//			{
//				System.out.print(init.next()+" ");
//			}
//			System.out.println();
//		}
		
		Trigger.resetTrigger(); //���ô�����
		initMachineSet(mchCnt,cm,ruleList); //��ʼ����������
		initCurStep(jobCnt); //��ʼ���ӹ��̶�����
		
		for(int i=0;i<jobCnt;i++)
			pq.add(new Event(Event.StatusSet.NEW_JOB_ARRIVED,0,0,i));
		
		while(Trigger.next(pq, jobSet, timeSet, jobidSet, machineSet, curStep));
		
		return Trigger.getMaxTime();
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		// TODO �Զ����ɵķ������
		int mchCnt = 4;
		int jobCnt = 6;
		int stepCnt = 4;
		String testcase_pcd = "E:\\Java codes\\workspace\\jobshop\\testcase\\case2_pcd.txt";
		String testcase_time = "E:\\Java codes\\workspace\\jobshop\\testcase\\case2_time.txt"; 
		long t = System.currentTimeMillis();
		for(int i=1;i<=1;i++)
		{
			long time = calcTime(mchCnt,jobCnt,stepCnt,testcase_pcd,testcase_time,new SPTrules()); 
			System.out.println("�ܵ���ʱ���ǣ�"+time);
		}
		System.out.println(System.currentTimeMillis()-t);
		
	}

}
