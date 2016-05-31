package jobshop.algorithm.hrules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jobshop.calctime.Job;
import jobshop.calctime.Machine;

public class WSPTrules implements AbstractRules
{	
	double weight;
	private static class Rule implements Comparator<Job>
	{
		private double weight;
		
		public Rule(double weight)
		{
			// TODO �Զ����ɵĹ��캯�����
			this.weight = weight;
		}

		@Override
		public int compare(Job o1, Job o2)
		{
			// TODO �Զ����ɵķ������
			return (int)(100.0*((weight*(o1.getJobID()-o2.getJobID())+(1.0-weight)*(o1.getDurTime()-o2.getDurTime()))));
		}
	}
	
	public WSPTrules(double weight)
	{
		// TODO �Զ����ɵĹ��캯�����
		this.weight = weight;
	}
	
	public void setPriority(Machine M)
	{
		ArrayList<Job> joblist = M.getInstanceOfPriority();
		Collections.sort(joblist, new Rule(weight));
	}
}
