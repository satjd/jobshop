package jobshop.calctime;

/*
 * Job.java
 * 
 * 	Job类       描述了工序的信息
 * 		int jobID       每个工序的唯一识别码ID
 * 		int jobDurTime  工序对应的加工时间
 * 		int partNum     这个工序属于哪个工件
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
