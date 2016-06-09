package jobshop.calctime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

/*
 * Trigger.java
 * 
 * 	SystemClock��      �����˵���ϵͳ��ʱ��
 * 		systemTime ����ϵͳʱ��
 * 		long getTime() ��ȡ��ǰ����ϵͳʱ��
 * 
 * 	Trigger��                 ���������ڴ���ʱ�䷢չ��ʱ�䲽���Ĵ�����
 * 		next()������������ʱ�䲽��������ʵ�����£�
 * 
 * 			��������е��¼�״̬ΪNEW_JOB_ARRIVED:
 * 				1.����״̬ΪJOB_SETUP���¼�������ʱ��Ϊ1
 * 				2.��ÿһ�������ĵ�һ������ӵ���Ӧ�����Ļ�������
 * 			��������е��¼�״̬ΪJOB_SETUP:
 * 				��������Ҫ�ӹ���������Ļ���״̬ΪIDLE:
 * 					1.JOB_SETUP�¼�ȡ��
 * 					2.����״̬����ΪPROCESSING
 * 					3.����״̬ΪJOB_RELEASED��ʱ�䣬����ʱ����JOB_SETUP�Ļ�����������jobDurTime���������ĳ���ʱ�䣩
 * 				��������Ҫ�ӹ���������Ļ���״̬ΪPROCESSING��
 * 					1.JOB_SETUP�¼����ã�����ʱ��ΪmachineIdleTime��¼��ʱ�̣�Ҳ������̨������һ�γ������õ�ʱ��ڵ�
 * 			��������е��¼�״̬ΪJOB_RELEASED:
 * 				1.�жϴ�ʱ�ù����Ƿ��Ѿ�ȫ���ӹ���ϣ����ӹ���ϣ�����½���ʱ��maxTime
 * 				2.��δ�ӹ���ϣ�ͨ��curStep��¼�����ҵ��ù�����Ӧ����һ�����������ù������뵽��������Ļ�������
 * 				3.����״̬ΪJOB_SETUP���¼�������ʱ���JOB_RELEASED��ʱ����ͬ
 * 				4.��JOB_RELEASED�¼���Ӧ�Ļ���״̬�޸�ΪIDLE
 */

class SystemClock
{
	//����ϵͳʱ��
	private long systemTime;
	public SystemClock(long initalTime)
	{
		this.systemTime = initalTime;
	}
	public long getTime()
	{
		return systemTime;
	}
}

public class Trigger     //������
{
	private static SystemClock sc = new SystemClock(0);
	private static long maxTime = 0; //���������ܵ�������JOB_RELEASED�¼�������ʱ��
	
	public static void resetTrigger()
	{
		sc = new SystemClock(0);
		maxTime = 0;
	}
	
	public static long getCurrentTime()
	{
		return sc.getTime();
	}
	
	public static long getMaxTime()
	{
		return maxTime;
	}
	
	public static int getPartnumByID(int id)
	{
		int index = 0;
		ArrayList<ArrayList<Integer>> idset = Scheduler.jobidSet; 
		for(Iterator<ArrayList<Integer>> it= idset.iterator();it.hasNext();index++)
		{
			ArrayList<Integer> a  = it.next();
			if(a.contains(id)) return index;
		}
		return -1;
	}
	
	public static boolean next(PriorityQueue<Event> pq,ArrayList<ArrayList<Integer>> jobSet,
			ArrayList<ArrayList<Integer>> timeSet,ArrayList<ArrayList<Integer>> jobidSet,
			ArrayList<Machine> machineSet,ArrayList<Integer> curStep) //����������ʱ�䲽��
	{
		//TODO 
		/*
		 * ��������е��¼�״̬ΪNEW_JOB_ARRIVED:
		 * 		1.����״̬ΪJOB_SETUP���¼�������ʱ��Ϊ1
		 * 		2.��ÿһ�������ĵ�һ������ӵ���Ӧ�����Ļ�������
		 * ��������е��¼�״̬ΪJOB_SETUP:
		 * 		��������Ҫ�ӹ���������Ļ���״̬ΪIDLE:
		 * 			1.JOB_SETUP�¼�ȡ��
		 * 			2.����״̬����ΪPROCESSING
		 * 			3.����״̬ΪJOB_RELEASED��ʱ�䣬����ʱ����JOB_SETUP�Ļ�����������jobDurTime���������ĳ���ʱ�䣩
		 * 		��������Ҫ�ӹ���������Ļ���״̬ΪPROCESSING��
		 * 			1.JOB_SETUP�¼����ã�����ʱ��+1s
		 * ��������е��¼�״̬ΪJOB_RELEASED:
		 * 		1.�жϴ�ʱ�ù����Ƿ��Ѿ�ȫ���ӹ���ϣ����ӹ���ϣ�����½���ʱ��maxTime
		 * 		2.��δ�ӹ���ϣ�ͨ��curStep��¼�����ҵ��ù�����Ӧ����һ�����������ù������뵽��������Ļ�������
		 * 		3.����״̬ΪJOB_SETUP���¼�������ʱ���JOB_RELEASED��ʱ����ͬ
		 * 		4.��JOB_RELEASED�¼���Ӧ�Ļ���״̬�޸�ΪIDLE
		 */
		if(!pq.isEmpty())
		{
			Event e = pq.poll();
			switch(e.getEventStatus())
			{
			case NEW_JOB_ARRIVED:
				//����һ������ӵ���Ӧ�����Ļ�������
				int curMachine = jobSet.get(e.getPartNum()).get(0);
				int curJobID = jobidSet.get(e.getPartNum()).get(0);
				//System.out.println("partNum="+e.getPartNum()+"step="+curStep.get(e.getPartNum()));
				int curJobTime = timeSet.get(e.getPartNum()).get(0);
				
				PriorityQueue<Job> curBuffer = machineSet.get(curMachine).getMachineBuffer();
				Job curJob = new Job(curJobID, curJobTime);
				curBuffer.add(curJob);
				curStep.set(e.getPartNum(),1+curStep.get(e.getPartNum()));
				
				//������һ���¼�
				Event ne = new Event(Event.StatusSet.JOB_SETUP, 1, curMachine, e.getPartNum());
				pq.add(ne);
				
				break;
				
			case JOB_RELEASED:
				boolean isAllScheduled = (curStep.get(e.getPartNum()))>(jobSet.get(0).size()); //�˹����Ƿ�ӹ����
				boolean hasNextJob = !((curStep.get(e.getPartNum()))==(jobSet.get(0).size())); //�˹����Ƿ�����һ������
				if(!isAllScheduled)
				{
					curMachine = e.getMachineNum();
					if(hasNextJob)
					{
						int nextMachine = jobSet.get(e.getPartNum()).get(curStep.get(e.getPartNum()));
						int nextJobID =  jobidSet.get(e.getPartNum()).get(curStep.get(e.getPartNum()));
						int nextJobTime = timeSet.get(e.getPartNum()).get(curStep.get(e.getPartNum()));
						Job nextJob = new Job(nextJobID, nextJobTime);
						PriorityQueue<Job> nextBuffer = machineSet.get(nextMachine).getMachineBuffer();
						nextBuffer.add(nextJob);
						curStep.set(e.getPartNum(),1+curStep.get(e.getPartNum()));

						//������һ���¼�
						ne = new Event(Event.StatusSet.JOB_SETUP,e.getEventTime(),nextMachine,e.getPartNum());
						pq.add(ne);
					}
					
					machineSet.get(curMachine).setMachineStatus(Machine.StatusSet.IDLE);
					maxTime = e.getEventTime()>maxTime?e.getEventTime():maxTime;
				}
//				else
//				{
//					System.out.println("�ܵ���ʱ����: "+maxTime);
//					return false;
//				}
				
				break;
				
			case JOB_SETUP:
				curMachine = e.getMachineNum();
				curBuffer = machineSet.get(curMachine).getMachineBuffer();
					
//				for(Job j:curBuffer)
//				{
//					System.out.println(j.getJobID()+"---"+j.getDurTime());
//				}
				curJob = curBuffer.poll();
				curJobTime = curJob.getDurTime();
				int curPart = getPartnumByID(curJob.getJobID());
				
				if(machineSet.get(curMachine).getMachineStatus()==Machine.StatusSet.IDLE)
				{
					ne = new Event(Event.StatusSet.JOB_RELEASED, e.getEventTime()+curJobTime, curMachine, curPart);
					pq.add(ne);
					//System.out.println(curMachine+"  "+(e.getEventTime()+curJobTime)+" "+(machineSet.get(curMachine).getMachineStatus()));
					machineSet.get(curMachine).setMachineIdleTime(e.getEventTime()+curJobTime);
					machineSet.get(curMachine).setMachineStatus(Machine.StatusSet.PROCESSING);
					
					//------PRINT-------
					//System.out.println(curMachine+"����"+"��"+e.getEventTime()+"s�ӹ�"+curPart+"�Ź���"+"��ʱ"+curJobTime);
				}
				else if(machineSet.get(curMachine).getMachineStatus()==Machine.StatusSet.PROCESSING)
				{
					
					//System.out.println("XXX");
					curBuffer.add(curJob);
					ne = new Event(Event.StatusSet.JOB_SETUP, (int)machineSet.get(curMachine).getMachineIdleTime(), curMachine, curPart);
					//ne = new Event(Event.StatusSet.JOB_SETUP, e.getEventTime()+1, curMachine, curPart);
					pq.add(ne);
				}
				
				break;
			default:
				break;
				
			}
		}
		else return false;
		
		return true;
		
	}
}
