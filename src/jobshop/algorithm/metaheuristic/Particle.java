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
	private ArrayList<ArrayList<Double>> curV; //΢�����ٶ�
	
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
	
	public void updateMachineSet() //������������΢����Ӧ�Ļ�����������˳��
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
	
	public void fly(Particle pBest,Particle gBest,double c1,double c2) //΢������
	{
		Random r = new Random();
		for(int i=0;i<curV.size();i++)
		{
			ArrayList<Double> v = curV.get(i); //�ٶȵķ���
			ArrayList<Double> curArray = wArray.get(i); //λ�õķ���
			ArrayList<Double> pbesti = pBest.getWArray().get(i); //pbest��Ӧλ�÷���
			ArrayList<Double> gbesti = gBest.getWArray().get(i); //gbest��Ӧλ�÷���
			
			for(int j=0;j<v.size();j++)
			{
				double tmp = v.get(j);
				double rand = r.nextDouble();
				tmp += c1*rand*(pbesti.get(j)-curArray.get(j)) + c2*rand*(gbesti.get(j)-curArray.get(j));
				v.set(j, tmp); //�����ٶ�
				curArray.set(j, tmp+curArray.get(j)); //����λ��
			}
		}
		updateMachineSet(); //���¶�Ӧ�����Ĺ������ȼ�
	}
}
