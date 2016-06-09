package jobshop.algorithm.metaheuristic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import jobshop.calctime.Scheduler;
import jobshop.output.xls.OutputXls;

public class HybridPSOalgorithm
{
	public static ArrayList<Double> gbestList = new ArrayList<Double>();
	public static void main(String[] args) throws IOException
	{
		// TODO �Զ����ɵķ������
		
		int maxIteration = 1000;  //��������
		int swarmSize = 30;       //΢������
//		double c1 = 0.5, c2 = 0.3;//��������
		double c1 = 2.8, c2 = 1.3;//��������
		double inertia = 1.0;     //����Ȩ��
		double w = 0.5;
		
		ArrayList<Particle> swarm = new ArrayList<Particle>();
		ArrayList<Particle> pbestList = new ArrayList<Particle>();
		for(int i=1;i<=swarmSize;i++)
		{
			Particle newParticle = Scheduler.getParticleFromMachineSet();
			swarm.add(newParticle);         //������ɳ���΢����λ�����
			pbestList.add(newParticle);     //pbest��ʼ��Ϊ����
		}
		
		double gbestFitness = 0;
		double worstFitness = 100000.0;
		Particle gbest = null;
		Particle pBestWorst = null;
		
		for(int iter=1;iter<=maxIteration;iter++) //��ʼ����
		{
			if(iter==1)
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
				for(Particle p:pbestList) //��pbestList��Ѱ��pbestWorst
				{
					if(p.getFitness()<worstFitness)
					{
						worstFitness = p.getFitness();
						pBestWorst = p;
					}
				}
			}
			else 
			{
				for(int i=0;i<pbestList.size();i++)
				{
					Particle p = swarm.get(i);
					p.fly(pbestList.get(i), gbest, inertia, c1, c2);
					if(p.getFitness()>gbestFitness)
					{
						pBestWorst = gbest;
						gbest = p;
					}
					else if(p.getFitness()<gbestFitness&&p.getFitness()>pBestWorst.getFitness())
					{
						boolean flag = false;
						for(int j=0;j<pbestList.size();j++)
						{
							if(i!=j&&p.getFitness()==pbestList.get(j).getFitness())
							{
								flag = true;
							}
						}
						if(flag==false)
							pBestWorst = p;
					}
					else 
					{
						if(p.getFitness()==gbestFitness)
							{
								gbest = p;
								gbestFitness = p.getFitness();
							}
						else 
						{
							for(int j=0;j<pbestList.size();j++)
							{
								if(i!=j&&p.getFitness()==pbestList.get(j).getFitness())
								{
									pbestList.set(j, p);
								}
							}
						}
					}
					
				}
			}

			
			//------PRINT&SAVE------
			System.out.println(iter+": gbest="+gbestFitness+"  time="+(1.0/gbestFitness));
			gbestList.add(1.0/gbestFitness);
			
			//---------------------
			
			
//			for(int i=0;i<swarm.size();i++)
//			{
//				Particle p = swarm.get(i);
//				Particle pbest = pbestList.get(i);
//				//p.hybridFly(pbest, gbest,inertia,c1,c2,0.6);
//				p.fly(pbest, gbest, inertia, c1, c2);
//			}
			
		}
		
		//------OUTPUT------
		//OutputXls.outputResultToXLS("E:\\Java codes\\workspace\\jobshop\\result\\EXP2.xls","PSOgbest",gbestList);
		
		
		System.gc();
		
	}

}
