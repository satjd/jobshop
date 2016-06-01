package jobshop.algorithm.metaheuristic;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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
	private ArrayList<ArrayList<Double>> curV; //微粒的速度
	
	public Particle(int arraySize,ArrayList<Machine> machineSet)
	{
		this.arraySize = arraySize;
		this.machineSet = machineSet;
		this.wArray = new ArrayList<ArrayList<Double>>();
		this.curV = new ArrayList<ArrayList<Double>>();
		
		ArrayList<Double> tmpLw = new ArrayList<Double>();
		ArrayList<Double> tmpLc = new ArrayList<Double>();
		for(int i=0;i<arraySize;i++)
		{
			tmpLw.add((double)i);
			tmpLc.add(0.0);
		}
		
		for(Machine m:machineSet)
		{
			Collections.shuffle(tmpLw);
			wArray.add(tmpLw);
			curV.add(tmpLc);
		}
			
		updateMachineSet();
	}
	
	public ArrayList<Machine> getMachineSet()
	{
		return machineSet;
	}
	
	public void updateMachineSet() //根据向量更新微粒对应的机器调度优先顺序
	{
		for(Machine m:machineSet)
		{
			Collections.sort(m.getInstanceOfPriority(), new JobPrioirtySetter(m));
			//m.setMachineBuffer();
		}
	}
	
	public double getFitness() throws FileNotFoundException
	{
		return FitnessCalc.calcFitness(this);
	}
	
	public ArrayList<ArrayList<Double>> getCurV()
	{
		return curV;
	}
	
	public ArrayList<ArrayList<Double>> getWArray()
	{
		return wArray;
	}
	
	public void fly(Particle pBest,Particle gBest,double c1,double c2) //微粒迭代
	{
		Random r = new Random();
		for(int i=0;i<curV.size();i++)
		{
			ArrayList<Double> v = curV.get(i); //速度的分量
			ArrayList<Double> curArray = wArray.get(i); //位置的分量
			ArrayList<Double> pbesti = pBest.getWArray().get(i); //pbest对应位置分量
			ArrayList<Double> gbesti = gBest.getWArray().get(i); //gbest对应位置分量
			
			for(int j=0;j<v.size();j++)
			{
				double tmp = v.get(j);
				double rand = r.nextDouble();
				tmp += c1*rand*(pbesti.get(j)-curArray.get(j)) + c2*rand*(gbesti.get(j)-curArray.get(j));
				v.set(j, tmp); //更新速度
				curArray.set(j, tmp+curArray.get(j)); //更新位置
			}
		}
		updateMachineSet(); //更新对应机器的工序优先级
	}
}
