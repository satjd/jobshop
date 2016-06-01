package jobshop.algorithm.metaheuristic;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jobshop.calctime.Job;
import jobshop.calctime.Machine;

public class Particle
{
	
	public class JobPrioirtySetter implements Comparator<Job>  //����ͨ����������ÿ̨�����������ȼ��ıȽ���	
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
			// TODO �Զ����ɵķ������
			ArrayList<Job> jobPriority = m.getInstanceOfPriority();
			if(o1.equals(o2)) return 0;
			if (wArray.get(indexOfMachine).get(jobPriority.indexOf(o1))
					<wArray.get(indexOfMachine).get(jobPriority.indexOf(o2)))
				return 1;
			return -1;
		}
		
	}
	
	
	private int arraySize; //΢��λ������ÿ̨���������Ĵ�С
	private ArrayList<Machine> machineSet;  //΢����Ӧ�Ļ�������
	private ArrayList<ArrayList<Double>> wArray; //΢����λ�ã�������������Ȩֵ ��������
	
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
