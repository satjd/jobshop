package jobshop.algorithm.metaheuristic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import jobshop.calctime.Scheduler;
import jobshop.output.xls.OutputXls;

public class HybridPSOalgorithm
{
	public static ArrayList<Double> gbestList = new ArrayList<Double>();
	public static void main(String[] args) throws IOException
	{
		// TODO �Զ����ɵķ������
		
		int maxIteration = 2000;  //��������
		int swarmSize = 30;       //΢������
		double c1 = 2.8, c2 = 1.3;//�������� 
		double inertia = 1.0;     //����Ȩ��
		double lambda = 0.99;      //����ϵ��
		double initialTemp = 0;   //��ʼ�¶�
		
		ArrayList<Particle> swarm = new ArrayList<Particle>();
		ArrayList<Particle> pbestList = new ArrayList<Particle>();
		Random r = new Random();
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
			double localGbestFitness = 0;
			Particle localGbest =  pbestList.get(0);
			for(Particle p:pbestList) //��pbestList��Ѱ��gbest
			{
				if(p.getFitness()>localGbestFitness)
				{
					localGbestFitness = p.getFitness();
					localGbest = p;
				}
			}
			
			if(localGbestFitness>gbestFitness)
			{
				gbestFitness = localGbestFitness;
				gbest = localGbest;
			}
			else 
			{
				if(r.nextDouble()<Math.exp((1.0/gbestFitness-1.0/localGbestFitness)/initialTemp))
				{
					gbestFitness = localGbestFitness;
					gbest = localGbest;
					System.out.println("----------------");
				}
			}
			
			if(iter==1) initialTemp = 1.609438/gbestFitness;//���鹫ʽ
			else 
			{
				initialTemp*=lambda;                           //����
			}
			//------PRINT&SAVE------
			System.out.println(iter+": gbest="+gbestFitness+"  time="+(1.0/gbestFitness));
			gbestList.add(1.0/gbestFitness);
			
			//---------------------
			
			
			for(int i=0;i<swarm.size();i++)
			{
				Particle p = swarm.get(i);
				Particle pbest = pbestList.get(i);
				//p.hybridFly(pbest, gbest,inertia,c1,c2,0.6);
				p.fly(pbest, gbest, inertia, c1, c2);
			}
		}
		
		//------OUTPUT------
		OutputXls.outputResultToXLS("E:\\Java codes\\workspace\\jobshop\\result\\EXP2.xls","PSOgbest",gbestList);
		
		
		System.gc();
		
	}

}
