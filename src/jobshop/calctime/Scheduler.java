package jobshop.calctime;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

import jobshop.algorithm.hrules.SPTrules;


class Scheduler
{
	//ʱ�����,�������,�����ž��� 
	private static ArrayList<ArrayList<Integer>> jobSet = new ArrayList<ArrayList<Integer>>();
	private static ArrayList<ArrayList<Integer>> timeSet = new ArrayList<ArrayList<Integer>>();
	private static ArrayList<ArrayList<Integer>> jobidSet = new ArrayList<ArrayList<Integer>>();
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
					jobLine.add(jobin.nextInt() - 1);
					timeLine.add(timein.nextInt());
					idline.add(id++);
				}
			}
			jobSet.add(jobLine);
			timeSet.add(timeLine);
			jobidSet.add(idline);
		}	
	}
	
	private static void initMachineSet(int mchCnt)  //��ʼ����������
	{
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
			
			SPTrules.setPriority(m);
			m.setMachineBuffer();
			machineSet.add(m);
			
		}
	}
	
	private static void initCurStep(int jobCnt)
	{
		for(int i=1;i<=jobCnt;i++)
			curStep.add(0);
	}
	
	public static long calcTime() throws FileNotFoundException
	{
		int mchCnt = 4;
		int jobCnt = 6;
		int stepCnt = 4;
		PriorityQueue<Event> pq = new PriorityQueue<Event>();
		inputData(mchCnt, jobCnt, stepCnt, "E:\\Java codes\\workspace\\jobshop\\testcase\\case2_pcd.txt", 
											"E:\\Java codes\\workspace\\jobshop\\testcase\\case2_time.txt");
//		for(Iterator<ArrayList<Integer>> it = jobSet.iterator();it.hasNext();)
//		{
//			for(Iterator<Integer> init = it.next().iterator();init.hasNext();)
//			{
//				System.out.print(init.next()+" ");
//			}
//			System.out.println();
//		}
		
		initMachineSet(mchCnt); //��ʼ����������
		initCurStep(jobCnt); //��ʼ���ӹ��̶�����
		
		for(int i=0;i<jobCnt;i++)
			pq.add(new Event(Event.StatusSet.NEW_JOB_ARRIVED,0,0,i));
		
		while(Trigger.next(pq, jobSet, timeSet, jobidSet, machineSet, curStep));
		
		return Trigger.getMaxTime();
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		// TODO �Զ����ɵķ������
		
		long time = calcTime();
		System.out.println("�ܵ���ʱ���ǣ�"+time);
	}

}
