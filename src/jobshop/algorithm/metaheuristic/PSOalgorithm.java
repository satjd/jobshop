package jobshop.algorithm.metaheuristic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import jobshop.calctime.Scheduler;
import jobshop.output.xls.OutputXls;

public class PSOalgorithm
{
	public static ArrayList<Double> result = new ArrayList<Double>();
	public static void main(String[] args) throws IOException
	{
		// TODO �Զ����ɵķ������
		
		int maxIteration = 1000;   //��������
		int swarmSize = 30;       //΢������
		double c1 = 2.0, c2 = 2.0; //��������
		
		ArrayList<Particle> swarm = new ArrayList<Particle>();
		ArrayList<Particle> pbestList = new ArrayList<Particle>();
		for(int i=1;i<=swarmSize;i++)
		{
			Particle newParticle = Scheduler.getParticleFromMachineSet();
			swarm.add(newParticle);         //������ɳ���΢����λ�����
			pbestList.add(newParticle);     //pbest��ʼ��Ϊ����
		}
		
		double gbestFitness = 0;
		Particle gbest = null;
		for(int iter=1;iter<=maxIteration;iter++) //��ʼ����
		{
			for(int index=0;index<swarmSize;index++) //Ѱ��pbest����pbestList
			{
				Particle p = swarm.get(index);
				double curFitness = p.getFitness();
				double pbestFitness = pbestList.get(index).getFitness();
				if(curFitness>pbestFitness)
				{
					pbestFitness = curFitness;
					pbestList.set(index, p);
				}
			}
			for(Particle p:pbestList) //��pbestList��Ѱ��gbest
			{
				if(p.getFitness()>gbestFitness)
				{
					gbestFitness = p.getFitness();
					gbest = p;
				}
			}
			
			//------PRINT&SAVE------
			System.out.println(iter+": gbest="+gbestFitness+"  time="+(1.0/gbestFitness));
			result.add(1.0/gbestFitness);
			
			for(int i=0;i<swarm.size();i++)
			{
				Particle p = swarm.get(i);
				Particle pbest = pbestList.get(i);
				p.fly(pbest, gbest, 2.0, 2.0);
			}
		}
		
		//------OUTPUT------
		OutputXls.outputResultToXLS(result);
	}

}
