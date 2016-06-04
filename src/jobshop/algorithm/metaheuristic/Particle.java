package jobshop.algorithm.metaheuristic;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;


import jobshop.calctime.Job;
import jobshop.calctime.Machine;

public class Particle
{
	
	public class JobPrioirtySetter implements Comparator<Job>  //用来通过向量设置每台机器工序优先级的比较器	
	{
		private Machine m;
		private int indexOfMachine;
		private ArrayList<Job> jobPriority;
		private ArrayList<Job> copyPriority;
		private ArrayList<Machine> mSet;
		JobPrioirtySetter(Machine m)
		{
			this.m = m;
			mSet = Particle.this.machineSet;
			indexOfMachine = Particle.this.machineSet.indexOf(m);
			jobPriority = m.getInstanceOfPriority();
			copyPriority = new ArrayList<Job>(jobPriority);
			//jobPriority = m.getInstanceOfPriority();
			
//			System.out.println("1-------------------"+indexOfMachine);
//			System.out.println(mSet);
//			for(Job i:mSet.get(0).getInstanceOfPriority())
//			{
//				System.out.println(i.getJobID()+"===="+i.getDurTime()+"======="+i.getPartNum()+"===="+machineSet.get(0).getInstanceOfPriority().indexOf(i));
//			}
//			System.out.println("1-------------------");
			
		}
		
		
		@Override
		public int compare(Job o1, Job o2)
		{
			// TODO 自动生成的方法存根
			if(o1.equals(o2)) return 0;
			//System.out.println(""+(mSet==Particle.this.machineSet)+" "+o1.getJobID()+" "+o2.getJobID()+"--->"+jobPriority.contains(o1));
//			if(mSet.get(0).getInstanceOfPriority().indexOf(o1)==-1) 
//			{
//				System.out.println("2-------------------"+indexOfMachine);
//				System.out.println(mSet);
//				for(Job i:mSet.get(0).getInstanceOfPriority())
//				{
//					System.out.println(i.getJobID()+"===="+i.getDurTime()+"======="+i.getPartNum()+"===="+machineSet.get(0).getInstanceOfPriority().indexOf(i));
//				}
//				System.out.println("2-------------------");
//				System.out.println(indexOfMachine+" "+o1.getJobID()+" "+o1.getDurTime()+" "+jobPriority.indexOf(o1));
//			}
//			if(mSet.get(0).getInstanceOfPriority().indexOf(o2)==-1)
//			{
//				System.out.println("2-------------------");
//				System.out.println(mSet);
//				for(Job i:mSet.get(0).getInstanceOfPriority())
//				{
//					System.out.println(i.getJobID()+"===="+i.getDurTime()+"======="+i.getPartNum()+"===="+machineSet.get(0).getInstanceOfPriority().indexOf(i));
//				}
//				System.out.println("2-------------------");
//				System.out.println(indexOfMachine+" "+o2.getJobID()+" "+o2.getDurTime()+" "+jobPriority.indexOf(o2));
//			}
			if (Particle.this.wArray.get(indexOfMachine).get(Particle.JobPrioirtySetter.this.copyPriority.indexOf(o1))
					<Particle.this.wArray.get(indexOfMachine).get(Particle.JobPrioirtySetter.this.copyPriority.indexOf(o2)))
				return 1;
			return -1;
		}
		
	}
	
	
	private int arraySize; //微粒位置向量每台机器分量的大小
	private double fitness = 0;
	private ArrayList<Machine> machineSet;  //微粒对应的机器集合
	private ArrayList<ArrayList<Double>> wArray; //微粒的位置（向量各个分量权值 的向量）
	private ArrayList<ArrayList<Double>> curV; //微粒的速度
	
	public Particle(int arraySize,ArrayList<Machine> machineSet)
	{
		this.arraySize = arraySize;
		this.machineSet = machineSet;
		this.wArray = new ArrayList<ArrayList<Double>>();
		this.curV = new ArrayList<ArrayList<Double>>();
		Random r = new Random();
		
		ArrayList<Double> tmpLw = new ArrayList<Double>();
		ArrayList<Double> tmpLc = new ArrayList<Double>();
		for(int i=0;i<arraySize;i++)
		{
			tmpLw.add((double)i);
			tmpLc.add(0.0);
		}
		
		for(Machine m:machineSet)
		{
			wArray.add(tmpLw);
			Collections.shuffle(tmpLw);
			curV.add(tmpLc);
		}
			
		
		System.out.println("-------------------");
		for(Job i:machineSet.get(0).getInstanceOfPriority())
		{
			System.out.println(i.getJobID()+"===="+i.getDurTime()+"======="+i.getPartNum()+"===="
								+machineSet.get(0).getInstanceOfPriority().indexOf(i));
		}
		System.out.println("-------------------");
		
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
			Particle.JobPrioirtySetter jobPrioritySetter = this.new JobPrioirtySetter(m);
			//Collections.sort(m.getInstanceOfPriority(), jobPrioritySetter);
			m.getInstanceOfPriority().sort(jobPrioritySetter);
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
		double rand = r.nextDouble();
		for(int i=0;i<curV.size();i++)
		{
			ArrayList<Double> v = curV.get(i); //速度的分量
			ArrayList<Double> curArray = wArray.get(i); //位置的分量
			ArrayList<Double> pbesti = pBest.getWArray().get(i); //pbest对应位置分量
			ArrayList<Double> gbesti = gBest.getWArray().get(i); //gbest对应位置分量
			
			for(int j=0;j<v.size();j++)
			{
				double tmp = v.get(j);
				tmp += c1*rand*(pbesti.get(j)-curArray.get(j)) + c2*rand*(gbesti.get(j)-curArray.get(j));
				v.set(j, tmp); //更新速度
				curArray.set(j, tmp+curArray.get(j)); //更新位置
			}
		}
		//System.out.println("("+wArray.get(0).get(0)+","+wArray.get(0).get(1)+")");
		updateMachineSet(); //更新对应机器的工序优先级
	}
}
