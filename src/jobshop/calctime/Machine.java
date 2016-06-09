package jobshop.calctime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/*
 * Machine.java
 * 
 * JobComparator��           �����������ȼ�������Ԫ��֮������ȹ�ϵ
 * 		jobPriority  ���ȼ������������������ȼ��������ArrayList���±�ԽС��Ԫ�����ȼ�Խ��
 * 
 * Machine��         �����˻�������
 * 		enum StatusSet{PROCESSING,IDLE}  ������״̬���ϣ�PROCESSING---->�ӹ�״̬��IDLE---->����״̬
 * 		StatusSet machineStatus          �����ĵ�ǰ״̬
 * 		long machineID                   ������Ψһ���ID
 * 		long machineIdleTime             ��������һ�����õ�ʱ��ڵ�
 * 		ArrayList<Job> jobPriority		   ����һ̨�����Ĺ������ȼ������ض����㷨����
 * 		PriorityQueue<Job> machineBuffer �����Ļ���������job������ռʱ�������ȼ�ѡ��
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
		// TODO �Զ����ɵķ������
		//System.out.println(o1.getDurTime()+"<-->"+o2.getDurTime()+":"+"index1"+(jobPriority.indexOf(o1))+"index2:"+(jobPriority.indexOf(o2)));
		return (jobPriority.indexOf(o1)-jobPriority.indexOf(o2));
	}
	
}
public class Machine
{
	public static enum StatusSet{PROCESSING,IDLE} //������״̬����
	private StatusSet machineStatus = StatusSet.IDLE;
	private long machineID;    //���������
	private long machineIdleTime = 1; //��������һ�����õ�ʱ��ڵ�
	private ArrayList<Job> jobPriority = new ArrayList<Job>(); //����һ̨�����Ĺ������ȼ������ض����㷨����
	private PriorityQueue<Job> machineBuffer; //�����Ļ���������job������ռʱ�������ȼ�ѡ��
	
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
