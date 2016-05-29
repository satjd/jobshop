package jobshop.calctime;

public class Job
{
	private int jobID;
	private int jobDurTime;
	public Job(int id,int time)
	{
		this.jobID = id;
		this.jobDurTime = time;
	}
	public int getDurTime()
	{
		return jobDurTime;
	}
	public int getJobID()
	{
		return jobID;
	}
	public boolean equals(Object j)
	{
		Job job = (Job)j;
		if(job.getDurTime()==jobDurTime&&job.getJobID()==jobID)
		{
			return true;
		}
		return false;
	}
}
