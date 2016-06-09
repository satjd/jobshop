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
 * Scheduler类:
 * 	
 * 	jobSet：    工序对应的二维数组，从文件读入
 * 	timeSet： 加工时间对应的二维数组，从文件读入
 * 	jobidSet：对每个工序分配了一个唯一识别码jobid，用于区分在同一机器上的不同工作，也用于表示工作的先后顺序
 * 	machineSet：机器（Machine）类的对象集合
 * 	curStep：  记录了加工过程，实际上为每个工件下一道加工工序的实际编号
 * 
 * 	void inputData(int mchCnt,int jobCnt,int stepCnt,String fileNameJobset,String fileNameTimeset)：从文件中读入数据
 * 	void initMachineSet(int mchCnt,AbstractRules rule)： 初始化机器集合，按启发式规则制定优先级向量
 * 	void initMachineSet(int mchCnt,Chromosome cm,ArrayList<AbstractRules> ruleList)：初始化机器集合，按染色体和底层规则库制定优先级
 * 	Particle getParticleFromMachineSet()：通过机器集合得到对应粒子，用于PSO算法中
 * 	void initCurStep(int jobCnt)：初始化过程记录矩阵
 * 	long calcTime(int mchCnt,int jobCnt,int stepCnt,
 *								String testcase_pcd,String testcase_time,AbstractRules rule)：解出某一个启发式规则对应的加工调度时间和具体调度方案
 *
 * 	long calcTime(int mchCnt,int jobCnt,int stepCnt,
 *			String testcase_pcd,String testcase_time,Particle pt)：解出某一个确定的微粒对应的加工时间和对应方案
 * 	long calcTime(int mchCnt,int jobCnt,int stepCnt,
 *								String testcase_pcd,String testcase_time,
 *								Chromosome cm,ArrayList<AbstractRules> ruleList)：解出某一个染色体对应的加工时间和对应方案
 * 
 * 
 * 
 * 
 */

public class Scheduler
{
	//时间矩阵,工序矩阵,工序编号矩阵 
	private static ArrayList<ArrayList<Integer>> jobSet = new ArrayList<ArrayList<Integer>>();
	private static ArrayList<ArrayList<Integer>> timeSet = new ArrayList<ArrayList<Integer>>();
	public static ArrayList<ArrayList<Integer>> jobidSet = new ArrayList<ArrayList<Integer>>();
	//机器集合,工件加工程度数组
	private static ArrayList<Machine> machineSet = new ArrayList<Machine>();
	private static ArrayList<Integer> curStep = new ArrayList<Integer>();
	
	//从文件中读入数据
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
	
	//初始化机器集合，按启发式规则制定优先级向量
	private static void initMachineSet(int mchCnt,AbstractRules rule) 
	{
		machineSet.clear(); //清空机器集合
		
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
	
	//初始化机器集合，按染色体和底层规则库制定优先级
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
	
	//初始化过程记录矩阵
	private static void initCurStep(int jobCnt)
	{
		curStep.clear(); //清空过程记录矩阵
		for(int i=1;i<=jobCnt;i++)
			curStep.add(0);
	}
	
	//通过机器集合得到对应粒子，用于PSO算法中
	public static Particle getParticleFromMachineSet() throws FileNotFoundException
	{
		if(jobSet.size()==0)
			inputData(FitnessCalc.mchCnt, FitnessCalc.jobCnt, FitnessCalc.stepCnt, FitnessCalc.testcase_pcd, FitnessCalc.testcase_time);
		initMachineSet(FitnessCalc.mchCnt,new SPTrules());
		return new Particle(FitnessCalc.jobCnt,machineSet);
	}
	
	
	//解出某一个启发式规则对应的加工调度时间和具体调度方案
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
		
		Trigger.resetTrigger(); //重置触发器
		initMachineSet(mchCnt,rule); //初始化机器集合
		initCurStep(jobCnt); //初始化加工程度数组
		
		for(int i=0;i<jobCnt;i++)
			pq.add(new Event(Event.StatusSet.NEW_JOB_ARRIVED,0,0,i));
		
		while(Trigger.next(pq, jobSet, timeSet, jobidSet, machineSet, curStep));
		
		return Trigger.getMaxTime();
	}
	
	//解出某一个确定的微粒对应的加工时间和对应方案
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

		Trigger.resetTrigger(); //重置触发器
		//initMachineSet(mchCnt); //初始化机器集合
		initCurStep(jobCnt); //初始化加工程度数组

		for(int i=0;i<jobCnt;i++)
			pq.add(new Event(Event.StatusSet.NEW_JOB_ARRIVED,0,0,i));

		while(Trigger.next(pq, jobSet, timeSet, jobidSet, pt.getMachineSet() , curStep));

		return Trigger.getMaxTime();
	}
	
	//解出某一个染色体对应的加工时间和对应方案
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
		
		Trigger.resetTrigger(); //重置触发器
		initMachineSet(mchCnt,cm,ruleList); //初始化机器集合
		initCurStep(jobCnt); //初始化加工程度数组
		
		for(int i=0;i<jobCnt;i++)
			pq.add(new Event(Event.StatusSet.NEW_JOB_ARRIVED,0,0,i));
		
		while(Trigger.next(pq, jobSet, timeSet, jobidSet, machineSet, curStep));
		
		return Trigger.getMaxTime();
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		// TODO 自动生成的方法存根
		int mchCnt = 4;
		int jobCnt = 6;
		int stepCnt = 4;
		String testcase_pcd = "E:\\Java codes\\workspace\\jobshop\\testcase\\case2_pcd.txt";
		String testcase_time = "E:\\Java codes\\workspace\\jobshop\\testcase\\case2_time.txt"; 
		long t = System.currentTimeMillis();
		for(int i=1;i<=1;i++)
		{
			long time = calcTime(mchCnt,jobCnt,stepCnt,testcase_pcd,testcase_time,new SPTrules()); 
			System.out.println("总调度时间是："+time);
		}
		System.out.println(System.currentTimeMillis()-t);
		
	}

}
