package jobshop.calctime;


class Event implements Comparable<Event>
{
	//定义事件的状态集合
	public static enum StatusSet{NEW_JOB_ARRIVED,JOB_SETUP,JOB_RELEASED};
	
	private StatusSet eventStatus;
	private int eventTime;
	private int machineNum;
	private int partNum;
	
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
		return (this.eventTime-o.eventTime);
	}
	public Event(StatusSet eventStatus,int eventTime,int machineNum,int partNum)
	{
		this.eventStatus = eventStatus;
		this.eventTime = eventTime;
		this.machineNum = machineNum;
		this.partNum = partNum;
	}
	
}
