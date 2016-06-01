package jobshop.calctime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

class JobComparator implements Comparator<Job>
{
	private ArrayList<Job> jobPriority;
	public JobComparator(ArrayList<Job> jobPriority)
	{
		// TODO 自动生成的构造函数存根
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
	private long machineID;
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
