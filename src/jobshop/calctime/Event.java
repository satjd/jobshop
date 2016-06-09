package jobshop.calctime;


/*
 * Event.java  定义了描述事件的类
 * 
 * Event类
 * 		enum StatusSet{NEW_JOB_ARRIVED,JOB_SETUP,JOB_RELEASED}  定义了事件的状态集合
 * 			NEW_JOB_ARRIVED---->  新工作到来，用于初始化整个加工过程
 * 			JOB_SETUP      ---->  工作可以从机器缓冲区中取出，是否可以加工取决于机器的状态是否为闲置状态
 * 			JOB_RELEASED   ---->  工作完成，从机器上释放
 * 		StatusSet eventStatus 机器的当前状态
 * 		int eventTime  事件的发生时间
 * 		int machineNum 事件对应的机器ID
 * 		int partNum    事件对应的零件编号
 * 
 */

public class Event implements Comparable<Event>
{
	//定义事件的状态集合
	public static enum StatusSet{NEW_JOB_ARRIVED,JOB_SETUP,JOB_RELEASED};
	
	private StatusSet eventStatus; //机器的当前状态
	private int eventTime;         //事件的发生时间
	private int machineNum;        //事件对应的机器ID
	private int partNum;           //事件对应的零件编号
	
	public int getEventTime()
	{
		return eventTime;
	}
	public void setEventTime(int eventTime)
	{
		this.eventTime = eventTime;
	}
	public int getMachineNum()
	{
		return machineNum;
	}
	public void setMachineNum(int machineNum)
	{
		this.machineNum = machineNum;
	}
	public int getPartNum()
	{
		return partNum;
	}
	public void setPartNum(int partNum)
	{
		this.partNum = partNum;
	}
	public StatusSet getEventStatus()
	{
		return eventStatus;
	}
	public void setEventStatus(StatusSet eventStatus)
	{
		this.eventStatus = eventStatus;
	}
	@Override
	public int compareTo(Event o) //基于时间顺序处理事件
	{
		// TODO 自动生成的方法存根
		if(this.getEventTime()!=o.getEventTime())
			return (this.eventTime-o.eventTime);
		else
		{
			if(this.eventStatus==StatusSet.JOB_RELEASED&&o.getEventStatus()==StatusSet.JOB_SETUP)
				return -1;
			else return 1;
		}
			
	}
	public Event(StatusSet eventStatus,int eventTime,int machineNum,int partNum)
	{
		this.eventStatus = eventStatus;
		this.eventTime = eventTime;
		this.machineNum = machineNum;
		this.partNum = partNum;
	}
	
}
