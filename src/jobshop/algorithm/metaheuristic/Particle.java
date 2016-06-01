package jobshop.algorithm.metaheuristic;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jobshop.calctime.Job;
import jobshop.calctime.Machine;

public class Particle
{
	
	public class JobPrioirtySetter implements Comparator<Job>  //用来通过向量设置每台机器工序优先级的比较器	
	{
		private Machine m;
		private int indexOfMachine;
		JobPrioirtySetter(Machine m)
		{
			this.m = m;
			indexOfMachine = machineSet.indexOf(m);
		}
		
		@Override
		public int compare(Job o1, Job o2)
		{
			// TODO 自动生成的方法存根
			ArrayList<Job> jobPriority = m.getInstanceOfPriority();
			if(o1.equals(o2)) return 0;
			if (wArray.get(indexOfMachine).get(jobPriority.indexOf(o1))
					<wArray.get(indexOfMachine).get(jobPriority.indexOf(o2)))
				return 1;
			return -1;
		}
		
	}
	
	
	private int arraySize; //微粒位置向量每台机器分量的大小
	private ArrayList<Machine> machineSet;  //微粒对应的机器集合
	private ArrayList<ArrayList<Double>> wArray; //微粒的位置（向量各个分量权值 的向量）
	
	public Particle(int arraySize,ArrayList<Machine> machineSet)
	{
		this.arraySize = arraySize;
		this.machineSet = machineSet;
		this.wArray = new ArrayList<ArrayList<Double>>();
		
		ArrayList<Double> tmpL = new ArrayList<Double>();
		for(int i=0;i<arraySize;i++)
			tmpL.add((double)i);
		
		for(Machine m:machineSet)
		{
			Collections.shuffle(tmpL);
			wArray.add(tmpL);
		}
			
		updateMachineSet();
	}
	
	public ArrayList<Machine> getMachineSet()
	{
		return machineSet;
	}
	
	public void updateMachineSet()
	{
		for(Machine m:machineSet)
		{
			Collections.sort(m.getInstanceOfPriority(), new JobPrioirtySetter(m));
		}
	}
	
	public double getFitness() throws FileNotFoundException
	{
		return FitnessCalc.calcFitness(this);
	}
}
