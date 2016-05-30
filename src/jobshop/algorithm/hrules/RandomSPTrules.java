package jobshop.algorithm.hrules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import jobshop.calctime.Job;
import jobshop.calctime.Machine;

public class RandomSPTrules
{	
	private static class Rule implements Comparator<Job>
	{
		private int select;
		public Rule(int select)
		{
			this.select = select;
		}
		@Override
		public int compare(Job o1, Job o2)
		{
			// TODO �Զ����ɵķ������
			if(select==0)
			{
				return o1.getJobID()-o2.getJobID();
			}
			else
			{
				return o1.getDurTime()-o2.getDurTime();
			}
			
		}
	}
	public static void setPriority(Machine M)
	{
		int randomNum = new Random().nextInt(2);
		ArrayList<Job> joblist = M.getInstanceOfPriority();
		Collections.sort(joblist, new Rule(randomNum));
	}
}
