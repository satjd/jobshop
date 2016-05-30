package jobshop.calctime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

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

class Trigger
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
	
	private static int getPartnumByID(int id,ArrayList<ArrayList<Integer>> idset)
	{
		int index = 0;
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
				boolean hasNextJob = !((curStep.get(e.getPartNum()))==(jobSet.get(0).size()));
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
				int curPart = getPartnumByID(curJob.getJobID(),jobidSet);
				
				if(machineSet.get(curMachine).getMachineStatus()==Machine.StatusSet.IDLE)
				{
					ne = new Event(Event.StatusSet.JOB_RELEASED, e.getEventTime()+curJobTime, curMachine, curPart);
					pq.add(ne);
					machineSet.get(curMachine).setMachineStatus(Machine.StatusSet.PROCESSING);
					System.out.println(curMachine+"机器"+"在"+e.getEventTime()+"s加工"+curPart+"号工件"+"用时"+curJobTime);
				}
				else if(machineSet.get(curMachine).getMachineStatus()==Machine.StatusSet.PROCESSING)
				{
					curBuffer.add(curJob);
					ne = new Event(Event.StatusSet.JOB_SETUP, e.getEventTime()+1, curMachine, curPart);
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
