package jobshop.calctime;


/*
 * Event.java  �����������¼�����
 * 
 * Event��
 * 		enum StatusSet{NEW_JOB_ARRIVED,JOB_SETUP,JOB_RELEASED}  �������¼���״̬����
 * 			NEW_JOB_ARRIVED---->  �¹������������ڳ�ʼ�������ӹ�����
 * 			JOB_SETUP      ---->  �������Դӻ�����������ȡ�����Ƿ���Լӹ�ȡ���ڻ�����״̬�Ƿ�Ϊ����״̬
 * 			JOB_RELEASED   ---->  ������ɣ��ӻ������ͷ�
 * 		StatusSet eventStatus �����ĵ�ǰ״̬
 * 		int eventTime  �¼��ķ���ʱ��
 * 		int machineNum �¼���Ӧ�Ļ���ID
 * 		int partNum    �¼���Ӧ��������
 * 
 */

public class Event implements Comparable<Event>
{
	//�����¼���״̬����
	public static enum StatusSet{NEW_JOB_ARRIVED,JOB_SETUP,JOB_RELEASED};
	
	private StatusSet eventStatus; //�����ĵ�ǰ״̬
	private int eventTime;         //�¼��ķ���ʱ��
	private int machineNum;        //�¼���Ӧ�Ļ���ID
	private int partNum;           //�¼���Ӧ��������
	
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
	public int compareTo(Event o) //����ʱ��˳�����¼�
	{
		// TODO �Զ����ɵķ������
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
