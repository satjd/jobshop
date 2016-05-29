package jobshop.algorithm.hrules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jobshop.calctime.*;

public class SPTrules
{	
	private static class Rule implements Comparator<Job>
	{

		@Override
		public int compare(Job o1, Job o2)
		{
			// TODO 自动生成的方法存根
			return (int) (o1.getDurTime()-o2.getDurTime());
		}
	}
	public static void setPriority(Machine M)
	{
		ArrayList<Job> joblist = M.getInstanceOfPriority();
		Collections.sort(joblist, new Rule());
	}
}
