package jobshop.calctime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

/*
 * Trigger.java
 * 
 * 	SystemClock类      定义了调度系统的时钟
 * 		systemTime 调度系统时间
 * 		long getTime() 获取当前调度系统时间
 * 
 * 	Trigger类                 定义了用于处理时间发展和时间步进的触发器
 * 		next()函数用来进行时间步进：具体实现如下：
 * 
 * 			若加入队列的事件状态为NEW_JOB_ARRIVED:
 * 				1.创建状态为JOB_SETUP的事件，发生时间为1
 * 				2.将每一个工件的第一道工序加到对应机器的缓冲区中
 * 			若加入队列的事件状态为JOB_SETUP:
 * 				若现在需要加工这个工件的机器状态为IDLE:
 * 					1.JOB_SETUP事件取出
 * 					2.机器状态更改为PROCESSING
 * 					3.创建状态为JOB_RELEASED的时间，发生时间在JOB_SETUP的基础上向后加上jobDurTime（这个工序的持续时间）
 * 				若现在需要加工这个工件的机器状态为PROCESSING：
 * 					1.JOB_SETUP事件搁置，发生时间为machineIdleTime记录的时刻，也就是这台机器下一次出现闲置的时间节点
 * 			若加入队列的事件状态为JOB_RELEASED:
 * 				1.判断此时该工件是否已经全部加工完毕，若加工完毕，则更新结束时间maxTime
 * 				2.若未加工完毕，通过curStep记录矩阵找到该工件对应的下一个机器，将该工件加入到这个机器的缓冲区中
 * 				3.创建状态为JOB_SETUP的事件，发生时间和JOB_RELEASED的时间相同
 * 				4.将JOB_RELEASED事件对应的机器状态修改为IDLE
 */

class SystemClock
{
	//定义系统时钟
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

public class Trigger     //触发器
{
	private static SystemClock sc = new SystemClock(0);
	private static long maxTime = 0; //触发器接受到的最晚JOB_RELEASED事件发生的时间
	
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
			ArrayList<Machine> machineSet,ArrayList<Integer> curStep) //触发器进行时间步进
	{
		//TODO 
		/*
		 * 若加入队列的事件状态为NEW_JOB_ARRIVED:
		 * 		1.创建状态为JOB_SETUP的事件，发生时间为1
		 * 		2.将每一个工件的第一道工序加到对应机器的缓冲区中
		 * 若加入队列的事件状态为JOB_SETUP:
		 * 		若现在需要加工这个工件的机器状态为IDLE:
		 * 			1.JOB_SETUP事件取出
		 * 			2.机器状态更改为PROCESSING
		 * 			3.创建状态为JOB_RELEASED的时间，发生时间在JOB_SETUP的基础上向后加上jobDurTime（这个工序的持续时间）
		 * 		若现在需要加工这个工件的机器状态为PROCESSING：
		 * 			1.JOB_SETUP事件搁置，发生时间+1s
		 * 若加入队列的事件状态为JOB_RELEASED:
		 * 		1.判断此时该工件是否已经全部加工完毕，若加工完毕，则更新结束时间maxTime
		 * 		2.若未加工完毕，通过curStep记录矩阵找到该工件对应的下一个机器，将该工件加入到这个机器的缓冲区中
		 * 		3.创建状态为JOB_SETUP的事件，发生时间和JOB_RELEASED的时间相同
		 * 		4.将JOB_RELEASED事件对应的机器状态修改为IDLE
		 */
		if(!pq.isEmpty())
		{
			Event e = pq.poll();
			switch(e.getEventStatus())
			{
			case NEW_JOB_ARRIVED:
				//将第一道工序加到对应机器的缓冲区中
				int curMachine = jobSet.get(e.getPartNum()).get(0);
				int curJobID = jobidSet.get(e.getPartNum()).get(0);
				//System.out.println("partNum="+e.getPartNum()+"step="+curStep.get(e.getPartNum()));
				int curJobTime = timeSet.get(e.getPartNum()).get(0);
				
				PriorityQueue<Job> curBuffer = machineSet.get(curMachine).getMachineBuffer();
				Job curJob = new Job(curJobID, curJobTime);
				curBuffer.add(curJob);
				curStep.set(e.getPartNum(),1+curStep.get(e.getPartNum()));
				
				//加入下一个事件
				Event ne = new Event(Event.StatusSet.JOB_SETUP, 1, curMachine, e.getPartNum());
				pq.add(ne);
				
				break;
				
			case JOB_RELEASED:
				boolean isAllScheduled = (curStep.get(e.getPartNum()))>(jobSet.get(0).size()); //此工件是否加工完毕
				boolean hasNextJob = !((curStep.get(e.getPartNum()))==(jobSet.get(0).size())); //此工件是否还有下一道工序
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

						//加入下一个事件
						ne = new Event(Event.StatusSet.JOB_SETUP,e.getEventTime(),nextMachine,e.getPartNum());
						pq.add(ne);
					}
					
					machineSet.get(curMachine).setMachineStatus(Machine.StatusSet.IDLE);
					maxTime = e.getEventTime()>maxTime?e.getEventTime():maxTime;
				}
//				else
//				{
//					System.out.println("总调度时间是: "+maxTime);
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
					//System.out.println(curMachine+"机器"+"在"+e.getEventTime()+"s加工"+curPart+"号工件"+"用时"+curJobTime);
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
