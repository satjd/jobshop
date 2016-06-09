package jobshop.calctime;

/*
 * Job.java
 * 
 * 	Job��       �����˹������Ϣ
 * 		int jobID       ÿ�������Ψһʶ����ID
 * 		int jobDurTime  �����Ӧ�ļӹ�ʱ��
 * 		int partNum     ������������ĸ�����
 */


public class Job
{
	private int jobID;
	private int jobDurTime;
	private int partNum;
	public Job(int id,int time)
	{
		this.jobID = id;
		this.jobDurTime = time;
		this.partNum = Trigger.getPartnumByID(id);
	}
	public int getDurTime()
	{
		return jobDurTime;
	}
	public int getJobID()
	{
		return jobID;
	}
	public int getPartNum()
	{
		return partNum;
	}
	public boolean equals(Object j)
	{
		Job job = (Job)j;
		if(job.getJobID()==this.getJobID())
		{
			return true;
		}
		return false;
	}
}
