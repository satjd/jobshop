package jobshop.calctime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/*
 * Machine.java
 * 
 * JobComparator类           定义了在优先级队列中元素之间的优先关系
 * 		jobPriority  优先级向量，用来描述优先级。在这个ArrayList中下表越小的元素优先级越高
 * 
 * Machine类         定义了机器对象
 * 		enum StatusSet{PROCESSING,IDLE}  机器的状态集合：PROCESSING---->加工状态，IDLE---->闲置状态
 * 		StatusSet machineStatus          机器的当前状态
 * 		long machineID                   机器的唯一编号ID
 * 		long machineIdleTime             机器在下一次闲置的时间节点
 * 		ArrayList<Job> jobPriority		   对于一台机器的工件优先级，由特定的算法生成
 * 		PriorityQueue<Job> machineBuffer 机器的缓冲区，当job发生抢占时进行优先级选择
 */




class JobComparator implements Comparator<Job>
{
	private ArrayList<Job> jobPriority;
	public JobComparator(ArrayList<Job> jobPriority)
	{
		this.jobPriority = jobPriority;
	}
	@Override
	public int compare(Job o1, Job o2)
	{
		// TODO 自动生成的方法存根
		//System.out.println(o1.getDurTime()+"<-->"+o2.getDurTime()+":"+"index1"+(jobPriority.indexOf(o1))+"index2:"+(jobPriority.indexOf(o2)));
		return (jobPriority.indexOf(o1)-jobPriority.indexOf(o2));
	}
	
}
public class Machine
{
	public static enum StatusSet{PROCESSING,IDLE} //机器的状态集合
	private StatusSet machineStatus = StatusSet.IDLE;
	private long machineID;    //机器的序号
	private long machineIdleTime = 1; //机器在下一次闲置的时间节点
	private ArrayList<Job> jobPriority = new ArrayList<Job>(); //对于一台机器的工件优先级，由特定的算法生成
	private PriorityQueue<Job> machineBuffer; //机器的缓冲区，当job发生抢占时进行优先级选择
	
	public Machine(long id)
	{
		this.machineID = id;
	}
	public long getMachineID()
	{
		return machineID;
	}
	public long getMachineIdleTime()
	{
		return machineIdleTime;
	}
	public void setMachineIdleTime(long time)
	{
		this.machineIdleTime = time;
	}
	public ArrayList<Job> getInstanceOfPriority()
	{
		return jobPriority;
	}
	public PriorityQueue<Job> getMachineBuffer()
	{
		return machineBuffer;
	}
	public void setMachineBuffer()
	{
//		System.out.println("-------------------");
//		for(Job i:jobPriority)
//		{
//			System.out.println(i.getJobID()+"===="+i.getDurTime()+"======="+i.getPartNum());
//		}
//		System.out.println("-------------------");
		machineBuffer = new PriorityQueue<Job>(new JobComparator(jobPriority));
	}
	public void setMachineStatus(StatusSet machineStatus)
	{
		this.machineStatus = machineStatus;
	}
	public StatusSet getMachineStatus()
	{
		return machineStatus;
	}
	
	public boolean equals(Object o)
	{
		Machine m = (Machine)o;
		if(machineID==m.getMachineID()) return true;
		return false;
	}
}
