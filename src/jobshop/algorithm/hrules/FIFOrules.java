package jobshop.algorithm.hrules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jobshop.calctime.Job;
import jobshop.calctime.Machine;

public class FIFOrules
{	
	private static class Rule implements Comparator<Job>
	{
		@Override
		public int compare(Job o1, Job o2)
		{
			// TODO �Զ����ɵķ������
			return o1.getJobID()-o2.getJobID();
		}
	}
	public static void setPriority(Machine M)
	{
		ArrayList<Job> joblist = M.getInstanceOfPriority();
		Collections.sort(joblist, new Rule());
	}
}
